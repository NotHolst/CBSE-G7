package dk.gruppe7.common;

import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.data.Room;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Holst & Harald
 */
public class World {
    
    private int currentLevel;
    private Room currentRoom = null;
    private List<Entity> entities = new CopyOnWriteArrayList<>(); //Jan bruger hashmap med <entityID (string), Entity>. Det er nok smart ift. lookup af en bestemt entity
    private List<Room> closedList = new ArrayList<>();
    
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
    
    public void removeEntitiesByClassRecursively(Class klass) {
        closedList.clear();
        
        List<Entity> removeList = new ArrayList<>();
        
        for(Iterator iterator =  entities.iterator(); iterator.hasNext();) {
            Entity entity = (Entity) iterator.next();
            
            if(isEntityOfClass(entity.getId(), klass)) {
                removeList.add(entity);
            }
        }
                
        entities.removeAll(removeList);
        
        removeRecursively(currentRoom, klass);
    }
    
    public void removeEntities(Collection<? extends Entity> col) {  
        entities.removeAll(col);
    }
    
    private void removeRecursively(Room room, Class klass) {
        closedList.add(room);
        
        List<Object> removeList = new ArrayList<>();
        
        for(Iterator iterator =  room.getEntities().iterator(); iterator.hasNext();) {
            try {
                removeList.add(klass.cast(iterator.next()));
            } catch (ClassCastException ex) { }
        }
        
        room.getEntities().removeAll(removeList);
        
        if(room.getNorth() != null && !closedList.contains(room.getNorth())) {
            removeRecursively(room.getNorth(), klass);
        }
        
        if(room.getEast() != null && !closedList.contains(room.getEast())) {
            removeRecursively(room.getEast(), klass);
        }
        
        if(room.getSouth() != null && !closedList.contains(room.getSouth())) {
            removeRecursively(room.getSouth(), klass);
        }
        
        if(room.getWest() != null && !closedList.contains(room.getWest())) {
            removeRecursively(room.getWest(), klass);
        }
    }
    
    public <T extends Entity> T getEntityByID(UUID entityID){
        for(Entity e : entities)
            if(e.getId().equals(entityID)) return (T) e;
        return null;
    }
    
    
    public <T extends Entity> ArrayList<T> getEntitiesByClass(Class entityClass) {
        ArrayList<Entity> list = new ArrayList<>();
        
        for(Entity e : entities) {
            if(entityClass.isAssignableFrom(e.getClass())) {
                list.add(e);
            }
        }
            
        return (ArrayList<T>)list;
    }
    
    public boolean isEntityOfClass(UUID entityID, Class entityClass) {
        Entity temp = getEntityByID(entityID);
        if(temp != null)
            return entityClass.isAssignableFrom(getEntityByID(entityID).getClass());
        else
            return false;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
    
}
