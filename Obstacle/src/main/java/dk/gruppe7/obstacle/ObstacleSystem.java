/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.obstacle;

import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.utils.RandomUtil;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import dk.gruppe7.obstaclecommon.Obstacle;
import org.openide.util.lookup.ServiceProvider;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author pc4
 */
@ServiceProviders(value ={
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})

public class ObstacleSystem implements IProcess, IRender {
    private InputStream boundsTexture = getClass().getResourceAsStream("roomBound.png");
    private InputStream wallTexture = getClass().getResourceAsStream("obstacle.png");
    private ArrayList<UUID> listOfBoundsIDs = new ArrayList<>();

    private final int boundsWidth = 25;
    private int frameWidth, frameHeight;

    @Override
    public void start(GameData gameData, World world) {
        for (Entity boundEntity : createRoomBounds(gameData)) {
            world.addEntity(boundEntity);
        }
        
        frameWidth = gameData.getScreenWidth();
        frameHeight = gameData.getScreenHeight();
        
        Dispatcher.subscribe(this);
    }

    private Obstacle[] createRoomBounds(GameData gameData) {
        Obstacle[] bounds = new Obstacle[4];
        bounds[0] = new Obstacle() {
            {
                setCollidable(true);
                setPosition(new Vector2(0, 0));
                setBounds(new Rectangle(boundsWidth, gameData.getScreenHeight()));
            }
        };
        bounds[1] = new Obstacle() {
            {
                setCollidable(true);
                setPosition(new Vector2(boundsWidth, 0));
                setBounds(new Rectangle(gameData.getScreenWidth() - 2 * boundsWidth, boundsWidth));
            }
        };
        bounds[2] = new Obstacle() {
            {
                setCollidable(true);
                setPosition(new Vector2(gameData.getScreenWidth() - boundsWidth, 0));
                setBounds(new Rectangle(boundsWidth, gameData.getScreenHeight()));
            }
        };
        bounds[3] = new Obstacle() {
            {
                setCollidable(true);
                setPosition(new Vector2(boundsWidth, gameData.getScreenHeight() - boundsWidth - 70));
                setBounds(new Rectangle(gameData.getScreenWidth() - 2 * boundsWidth, boundsWidth+100));
            }
        };

        for (int i = 0; i < 4; i++) {
            listOfBoundsIDs.add(bounds[i].getId());
        }

        return bounds;
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
        world.removeEntities(world.<Obstacle>getEntitiesByClass(Obstacle.class));
    }

    @Override
    public void process(GameData gameData, World world) {
        
    }

    @Override
    public void render(Graphics g, World world) {
        //for (UUID uuid : listOfBoundsIDs) {
        for(Obstacle obstacle : world.<Obstacle>getEntitiesByClass(Obstacle.class)) {
            //Entity bound = world.getEntityByID(uuid);
            InputStream tex = (obstacle.getBounds().getWidth() >= obstacle.getBounds().getHeight())?wallTexture:boundsTexture;
            
            g.drawRepeatedSprite(
                    /* Position    */obstacle.getPosition(),
                    /* Size        */ new Vector2(obstacle.getBounds().getWidth(), obstacle.getBounds().getHeight()),
                    /* InputStream */ tex,
                    /* Rotation    */ obstacle.getRotation(),
                    /* zIndex      */ -5000
            );
        }
    }
    
    ActionEventHandler<RoomChangedEvent> collisionHandler = new ActionEventHandler<RoomChangedEvent>() {
        @Override
        public void call(RoomChangedEvent event, World world) {
            if(event.getRoom().isCleared()) {
                return;
            }
            
            int subWidth = frameWidth - ((64 * 4) + 25);
            int subHeight = frameHeight - ((64 * 4) + 25);
            
            List<Obstacle> listOfObstacles = new ArrayList<>();

            int numberOfPossiblePositionsX = (int)Math.floor(subWidth / 64);
            int numberOfPossiblePositionsY = (int)Math.floor(subHeight / 64);
            int numberOfPossiblePositions = (int)((numberOfPossiblePositionsX * numberOfPossiblePositionsY) * 0.1);
            int[][] arr = new int[numberOfPossiblePositionsX][numberOfPossiblePositionsY];

            if(numberOfPossiblePositions < 1) numberOfPossiblePositions = 1;
            int numberOfObstaclesToPlace = RandomUtil.GetRandomInteger(1, numberOfPossiblePositions);
            
            while(numberOfObstaclesToPlace > 0) {
                int x = RandomUtil.GetRandomInteger(0, numberOfPossiblePositionsX - 1);
                int y = RandomUtil.GetRandomInteger(0, numberOfPossiblePositionsY - 1);
                
                if(arr[x][y] == 0) {
                    arr[x][y] = 1;

                    Obstacle o = new Obstacle();
                    o.setPosition(new Vector2((64 * 2) + (x * 64), (64 * 2) + (y * 64)));
                    o.setBounds(new Rectangle(64, 64));
                    o.setCollidable(true);
                    o.setRoomPersistent(false);
                    
                    listOfObstacles.add(o);
                    listOfBoundsIDs.add(o.getId());
                    numberOfObstaclesToPlace--;
                }
            }
            
            listOfObstacles.forEach(obstacle -> System.out.println(obstacle.getPosition() + " | " + obstacle.getBounds()));
            
            world.addEntities(listOfObstacles);
        }
    };
}