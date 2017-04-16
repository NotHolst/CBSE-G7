package dk.gruppe7.player;

import collision.CollisionEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.Input;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.ActionEventHandler;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.data.KeyEventHandler;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.mobcommon.MobData;
import dk.gruppe7.mobcommon.MobEvent;
import dk.gruppe7.mobcommon.MobEventType;
import dk.gruppe7.obstaclecommon.Obstacle;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.weaponcommon.WeaponData;
import dk.gruppe7.weaponcommon.WeaponEvent;
import java.util.List;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import dk.gruppe7.common.resources.Image;

@ServiceProvider(service = IProcess.class)

/**
 *
 * @author Mikkel
 */
public class PlayerSystem implements IProcess, IRender {
    Input input;
    boolean north, south, west, east;
    Vector2 aimDirection = Vector2.zero;

    Image texture;
    Image[] frames;

    UUID playerID;
    List<WeaponEvent> weaponEvents = WeaponData.getEvents();

    KeyEventHandler wKeyEventHandler = (newKeyState) -> {
        north = newKeyState;
    };
    KeyEventHandler aKeyEventHandler = (newKeyState) -> {
        west = newKeyState;
    };
    KeyEventHandler sKeyEventHandler = (newKeyState) -> {
        south = newKeyState;
    };
    KeyEventHandler dKeyEventHandler = (newKeyState) -> {
        east = newKeyState;
    };

