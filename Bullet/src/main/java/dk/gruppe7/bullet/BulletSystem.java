/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.bullet;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    List<UUID> bullets = new ArrayList<>();
    HashMap<UUID, ShootingType> bulletTypes = new HashMap<>();
    
    InputStream texture = getClass().getResourceAsStream("bullet.png");
    
    @Override
    public void start(GameData gameData, World world)
    {
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
        for (UUID bullet : bullets)
        {
            world.removeEntity(world.getEntityByID(bullet));
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
        
        for (UUID bulletId : bullets)
        {
            Entity bullet = world.getEntityByID(bulletId);
            switch(bulletTypes.get(bullet.getId())){
                default:
                    bullet.setPosition(bullet.getPosition().add(bullet.getVelocity().mul(gameData.getDeltaTime())));
                    bullet.setVelocity(bullet.getVelocity().mul(bullet.getAcceleration())); 
                break;
            }
        }
        
    }
    
    private void makeBullet(Bullet bullet, World world)
    {
        world.addEntity(bullet);
        bullets.add(bullet.getId());
        bulletTypes.put(bullet.getId(), bullet.getBulletType());
        
    }

    @Override
    public void render(Graphics g, World world) {
        for (UUID bulletId : bullets)
        {
            Entity bullet = world.getEntityByID(bulletId);
            g.drawSprite(bullet.getPosition(), new Vector2(32,32), texture, 0);
        }
    }
    
    
    
}
