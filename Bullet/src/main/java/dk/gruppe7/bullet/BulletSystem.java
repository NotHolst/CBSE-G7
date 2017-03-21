/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.bullet;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mathies H
 */
@ServiceProvider (service = IProcess.class) 
 
public class BulletSystem implements IProcess
{
    List<ShootingEvent> events = ShootingData.events;
    List<Entity> bullets = new ArrayList<>();
    
    @Override
    public void start(GameData gameData, World world)
    {
        
    }

    @Override
    public void stop(GameData gameData, World world)
    {
        for (Entity bullet : bullets)
        {
            world.removeEntity(bullet);
        }
    }

    @Override
    public void process(GameData gameData, World world)
    {
        while(events.size() > 0)
        {
            makeBullet(events.get(0).gunner, world);
            events.remove(0);
        }
        
        for (Entity bullet : bullets)
        {
            bullet.getPosition().add(1, 1);
        }
        
        System.out.println(bullets.size());
    }
    
    private void makeBullet(Entity gunner, World world)
    {
        Entity bullet = new Entity();
        bullet.setPosition(gunner.getPosition());
        bullet.setRotation(gunner.getRotation());
        bullet.setVelocity(new Vector2(10, 10));
        
        world.addEntity(bullet);
        bullets.add(bullet);
        
    }
    
    
    
}
