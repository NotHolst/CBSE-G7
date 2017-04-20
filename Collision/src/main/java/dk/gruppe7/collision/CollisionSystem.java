/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.collision;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import java.util.List;
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
        List<Entity> list = world.getEntities();
        for(int i = 0; i < list.size(); i++) {
            for(int j = i + 1; j < list.size(); j++) {
                if(list.get(i).isCollidable() && list.get(j).isCollidable() && intersects(list.get(i), list.get(j))) {
                    Dispatcher.post(new CollisionEvent(list.get(i).getId(), list.get(j).getId()), world);
                    Dispatcher.post(new CollisionEvent(list.get(j).getId(), list.get(i).getId()), world);
                }
            }
        }
    }
    
    private boolean intersects(Entity targetEntity, Entity otherEntity) {
        if(targetEntity.getBounds() == null || otherEntity.getBounds() == null) return false;
        
        if(targetEntity.getBounds().isEmpty() || otherEntity.getBounds().isEmpty()) return false;
        
        Rectangle targetRect = targetEntity.getBounds().add((int) targetEntity.getPosition().x, (int) targetEntity.getPosition().y);
        Rectangle otherRect = otherEntity.getBounds().add((int) otherEntity.getPosition().x, (int) otherEntity.getPosition().y);
        
        return ((otherRect.getWidth() < otherEntity.getPosition().x || otherRect.getWidth() > targetEntity.getPosition().x) &&
                (otherRect.getHeight() < otherEntity.getPosition().y || otherRect.getHeight() > targetEntity.getPosition().y) &&
                (targetRect.getWidth() < targetEntity.getPosition().x || targetRect.getWidth() > otherEntity.getPosition().x) &&
                (targetRect.getHeight() < targetEntity.getPosition().y || targetRect.getHeight() > otherEntity.getPosition().y));
    }
}