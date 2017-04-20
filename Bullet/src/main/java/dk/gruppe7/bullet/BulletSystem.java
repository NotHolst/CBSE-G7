/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.bullet;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.eventtypes.DisposeEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;


/**
 *
 * @author Mathies H
 */
@ServiceProvider (service = IProcess.class) 
 
public class BulletSystem implements IProcess, IRender
{
    List<Bullet> listOfBulletsToRemove = new ArrayList<>();
    
    Image textureCrossbowBolt;

    @Override
    public void start(GameData gameData, World world)
    {
        textureCrossbowBolt = gameData.getResourceManager().addImage("bolt", getClass().getResourceAsStream("CrossbowBolt.png"));

        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world)
    {
        Dispatcher.unsubscribe(this);
        world.removeEntities(world.<Bullet>getEntitiesByClass(Bullet.class));
    }

    @Override
    public void process(GameData gameData, World world)
    {
        for(Bullet bullet : world.<Bullet>getEntitiesByClass(Bullet.class)) {
            bullet.setDespawnTimer(bullet.getDespawnTimer() - gameData.getDeltaTime()); 
            bullet.setPosition(bullet.getPosition().add(bullet.getVelocity().mul(gameData.getDeltaTime())));
            
            if(0 > bullet.getDespawnTimer()) {  
                listOfBulletsToRemove.add(bullet);
            }
        } 
    }
    
    ActionEventHandler<DisposeEvent> disposalHandler = (event, world) -> {
        world.removeEntities(listOfBulletsToRemove);
        listOfBulletsToRemove.clear();
    };
    
    ActionEventHandler<CollisionEvent> collisionHandler = (event, world) -> {
        if(world.isEntityOfClass(event.getOtherID(), Bullet.class) && !world.isEntityOfClass(event.getTargetID(), Bullet.class)) 
            if(!world.<Bullet>getEntityByID(event.getOtherID()).getOwner().equals(event.getTargetID()))
                listOfBulletsToRemove.add(world.getEntityByID(event.getOtherID()));
    };
    
    ActionEventHandler<ShootingEvent> shootingHandler = (event, world) -> {
        world.addEntity(event.getBlueprint());
    };

    @Override
    public void render(Graphics g, World world) {
        for(Bullet bullet : world.<Bullet>getEntitiesByClass(Bullet.class)) {
            g.drawSprite(bullet.getPosition(), new Vector2(17,3), textureCrossbowBolt.getInputStream(), bullet.getRotation(), 5);
        }
    }
}