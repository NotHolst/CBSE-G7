package dk.gruppe7.common;

import dk.gruppe7.common.data.Point;
import dk.gruppe7.common.data.Room;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Holst & Harald
 */
public class World {
    
    private int currentLevel;
    private Room currentRoom = null;
    private HashMap<Point, Point[]> map;
    private ArrayList<Entity> entities = new ArrayList<>(); //Jan bruger hashmap med <entityID (string), Entity>. Det er nok smart ift. lookup af en bestemt entity
    
    public ArrayList<Entity> getEntities()
    {
        return entities;
    }
    
    public void addEntity(Entity e)
    {
        entities.add(e);
    }
    
    public void removeEntity(Entity e)
    {
        entities.remove(e);
    }
    
    public Entity getEntityByID(UUID entityID){
        for(Entity e : entities)
            if(e.getId().equals(entityID)) return e;
        return null;
    }
    
    public ArrayList<Entity> getEntitiesByClass(Class c)
    {
        ArrayList<Entity> list = new ArrayList<Entity>();
        for(Entity e : entities)
            if(e.getClass().equals(c)) list.add(e);
        
        return list;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }
}
