/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mob;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.data.MobType;
import static dk.gruppe7.data.MobType.DEFENDER;
import static dk.gruppe7.data.MobType.MELEE;
import static dk.gruppe7.data.MobType.RANGED;
import static dk.gruppe7.data.MobType.SUPPORT;
import dk.gruppe7.mobcommon.Mob;
import dk.gruppe7.mobcommon.MobData;
import dk.gruppe7.mobcommon.MobEvent;
import static dk.gruppe7.mobcommon.MobEventType.SPAWN;
import dk.gruppe7.mobcommon.MobID;
import java.io.InputStream;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author benjaminmlynek
 */
@ServiceProvider(service = IProcess.class)
public class MobSystem implements IProcess, IRender {

    UUID mobID;
    
    InputStream texture = getClass().getResourceAsStream("mob.png");

    @Override
    public void start(GameData gameData, World world) {
        Entity mob;
        // Add mobs to the world
        for(int i = 0; i < GetRandomNumberBetween(2, 7); i++) {
            mob = createMob(
                    GetRandomNumberBetween(0, gameData.getScreenWidth()),
                    GetRandomNumberBetween(0, gameData.getScreenHeight()),
                    pickRandomMobType(MobType.class));
            world.addEntity(mob);
            MobData.getEvents().add(new MobEvent((Mob) mob, SPAWN));
        }
                
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove mobs from world.
        world.removeEntity(world.getEntityByID(mobID));
        mobID = null;

    }

    @Override
    public void process(GameData gameData, World world) {
        // Need som help with this one. Cant seem to get other than the first mobID.
        Collection<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            if(entity instanceof Mob) {
                Mob m = (Mob) entity;
                
                if(m.getWanderTimer() <= 0){
                    m.setVelocity(new Vector2(GetRandomNumberBetween(-10, 10), GetRandomNumberBetween(-10, 10)));
                    m.setWanderTimer(GetRandomNumberBetween(1, 4));
                }else{
                    m.setWanderTimer(m.getWanderTimer() - gameData.getDeltaTime());
                }
                
                m.setPosition(m.getPosition().add(m.getVelocity().mul(gameData.getDeltaTime())));
            }
        }
           
    }
    
    private Entity createMob(float x, float y, MobType type) {
        Mob mob = new Mob();
        MobID.setMobID(mobID = mob.getId());
        mob.setPosition(new Vector2(x, y));
        mob.setMobType(type);
        mob.setMaxVelocity(300.f);
        mob.setAcceleration(80.f);
        mob.setCollidable(true);
        mob.setBounds(new Rectangle(64, 64));

        // Create different types of mobs with different behavior
        if (type == SUPPORT) {
            // TO DO
            System.out.println("Support Mob " + mobID);
            mob.setMaxVelocity(250.f);
            mob.setAcceleration(90);
        }
        
        if (type == DEFENDER) {
            // TO DO
            System.out.println("Defender Mob " + mobID);
            mob.setMaxVelocity(200.f);
            mob.setAcceleration(70);
        }
        
        if (type == RANGED) {
            // TO DO
            System.out.println("Ranged Mob " + mobID);
            mob.setMaxVelocity(150.f);
            mob.setAcceleration(50);
        }
        
        if (type == MELEE) {
            // TO DO
            System.out.println("Melee Mob " + mobID);
            mob.setMaxVelocity(100.f);
            mob.setAcceleration(30);
        }
        
        return mob;
    }
    
    private int GetRandomNumberBetween(int start, int end) {
        Random r = new Random();
        return start + r.nextInt(end - start +1);
    }
    
    private float GetRandomNumber(float range) {
        Random r = new Random();
        return r.nextFloat() * range;
    }
    
    private static <T extends Enum<?>> T pickRandomMobType(Class<T> mobType) {
        Random r = new Random();
        int pick = r.nextInt(mobType.getEnumConstants().length);
        return mobType.getEnumConstants()[pick];
    }

    @Override
    public void render(Graphics g, World world) {
        for (Entity e : world.getEntities()){
            if(e instanceof Mob)
                g.drawSprite(e.getPosition(), new Vector2(64, 64), texture, 0);
        }
    }
    
    
}
