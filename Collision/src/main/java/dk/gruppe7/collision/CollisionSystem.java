/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.collision;

import collision.CollisionData;
import collision.CollisionEvent;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.World;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author JOnes
 */

@ServiceProvider (service = IProcess.class) 
public class CollisionSystem implements IProcess {
    
    @Override
    public void start(GameData gameData, World world) {
        //System.out.println("CollisionSystem started");
    }

    @Override
    public void stop(GameData gameData, World world) {
        //System.out.println("CollisionSystem stopeed");
    }

    @Override
    public void process(GameData gameData, World world) {
        for(Entity target: world.getEntities()) {
            for(Entity other: world.getEntities()) {
                /*System.out.println(target.getPosition().x + " | " + target.getPosition().y + " | " 
                        + target.getBounds().getWidth() + " | " + target.getBounds().getHeight() + " | "
                        + target.isCollidable());
                */
                if(!target.equals(other) && target.isCollidable() && other.isCollidable() && intersects(target, other)) {
                    //System.out.println("Adding new CollisionEvent");
                    CollisionData.getEvents().add(new CollisionEvent(target.getId(), other.getId())); // CollisionEvent for the first
                    CollisionData.getEvents().add(new CollisionEvent(other.getId(), target.getId())); // CollisionEvent for the second
                }
            }
        }
    }
    
    private boolean intersects(Entity target, Entity other) {
        if(target.getBounds() == null || other.getBounds() == null) return false;
        float tw = target.getBounds().getWidth();
        float th = target.getBounds().getHeight();
        float rw = other.getBounds().getWidth();
        float rh = other.getBounds().getHeight();
        if(rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) return false; // you can't collide with Entity with a less than 0 bounds
        
        float tx = target.getPosition().x;
        float ty = target.getPosition().y;
        float rx = other.getPosition().x;
        float ry = other.getPosition().y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }
    
    
}
