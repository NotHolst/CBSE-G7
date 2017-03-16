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
    private ArrayList<Entity> entities; //Jan bruger hashmap med <entityID (string), Entity>. Det er nok smart ift. lookup af en bestemt entity
    private ArrayList<Event> events; //Linked list ?
    
}
