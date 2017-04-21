package dk.gruppe7.common.data;

import java.util.ArrayList;

public class Room {

    private Room north = null;
    private Room east = null;
    private Room south = null;
    private Room west = null;
    private ArrayList<Entity> entities = new ArrayList<>();
    
    private int distanceFromStart;

    public ArrayList<Entity> getEntities() {
        return entities;
    }
    
    private boolean cleared = false;

    public Room() {

    }

    public Room(Room originRoom, Direction originDirection) {
        switch (originDirection) {
            case NORTH:
                south = originRoom;
                break;

            case EAST:
                west = originRoom;
                break;

            case SOUTH:
                north = originRoom;
                break;

            case WEST:
                east = originRoom;
                break;
        }
    }

    public Room getNorth() {
        return north;
    }

    public void setNorth(Room north) {
        this.north = north;
    }

    public Room getEast() {
        return east;
    }

    public void setEast(Room east) {
        this.east = east;
    }

    public Room getSouth() {
        return south;
    }

    public void setSouth(Room south) {
        this.south = south;
    }

    public Room getWest() {
        return west;
    }

    public void setWest(Room west) {
        this.west = west;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public int getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }
    
    

}
