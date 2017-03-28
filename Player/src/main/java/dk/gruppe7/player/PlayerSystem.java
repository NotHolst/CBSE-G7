package dk.gruppe7.player;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.Input;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IProcess.class)

/**
 *
 * @author Mikkel
 */
public class PlayerSystem implements IProcess {

    Input input;
    boolean north, south, west, east;
    Vector2 aimDirection = Vector2.zero;

    UUID playerID;
    List<ShootingEvent> events = ShootingData.getEvents();

    Callable wKeyEvent = () -> { return north = !north; };
    Callable aKeyEvent = () -> { return west = !west; };
    Callable sKeyEvent = () -> { return south = !south; };
    Callable dKeyEvent = () -> { return east = !east; };

    Callable upKeyEvent = new Callable() {
        long i = 0;

        @Override
        public Object call() throws Exception {
            if (i++ % 2 == 0) {
                aimDirection = Vector2.up; 
            } else if(aimDirection.equals(Vector2.up)) {
                aimDirection = Vector2.zero; 
            }
            
            return null;
        }
    };
    Callable leftKeyEvent = new Callable() {
        long i = 0;

        @Override
        public Object call() throws Exception {
            if (i++ % 2 == 0) {
                aimDirection = Vector2.left;
            } else if(aimDirection.equals(Vector2.left)) {
                aimDirection = Vector2.zero;
            }
            
            return null;
        }
    };
    Callable downKeyEvent = new Callable() {
        long i = 0;

        @Override
        public Object call() throws Exception {
            if (i++ % 2 == 0) {
                aimDirection = Vector2.down;
            } else if (aimDirection.equals(Vector2.down)) {
                aimDirection = Vector2.zero;
            }
            
            return null;
        }
    };
    Callable rightKeyEvent = new Callable() {
        long i = 0;

        @Override
        public Object call() throws Exception {
            if (i++ % 2 == 0) {
                aimDirection = Vector2.right;
            } else if(aimDirection.equals(Vector2.right)) {
                aimDirection = Vector2.zero;
            }
            
            return null;
        }
    };

    @Override
    public void start(GameData gameData, World world) {
        Entity temp = makePlayer();
        playerID = temp.getId();
        world.addEntity(temp);

        input = gameData.getInput();
        input.registerKeyEvent(KeyEvent.VK_W, wKeyEvent);
        input.registerKeyEvent(KeyEvent.VK_A, aKeyEvent);
        input.registerKeyEvent(KeyEvent.VK_S, sKeyEvent);
        input.registerKeyEvent(KeyEvent.VK_D, dKeyEvent);

        input.registerKeyEvent(KeyEvent.VK_UP, upKeyEvent);
        input.registerKeyEvent(KeyEvent.VK_LEFT, leftKeyEvent);
        input.registerKeyEvent(KeyEvent.VK_DOWN, downKeyEvent);
        input.registerKeyEvent(KeyEvent.VK_RIGHT, rightKeyEvent);
    }

    @Override
    public void stop(GameData gameData, World world) {
        input.unregisterKeyEvent(wKeyEvent, aKeyEvent, sKeyEvent, dKeyEvent);
        input.unregisterKeyEvent(upKeyEvent, leftKeyEvent, downKeyEvent, rightKeyEvent);
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
                .clamp(
                    -playerEntity.getMaxVelocity(), 
                    playerEntity.getMaxVelocity())
                .mul(.9f)
        ); 
        
        playerEntity.setPosition(playerEntity.getPosition()
                .add(playerEntity.getVelocity().mul(gameData.getDeltaTime())
        ));

        if(!aimDirection.equals(Vector2.zero)) {
            Bullet b = new Bullet();
            b.setBulletType(ShootingType.PROJECTILE);
            b.setAcceleration(1.f);
            b.setVelocity(b.getVelocity().add(aimDirection).mul(666.f));
            b.setPosition(b.getPosition().add(playerEntity.getPosition()));
            events.add(new ShootingEvent(b));
            
            b.setBulletType(ShootingType.PROJECTILE);
        }
    }

    private Entity makePlayer() {
        return new Entity() {{
                setPosition(new Vector2(50.f, 50.f));
                setMaxVelocity(300.f);
                setAcceleration(100.f);
                setCollidable(true);
        }};
    }

    // Skal muligvis flyttes til en common util pakke?
    private int booleanToInt(boolean b) {
        return (b) ? 1 : 0;
    }
}
