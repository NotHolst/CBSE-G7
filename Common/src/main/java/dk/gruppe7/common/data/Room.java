package dk.gruppe7.common.data;

import java.util.ArrayList;

public class Room {

    Room north = null;
    Room east = null;
    Room south = null;
    Room west = null;
    private ArrayList<Entity> entities = new ArrayList<>();

    public ArrayList<Entity> getEntities() {
        return entities;
    }

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

}
