package dk.gruppe7.common;

import dk.gruppe7.common.data.Point;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Holst & Harald
 */
public class World {
    
    private int currentLevel;
    private int currentRoom;
    private HashMap<Point, Point[]> map;
    private ArrayList<Entity> entities = new ArrayList<>(); //Jan bruger hashmap med <entityID (string), Entity>. Det er nok smart ift. lookup af en bestemt entity
    private ArrayList<Event> events; //Linked list ?
    
    
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
}