    KeyEventHandler arrowUpKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.up;
            } else if (aimDirection.equals(Vector2.up)) {
                aimDirection = Vector2.zero;
            }
        }
    };

    KeyEventHandler arrowLeftKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.left;
            } else if (aimDirection.equals(Vector2.left)) {
                aimDirection = Vector2.zero;
            }
        }
    };

    KeyEventHandler arrowDownKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.down;
            } else if (aimDirection.equals(Vector2.down)) {
                aimDirection = Vector2.zero;
            }
        }
    };

    KeyEventHandler arrowRightKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.right;
            } else if (aimDirection.equals(Vector2.right)) {
                aimDirection = Vector2.zero;
            }
        }
    };

    @Override
    public void start(GameData gameData, World world) {

        frames = new Image[]{
            gameData.getResourceManager().addImage("frame0", getClass().getResourceAsStream("PlayerFeet0.png")),
            gameData.getResourceManager().addImage("frame1", getClass().getResourceAsStream("PlayerFeet1.png")),
            gameData.getResourceManager().addImage("frame2", getClass().getResourceAsStream("PlayerFeet2.png")),
            gameData.getResourceManager().addImage("frame3", getClass().getResourceAsStream("PlayerFeet3.png")),
            gameData.getResourceManager().addImage("frame4", getClass().getResourceAsStream("PlayerFeet4.png")),
            gameData.getResourceManager().addImage("frame5", getClass().getResourceAsStream("PlayerFeet5.png")),
            gameData.getResourceManager().addImage("frame6", getClass().getResourceAsStream("PlayerFeet6.png")),
            gameData.getResourceManager().addImage("frame7", getClass().getResourceAsStream("PlayerFeet7.png")),
            gameData.getResourceManager().addImage("frame8", getClass().getResourceAsStream("PlayerFeet8.png")),
            gameData.getResourceManager().addImage("frame9", getClass().getResourceAsStream("PlayerFeet9.png")),
            gameData.getResourceManager().addImage("frame10", getClass().getResourceAsStream("PlayerFeet10.png")),
            gameData.getResourceManager().addImage("frame11", getClass().getResourceAsStream("PlayerFeet11.png")),
            gameData.getResourceManager().addImage("frame12", getClass().getResourceAsStream("PlayerFeet12.png"))

        };

        Player temp = makePlayer();
        playerID = temp.getId();
        temp.setAnimator(
                new Animator(frames, .1f)
        );
        world.addEntity(temp);

        texture = gameData.getResourceManager().addImage("torso", getClass().getResourceAsStream("player.png"));
        //String torso = "torso";
        //InputStream stream = getClass().getResourceAsStream("player.png");
        //Image image = new Image(stream);
        //texture = 

        input = gameData.getInput();
        input.registerKeyEventHandler(VirtualKeyCode.VC_W, wKeyEventHandler);
        input.registerKeyEventHandler(VirtualKeyCode.VC_A, aKeyEventHandler);
        input.registerKeyEventHandler(VirtualKeyCode.VC_S, sKeyEventHandler);
        input.registerKeyEventHandler(VirtualKeyCode.VC_D, dKeyEventHandler);

        input.registerKeyEventHandler(VirtualKeyCode.VC_UP, arrowUpKeyEventHandler);
        input.registerKeyEventHandler(VirtualKeyCode.VC_LEFT, arrowLeftKeyEventHandler);
        input.registerKeyEventHandler(VirtualKeyCode.VC_DOWN, arrowDownKeyEventHandler);
        input.registerKeyEventHandler(VirtualKeyCode.VC_RIGHT, arrowRightKeyEventHandler);
        
        Dispatcher.subscribe(CollisionEvent.class, bulletCollisionHandler);
        Dispatcher.subscribe(CollisionEvent.class, obstacleCollisionHandler);
    }

    @Override
    public void stop(GameData gameData, World world) {
        input.unregisterKeyEventHandler(wKeyEventHandler, aKeyEventHandler, sKeyEventHandler, dKeyEventHandler);
        input.unregisterKeyEventHandler(arrowUpKeyEventHandler, arrowLeftKeyEventHandler, arrowDownKeyEventHandler, arrowRightKeyEventHandler);
        
        Dispatcher.unsubscribe(CollisionEvent.class, bulletCollisionHandler);
        Dispatcher.unsubscribe(CollisionEvent.class, obstacleCollisionHandler);
        
        world.removeEntity(world.getEntityByID(playerID));
        playerID = null;
    }

    @Override
    public void process(GameData gameData, World world) {
        Player playerEntity = (Player) world.getEntityByID(playerID);

        if (playerEntity != null && playerEntity.getHealthData().getHealth() > 0) {
            playerEntity.setVelocity(playerEntity.getVelocity()
                    .add(
                            playerEntity.getAcceleration() * (booleanToInt(east) - booleanToInt(west)),
                            playerEntity.getAcceleration() * (booleanToInt(north) - booleanToInt(south)))
                    .clampLength(
                            playerEntity.getMaxVelocity()
                    )
                    .mul(.9f)
            );

            playerEntity.setPosition(playerEntity.getPosition()
                    .add(playerEntity.getVelocity()
                            .mul(gameData.getDeltaTime())
                    ));

            if (!aimDirection.equals(Vector2.zero)) {
                playerEntity.setRotation((float) Math.toDegrees(Math.atan2(aimDirection.y, aimDirection.x)));
                weaponEvents.add(new WeaponEvent(playerEntity.getId()));
            } else if (playerEntity.getVelocity().len() > 50f) {
                playerEntity.setRotation((float) Math.toDegrees(Math.atan2(playerEntity.getVelocity().y, playerEntity.getVelocity().x)));
                
            }
            if(playerEntity.getVelocity().len() > .1f){
                playerEntity.getAnimator().setInterval(15*1.0f/playerEntity.getVelocity().len());
                playerEntity.getAnimator().update(gameData);
            }
            }
            if(playerEntity.getVelocity().len() > .1f){
                playerEntity.getAnimator().setInterval(15*1.0f/playerEntity.getVelocity().len());
                playerEntity.getAnimator().update(gameData);
            }

            for (MobEvent event : MobData.getEvents(gameData.getTickCount())) {
                if (event.getType() == MobEventType.DEATH) {
                    playerEntity.incrementScoreBy(1);
                }
            }
    }

    ActionEventHandler<CollisionEvent> bulletCollisionHandler = (event, world) -> {
        // Bullet collision -- Bullet collides with player the moment it spawns.
        //if(event.getOtherID().equals(playerID))
        //{
        //    Entity hitBy = world.getEntityByID(event.getTargetID());
        //    Player player = (Player) world.getEntityByID(playerID);

        //    Bullet b = Bullet.class.isInstance(hitBy) ? (Bullet)hitBy : null;
        //    if(b != null)
        //    {
        //        
        //        player.getHealthData().setHealth(player.getHealthData().getHealth() - b.getDamageData().getDamage());
        //        //Temporary: to avoid bullets hitting multiple times
        //        b.getDamageData().setDamage(0);
        //    }  
        //}
    };
    
    ActionEventHandler<CollisionEvent> obstacleCollisionHandler = (event, world) -> {
        if(Obstacle.class.isInstance(world.getEntityByID(event.getOtherID())) && event.getTargetID().equals(playerID)){ 
            Entity targetEntity = world.getEntityByID(event.getTargetID());
            Entity otherEntity = world.getEntityByID(event.getOtherID());
            
            float sumY = (targetEntity.getBounds().getWidth() + otherEntity.getBounds().getWidth()) * (targetEntity.getPositionCentered().y - otherEntity.getPositionCentered().y);
            float sumX = (targetEntity.getBounds().getHeight() + otherEntity.getBounds().getHeight()) * (targetEntity.getPositionCentered().x - otherEntity.getPositionCentered().x);
            
            if(sumY > sumX) {
                    if(sumY > -sumX) {
                        targetEntity.setPosition(new Vector2(targetEntity.getPosition().x, otherEntity.getPosition().y + otherEntity.getBounds().getHeight() + 0)); 
                    } else {
                        targetEntity.setPosition(new Vector2((otherEntity.getPosition().x - targetEntity.getBounds().getWidth() - 0), targetEntity.getPosition().y));
                    }
                } else {
                    if(sumY > -sumX) {
                        targetEntity.setPosition(new Vector2(otherEntity.getPosition().x + otherEntity.getBounds().getWidth() + 0, targetEntity.getPosition().y));
                    } else {
                        targetEntity.setPosition(new Vector2(targetEntity.getPosition().x, otherEntity.getPosition().y - targetEntity.getBounds().getHeight() - 0));
                }
            }
                            
            targetEntity.setVelocity(targetEntity.getVelocity().normalize());
        }
    };

    private Player makePlayer() {
        return new Player() {
            {
                setPosition(new Vector2(50.f, 50.f));
                setMaxVelocity(300.f);
                setAcceleration(100.f);
                setCollidable(true);
                setBounds(new Rectangle(64, 64));
            }
        };
    }

    // Skal muligvis flyttes til en common util pakke?
    private int booleanToInt(boolean b) {
        return (b) ? 1 : 0;
    }

    @Override
    public void render(Graphics g, World world) {
        Player playerEntity = (Player) world.getEntityByID(playerID);

        g.drawSprite(
                /* Position    */playerEntity.getPosition(),
                /* Size        */ new Vector2(playerEntity.getBounds().getWidth(), playerEntity.getBounds().getHeight()),
                /* InputStream */ texture.getInputStream(),
                /* Rotation    */ playerEntity.getRotation()
        );
        
        g.drawSprite(
                /* Position    */playerEntity.getPosition(),
                /* Size        */ new Vector2(playerEntity.getBounds().getWidth(), playerEntity.getBounds().getHeight()),
                /* InputStream */ playerEntity.getAnimator().getTexture(),
                /* Rotation    */ (float) Math.toDegrees(Math.atan2(playerEntity.getVelocity().y, playerEntity.getVelocity().x))
        );
        
        g.drawString(new Vector2(50, 670), String.format("Score : %d", playerEntity.getScore()));
    }
}
