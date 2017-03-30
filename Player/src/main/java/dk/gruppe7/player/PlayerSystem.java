package dk.gruppe7.player;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.Input;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.KeyEventHandler;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IProcess.class)

/**
 *
 * @author Mikkel
 */
public class PlayerSystem implements IProcess, IRender {

    Input input;
    boolean north, south, west, east;
    Vector2 aimDirection = Vector2.zero;
    
    InputStream texture = getClass().getResourceAsStream("player.png");

    UUID playerID;
    List<ShootingEvent> events = ShootingData.getEvents();

    KeyEventHandler wKeyEventHandler = (newKeyState) -> { north = newKeyState; };
    KeyEventHandler aKeyEventHandler = (newKeyState) -> { west = newKeyState; };
    KeyEventHandler sKeyEventHandler = (newKeyState) -> { south = newKeyState; };
    KeyEventHandler dKeyEventHandler = (newKeyState) -> { east = newKeyState; };
    
    KeyEventHandler ArrowUpKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.up;
            } else if(aimDirection.equals(Vector2.up)) {
                aimDirection = Vector2.zero;
            }
        }
    };
    
    KeyEventHandler ArrowLeftKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.left;
            } else if(aimDirection.equals(Vector2.left)) {
                aimDirection = Vector2.zero;
            }
        }
    };
        
    KeyEventHandler ArrowDownKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.down;
            } else if(aimDirection.equals(Vector2.down)) {
                aimDirection = Vector2.zero;
            }
        }
    };
            
    KeyEventHandler ArrowRightKeyEventHandler = new KeyEventHandler() {
        @Override
        public void call(boolean newKeyState) {
            if (newKeyState == true) {
                aimDirection = Vector2.right;
            } else if(aimDirection.equals(Vector2.right)) {
                aimDirection = Vector2.zero;
            }
        }
    };

    @Override
    public void start(GameData gameData, World world) {
        Entity temp = makePlayer();
        playerID = temp.getId();
        world.addEntity(temp);

        input = gameData.getInput();
        input.registerKeyEventHandler(KeyEvent.VK_W, wKeyEventHandler);
        input.registerKeyEventHandler(KeyEvent.VK_A, aKeyEventHandler);
        input.registerKeyEventHandler(KeyEvent.VK_S, sKeyEventHandler);
        input.registerKeyEventHandler(KeyEvent.VK_D, dKeyEventHandler);

        input.registerKeyEventHandler(KeyEvent.VK_UP, ArrowUpKeyEventHandler);
        input.registerKeyEventHandler(KeyEvent.VK_LEFT, ArrowLeftKeyEventHandler);
        input.registerKeyEventHandler(KeyEvent.VK_DOWN, ArrowDownKeyEventHandler);
        input.registerKeyEventHandler(KeyEvent.VK_RIGHT, ArrowRightKeyEventHandler);
    }

    @Override
    public void stop(GameData gameData, World world) {
        input.unregisterKeyEventHandler(wKeyEventHandler, aKeyEventHandler, sKeyEventHandler, dKeyEventHandler);
        input.unregisterKeyEventHandler(ArrowUpKeyEventHandler, ArrowLeftKeyEventHandler, ArrowDownKeyEventHandler, ArrowRightKeyEventHandler);
        world.removeEntity(world.getEntityByID(playerID));
        playerID = null;
    }

    @Override
    public void process(GameData gameData, World world) {
        Entity playerEntity = world.getEntityByID(playerID);
        
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

        if(!aimDirection.equals(Vector2.zero)) {
            events.add(new ShootingEvent(new Bullet(){{
                setBulletType(ShootingType.PROJECTILE);
                setAcceleration(1.f);
                setVelocity(getVelocity().add(aimDirection).mul(666.f));
                setPosition(getPosition().add(playerEntity.getPosition()));
            }}));
        }
    }

    private Entity makePlayer() {
        return new Entity() {{
                setPosition(new Vector2(50.f, 50.f));
                setMaxVelocity(300.f);
                setAcceleration(100.f);
                setCollidable(true);
                setBounds(new Rectangle(64, 64));
        }};
    }

    // Skal muligvis flyttes til en common util pakke?
    private int booleanToInt(boolean b) {
        return (b) ? 1 : 0;
    }

    @Override
    public void render(Graphics g, World world) {
        Entity playerEntity = world.getEntityByID(playerID);
        
        g.drawSprite(
                /* Position    */ playerEntity.getPosition(), 
                /* Size        */ new Vector2(playerEntity.getBounds().getWidth(), playerEntity.getBounds().getHeight()), 
                /* InputStream */ texture, 
                /* Rotation    */ (float) Math.toDegrees(Math.atan2(playerEntity.getVelocity().y, playerEntity.getVelocity().x))
        );
    }
}
