package dk.gruppe7.common;

import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.data.Point;
import dk.gruppe7.common.data.Room;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Holst & Harald
 */
public class World {
    
    private int currentLevel;
    private Room currentRoom = null;
    private HashMap<Point, Point[]> map;
    private List<Entity> entities = new CopyOnWriteArrayList<>(); //Jan bruger hashmap med <entityID (string), Entity>. Det er nok smart ift. lookup af en bestemt entity
    
    public List<Entity> getEntities()
    {
        return entities;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
    }
    
    public void addEntities(Collection<? extends Entity> col) {
        entities.addAll(col);
    }
    
    public void removeEntity(Entity e)
    {
        entities.remove(e);
    }
    
    public void removeEntities(Collection<? extends Entity> col) {
        entities.removeAll(col);
    }
    
    public Entity getEntityByID(UUID entityID){
        for(Entity e : entities)
            if(e.getId().equals(entityID)) return e;
        return null;
    }
    
    public <T extends Entity> ArrayList<T> getEntitiesByClass(Class klass) {
        ArrayList<Entity> list = new ArrayList<>();
        
        for(Entity e : entities) {
            if(e.getClass().equals(klass)) {
                list.add(e);
                continue;
            }
            
            if(e.getClass().getSuperclass().equals(klass)) {
                list.add(e);
            }
        }
            
        return (ArrayList<T>)list;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}
