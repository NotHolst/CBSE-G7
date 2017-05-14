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
import dk.gruppe7.damagecommon.DamageEvent;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingEvent;
import dk.gruppe7.weaponcommon.Weapon;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;


/**
 *
 * @author Mathies H
 */

@ServiceProviders(value ={
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})
 
public class BulletSystem implements IProcess, IRender
{
    List<Bullet> listOfBulletsToRemove = new ArrayList<>();
    
    Image textureCrossbowBolt;
    
    Image shadow;

    @Override
    public void start(GameData gameData, World world)
    {
        textureCrossbowBolt = gameData.getResourceManager().addImage("bolt", getClass().getResourceAsStream("CrossbowBolt.png"));
        shadow = gameData.getResourceManager().addImage("shadow", getClass().getResourceAsStream("Shadow.png"));

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
        if(world.isEntityOfClass(event.getOtherID(), Bullet.class) && !world.isEntityOfClass(event.getTargetID(), Bullet.class) && !world.isEntityOfClass(event.getTargetID(), Weapon.class))
        {
            Bullet bullet = world.getEntityByID(event.getOtherID());
            if(!bullet.getOwner().equals(event.getTargetID()) && !world.isEntityOfClass(event.getTargetID(), Weapon.class))
            {
                listOfBulletsToRemove.add(world.getEntityByID(event.getOtherID()));
                Dispatcher.post(new DamageEvent(bullet.getDamageData(), event.getTargetID()), world);
            }
        }     
    };
    
    ActionEventHandler<ShootingEvent> shootingHandler = (event, world) -> {
        world.addEntity(event.getBlueprint());
    };

    @Override
    public void render(Graphics g, World world) {
        for(Bullet bullet : world.<Bullet>getEntitiesByClass(Bullet.class)) {
            g.drawSprite(bullet.getPosition(), new Vector2(16,16), shadow.getInputStream(), bullet.getRotation(), 0, bullet.getPositionCentered().y);
            g.drawSprite(bullet.getPosition().add(0,30), new Vector2(16,16), bullet.getTexture().getInputStream(), bullet.getRotation(), 0, bullet.getPositionCentered().y);
        }
    }
}