package dk.gruppe7.player;

import dk.gruppe7.bosscommon.Boss;
import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.eventtypes.KeyReleasedEvent;
import dk.gruppe7.common.World;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Animation;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.mobcommon.MobEvent;
import dk.gruppe7.mobcommon.MobEventType;
import dk.gruppe7.obstaclecommon.Obstacle;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.weaponcommon.WeaponEvent;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.common.utils.ConverterUtil;
import dk.gruppe7.damagecommon.DamageEvent;
import dk.gruppe7.powerupcommon.PowerupEvent;

@ServiceProvider(service = IProcess.class)

/**
 *
 * @author Mikkel
 */
public class PlayerSystem implements IProcess, IRender {
    boolean north, south, west, east;
    Vector2 aimDirection = Vector2.zero;

    Image health;
    
    Animation walk_up, walk_down, walk_left, walk_right;

    UUID playerID;

    KeyEventHandler<KeyPressedEvent> wKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_W) {
        @Override
        public void call(KeyPressedEvent event) {
            north = event.getState();
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> wKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_W) {
        @Override
        public void call(KeyReleasedEvent event) {
            north = event.getState();
        }
    };
    
    KeyEventHandler<KeyPressedEvent> aKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_A) {
        @Override
        public void call(KeyPressedEvent event) {
            west = event.getState();
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> aKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_A) {
        @Override
        public void call(KeyReleasedEvent event) {
            west = event.getState();
        }
    };
    
    KeyEventHandler<KeyPressedEvent> sKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_S) {
        @Override
        public void call(KeyPressedEvent event) {
            south = event.getState();
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> sKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_S) {
        @Override
        public void call(KeyReleasedEvent event) {
            south = event.getState();
        }
    };
    
    KeyEventHandler<KeyPressedEvent> dKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_D) {
        @Override
        public void call(KeyPressedEvent event) {
            east = event.getState();
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> dKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_D) {
        @Override
        public void call(KeyReleasedEvent event) {
            east = event.getState();
        }
    };
    
    KeyEventHandler<KeyPressedEvent> arrowUpKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_UP) {
        @Override
        public void call(KeyPressedEvent event) {
            aimDirection = Vector2.up;
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> arrowUpKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_UP) {
        @Override
        public void call(KeyReleasedEvent event) {
            if(aimDirection.equals(Vector2.up)) {
                aimDirection = Vector2.zero;
            }
        }
    };
    
    KeyEventHandler<KeyPressedEvent> arrowLeftKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_LEFT) {
        @Override
        public void call(KeyPressedEvent event) {
            aimDirection = Vector2.left;
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> arrowLeftKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_LEFT) {
        @Override
        public void call(KeyReleasedEvent event) {
            if(aimDirection.equals(Vector2.left)) {
                aimDirection = Vector2.zero;
            }
        }
    };
    
    KeyEventHandler<KeyPressedEvent> arrowDownKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_DOWN) {
        @Override
        public void call(KeyPressedEvent event) {
            aimDirection = Vector2.down;
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> arrowDownKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_DOWN) {
        @Override
        public void call(KeyReleasedEvent event) {
            if(aimDirection.equals(Vector2.down)) {
                aimDirection = Vector2.zero;
            }
        }
    };
    
    KeyEventHandler<KeyPressedEvent> arrowRightKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_RIGHT) {
        @Override
        public void call(KeyPressedEvent event) {
            aimDirection = Vector2.right;
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> arrowRightKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_RIGHT) {
        @Override
        public void call(KeyReleasedEvent event) {
            if(aimDirection.equals(Vector2.right)) {
                aimDirection = Vector2.zero;
            }
        }
    };

    @Override
    public void start(GameData gameData, World world) {
        
        walk_up = new Animation(new Image[]{
            gameData.getResourceManager().addImage("walk_up1", getClass().getResourceAsStream("U1.png")),
            gameData.getResourceManager().addImage("walk_up2", getClass().getResourceAsStream("U2.png")),
            gameData.getResourceManager().addImage("walk_up3", getClass().getResourceAsStream("U3.png")),
            gameData.getResourceManager().addImage("walk_up4", getClass().getResourceAsStream("U4.png")),
        }, .1f);
        walk_down = new Animation(new Image[]{
            gameData.getResourceManager().addImage("walk_down1", getClass().getResourceAsStream("D1.png")),
            gameData.getResourceManager().addImage("walk_down2", getClass().getResourceAsStream("D2.png")),
            gameData.getResourceManager().addImage("walk_down3", getClass().getResourceAsStream("D3.png")),
            gameData.getResourceManager().addImage("walk_down4", getClass().getResourceAsStream("D4.png")),
        }, .1f);
        walk_left = new Animation(new Image[]{
            gameData.getResourceManager().addImage("walk_left1", getClass().getResourceAsStream("L1.png")),
            gameData.getResourceManager().addImage("walk_left2", getClass().getResourceAsStream("L2.png")),
            gameData.getResourceManager().addImage("walk_left3", getClass().getResourceAsStream("L3.png")),
            gameData.getResourceManager().addImage("walk_left4", getClass().getResourceAsStream("L4.png")),
        }, .1f);
        walk_right = new Animation(new Image[]{
            gameData.getResourceManager().addImage("walk_right1", getClass().getResourceAsStream("R1.png")),
            gameData.getResourceManager().addImage("walk_right2", getClass().getResourceAsStream("R2.png")),
            gameData.getResourceManager().addImage("walk_right3", getClass().getResourceAsStream("R3.png")),
            gameData.getResourceManager().addImage("walk_right4", getClass().getResourceAsStream("R4.png")),
        }, .1f);
        
        health = gameData.getResourceManager().addImage("healthBar", getClass().getResourceAsStream("healthGreen.png"));
        
        Player temp = makePlayer();
        playerID = temp.getId();
        temp.getAnimator().play(walk_down);
        temp.getAnimator().update(gameData);

        world.addEntity(temp);

        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);

        world.removeEntity(world.getEntityByID(playerID));
        playerID = null;
    }

    @Override
    public void process(GameData gameData, World world) {
        Player playerEntity = (Player) world.getEntityByID(playerID);

        if (playerEntity != null && playerEntity.getHealthData().getHealth() > 0) {
            playerEntity.setPosition(playerEntity.getPosition()
                .add(playerEntity.getVelocity()
                        .mul(gameData.getDeltaTime())
                ));
            
            playerEntity.setPosition(playerEntity.getPosition().clampX(25, gameData.getScreenWidth() - (playerEntity.getBounds().getWidth() + 25)));
            playerEntity.setPosition(playerEntity.getPosition().clampY(25, gameData.getScreenHeight() - (playerEntity.getBounds().getHeight() + 25)));
                    
            playerEntity.setVelocity(playerEntity.getVelocity()
                    .add(playerEntity.getAcceleration() * (ConverterUtil.BooleanToInt(east) - ConverterUtil.BooleanToInt(west)),
                            playerEntity.getAcceleration() * (ConverterUtil.BooleanToInt(north) - ConverterUtil.BooleanToInt(south)))
                    .clampLength(
                            playerEntity.getMaxVelocity()
                    )
                    .mul(.9f)
            );
            
            

            if (!aimDirection.equals(Vector2.zero)) {
                playerEntity.setRotation((float) Math.toDegrees(Math.atan2(aimDirection.y, aimDirection.x)));
                Dispatcher.post(new WeaponEvent(playerID), world);
                if(aimDirection == Vector2.up) playerEntity.getAnimator().play(walk_up);
                else if(aimDirection == Vector2.down) playerEntity.getAnimator().play(walk_down);
                else if(aimDirection == Vector2.left) playerEntity.getAnimator().play(walk_left);
                else if(aimDirection == Vector2.right) playerEntity.getAnimator().play(walk_right);
            } else if (playerEntity.getVelocity().len() > 50f) {
                playerEntity.setRotation((float) Math.toDegrees(Math.atan2(playerEntity.getVelocity().y, playerEntity.getVelocity().x)));
                if(north) playerEntity.getAnimator().play(walk_up);
                else if(south) playerEntity.getAnimator().play(walk_down);
                else if(west) playerEntity.getAnimator().play(walk_left);
                else if(east) playerEntity.getAnimator().play(walk_right);
            }

            if(playerEntity.getVelocity().len() > .1f){
                playerEntity.getAnimator().setInterval(15*1.0f/playerEntity.getVelocity().len());
                playerEntity.getAnimator().update(gameData);
            }
        }
    }
    
    ActionEventHandler<MobEvent> mobEventHandler = (event, world) -> {
        if (event.getType() == MobEventType.DEATH) {
            ((Player)world.getEntityByID(playerID)).incrementScoreBy(1);
        }
    };
    
    ActionEventHandler<DamageEvent> damageHandler = (event, world) -> {
        if(event.getTarget().equals(playerID))
            world.<Player>getEntityByID(playerID).getHealthData().decreaseHealth(event.getDamageDealt().getDamage());
    };
    
    ActionEventHandler<CollisionEvent> obstacleCollisionHandler = (event, world) -> {
        if(( Obstacle.class.isInstance(world.getEntityByID(event.getOtherID())) ) && event.getTargetID().equals(playerID)){ 
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
                            
            targetEntity.setVelocity(Vector2.zero);
        }
    };
    
    ActionEventHandler<PowerupEvent> powerupHandler = (event, world) -> {
        Player player = (Player) world.getEntityByID(event.getTarget());
        player.multiplyMaxVelocity(event.getPowerupData().getBoostMaxVelocity());
    };

    private Player makePlayer() {
        return new Player() {
            {
                setPosition(new Vector2(50.f, 50.f));
                setMaxVelocity(300.f);
                setAcceleration(100.f);
                setCollidable(true);
                setBounds(new Rectangle(48, 38));
                setRoomPersistent(true);
            }
        };
    }

    @Override
    public void render(Graphics g, World world) {
        Player playerEntity = (Player) world.getEntityByID(playerID);
        
        g.drawSprite(
                /* Position    */playerEntity.getPosition(),
                /* Size        */ new Vector2(playerEntity.getBounds().getWidth(), playerEntity.getBounds().getWidth()*1.7f),
                /* InputStream */ playerEntity.getAnimator().getTexture(),
                /* Rotation    */ 0,
                /* LayerHeight */ 1,
                playerEntity.getPositionCentered().y
        );
        
        g.drawString(new Vector2(50, 670), String.format("Score : %d", playerEntity.getScore()));
        g.drawSprite(playerEntity.getPosition().add(0, -2), new Vector2(64 * playerEntity.getHealthData().getHealth() / playerEntity.getHealthData().getStartHealth(), 5), health.getInputStream(), 0, 3);

    }
}
