/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.bullet;

import collision.CollisionData;
import collision.CollisionEvent;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;


/**
 *
 * @author Mathies H
 */
@ServiceProvider (service = IProcess.class) 
 
public class BulletSystem implements IProcess, IRender
{
    List<ShootingEvent> events = ShootingData.getEvents();
    //List<UUID> bullets = new ArrayList<>();
    //HashMap<UUID, ShootingType> bulletTypes = new HashMap<>();
    
    Image textureCrossbowBolt;
    
    @Override
    public void start(GameData gameData, World world)
    {
        
        textureCrossbowBolt = gameData.getResourceManager().addImage("bolt", getClass().getResourceAsStream("CrossbowBolt.png"));
/*
        //Bullet for testing.
        BulletBluePrint bluePrint = new BulletBluePrint();
        bluePrint.setBulletType(BulletType.BULLET);
        bluePrint.setPosition(new Vector2(55, 55));
        bluePrint.setVelocity(new Vector2(10, 10));
        bluePrint.setAccerleration(0.5f);
        events.add(new ShootingEvent(bluePrint));
*/
        
    }

    @Override
    public void stop(GameData gameData, World world)
    {
        for (ListIterator<Entity> e = world.getEntities().listIterator(); e.hasNext();)
        {
            Entity entity = e.next();
            if (entity instanceof Bullet) e.remove();
        }
    }

    @Override
    public void process(GameData gameData, World world)
    {
        while(events.size() > 0)
        {
            makeBullet(events.get(0).getBlueprint(), world);
            events.remove(0);
        }
        
        for (ListIterator<Entity> e = world.getEntities().listIterator(); e.hasNext();)
        {
            Entity entity = e.next();
            
                    if (!(entity instanceof Bullet)) continue;
                    Bullet bullet = (Bullet) entity;
                    bullet.setDespawnTimer(bullet.getDespawnTimer() - gameData.getDeltaTime());
                        
                    bullet.setPosition(bullet.getPosition().add(bullet.getVelocity().mul(gameData.getDeltaTime())));
                    //bullet.setVelocity(bullet.getVelocity().mul(bullet.getAcceleration())); 
                    if(0 > bullet.getDespawnTimer()) {                            
                            e.remove();  
                        }
                
            
        }
        
    }
    
    private void makeBullet(Bullet bullet, World world)
    {
        world.addEntity(bullet);        
    }

    @Override
    public void render(Graphics g, World world) {
        for (ListIterator<Entity> e = world.getEntities().listIterator(); e.hasNext();)
        {
            Entity entity = e.next();
            if(entity instanceof Bullet){
            Bullet bullet = (Bullet) entity;
            if (bullet.getDamageData().getDamage() != 0) //Need to be replaced, temporary fix to make it look like bullets dissapear
                 g.drawSprite(bullet.getPosition(), new Vector2(17,3), textureCrossbowBolt.getInputStream(), bullet.getRotation());
                 
            }
           
        }
    }
    
    
    
}
