/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.obstacle;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.obstaclecommon.Obstacle;
import org.openide.util.lookup.ServiceProvider;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 *
 * @author pc4
 */
@ServiceProvider(service = IProcess.class)

public class ObstacleSystem implements IProcess, IRender {
    private InputStream boundsTexture = getClass().getResourceAsStream("roomBound.png");
    private ArrayList<UUID> listOfBoundsIDs = new ArrayList<>();

    private final int boundsWidth = 25;

    @Override
    public void start(GameData gameData, World world) {
        for (Entity boundEntity : createRoomBounds(gameData)) {
            world.addEntity(boundEntity);
        }
    }

    private Obstacle[] createRoomBounds(GameData gameData) {
        Obstacle[] bounds = new Obstacle[5];
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
                setPosition(new Vector2(boundsWidth, gameData.getScreenHeight() - boundsWidth));
                setBounds(new Rectangle(gameData.getScreenWidth() - 2 * boundsWidth, boundsWidth));
            }
        };
        
        // Temp Dummy Obstacle
        bounds[4] = new Obstacle() {
            {
                setCollidable(true);
                setPosition(new Vector2(608, 328));
                setBounds(new Rectangle(64, 64));
            }
        };

        for (int i = 0; i < 5; i++) {
            listOfBoundsIDs.add(bounds[i].getId());
        }

        return bounds;
    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    @Override
    public void process(GameData gameData, World world) {
        
    }

    @Override
    public void render(Graphics g, World world) {
        for (UUID uuid : listOfBoundsIDs) {
            Entity bound = world.getEntityByID(uuid);
            g.drawRepeatedSprite(
                    /* Position    */bound.getPosition(),
                    /* Size        */ new Vector2(bound.getBounds().getWidth(), bound.getBounds().getHeight()),
                    /* InputStream */ boundsTexture,
                    /* Rotation    */ bound.getRotation()
            );
        }
    }
}