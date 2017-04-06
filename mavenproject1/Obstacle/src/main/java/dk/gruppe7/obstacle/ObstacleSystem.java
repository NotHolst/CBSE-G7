/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.obstacle;

import collision.CollisionData;
import collision.CollisionEvent;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import org.openide.util.lookup.ServiceProvider;
import java.io.InputStream;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

/**
 *
 * @author pc4
 */
@ServiceProvider(service = IProcess.class)

public class ObstacleSystem implements IProcess, IRender
{

    InputStream boundsTexture = getClass().getResourceAsStream("roomBound.png");
    UUID[] boundsID = new UUID[4];
    
    int boundsWidth = 25;
    
    @Override
    public void start(GameData gameData, World world)
    {
        for (Entity boundEntity : createRoomBounds(gameData))
        {
            world.addEntity(boundEntity);
        }
    }
    
    private Entity[] createRoomBounds(GameData gameData)
    {
        Entity[] bounds = new Entity[4];
        bounds[0] = new Entity() {{
            setCollidable(true);
            setPosition(new Vector2(0, 0));
            setBounds(new Rectangle(boundsWidth, gameData.getScreenHeight()));
        }};
        bounds[1] = new Entity() {{
            setCollidable(true);
            setPosition(new Vector2(boundsWidth, 0));
            setBounds(new Rectangle(gameData.getScreenWidth()-2*boundsWidth, boundsWidth));
        }};
        bounds[2] = new Entity() {{
            setCollidable(true);
            setPosition(new Vector2(gameData.getScreenWidth()-boundsWidth, 0));
            setBounds(new Rectangle(boundsWidth, gameData.getScreenHeight()));
        }};
        bounds[3] = new Entity() {{
            setCollidable(true);
            setPosition(new Vector2(boundsWidth, gameData.getScreenHeight()-boundsWidth));
            setBounds(new Rectangle(gameData.getScreenWidth()-2*boundsWidth, boundsWidth));
        }};
        
        
        
        
        for (int i = 0; i < 4; i++)
            boundsID[i] = bounds[i].getId();
        
        
        
        
        return bounds;
    }

    @Override
    public void stop(GameData gameData, World world)
    {
        
    }

    @Override
    public void process(GameData gameData, World world)
    {
        System.out.println(CollisionData.getEvents(gameData.getTickCount()).size());
        
        for(ListIterator<CollisionEvent> iterator = CollisionData.getEvents(gameData.getTickCount()).listIterator(); iterator.hasNext();)
        {
            CollisionEvent tempi = iterator.next();
            
            for (UUID uuid : boundsID)
            {
                if(tempi.getOtherID().equals(uuid))
                {
                    Entity e = world.getEntityByID(tempi.getTargetID());
                    e.setPosition(new Vector2(150,150));                   
                    iterator.remove();
                }
                else if(tempi.getTargetID().equals(uuid))
                    iterator.remove();
            }
        }
    }

    @Override
    public void render(Graphics g, World world)
    {
        for (UUID uuid : boundsID)
        {
            Entity bound = world.getEntityByID(uuid);
            g.drawRepeatedSprite(
                /* Position    */ bound.getPosition(), 
                /* Size        */ new Vector2(bound.getBounds().getWidth(), bound.getBounds().getHeight()), 
                /* InputStream */ boundsTexture, 
                /* Rotation    */ bound.getRotation()
                                  
            );
        }
        
    }
    
}
