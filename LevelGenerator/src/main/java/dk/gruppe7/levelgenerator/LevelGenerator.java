package dk.gruppe7.levelgenerator;

import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Point;
import dk.gruppe7.common.data.Room;
import java.util.ArrayList;
import java.util.ListIterator;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = IProcess.class)

public class LevelGenerator implements IProcess
{
    final ArrayList<Point> expansionDirections = new ArrayList<Point>() {{
        add(new Point(1, 0));
        add(new Point(-1, 0));
        add(new Point(0, 1));
        add(new Point(0, -1));
    }};
    
    @Override
    public void start(GameData gameData, World world) {
        int[][] gridMap = generateGridMap(15);
        Room start = graphifyGridMap(gridMap);
        world.setCurrentRoom(start);
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Clean-up relevant ?
    }

    @Override
    public void process(GameData gameData, World world) {
        // We will probably listen for events here to trigger the generator when a new level is needed. 
    }
        
    private int[][] generateGridMap(int numberOfRooms) {

        // Declare return variable.
        int[][] grid = null;

        while (true) {

            // Initialize required variables.
            int attempt = 0;
            Point spawn = null;
            int roomsLeftToPlace = numberOfRooms;
            grid = new int[numberOfRooms / 2][numberOfRooms / 2];

            // Block pseudo random positions.
            for (int[] column : grid) {
                for (int y = 0; y < (grid.length / 2); y++) {
                    column[(int) (Math.random() * (grid.length - 1))] = -1;
                }
            }

            // Pick random start point.
            while (spawn == null) {
                int x = (int) (Math.random() * (grid.length - 1));
                int y = (int) (Math.random() * (grid.length - 1));

                if (grid[x][y] == 0) {
                    spawn = new Point(x, y);
                    roomsLeftToPlace--;
                    grid[x][y] = 1;
                }
            }

            // Fill from start point.
            ArrayList<Point> listOfActiveCells = new ArrayList<>();
            listOfActiveCells.add(spawn);

            while (roomsLeftToPlace > 0) {
                if (++attempt > numberOfRooms) {
                    break;
                }

                for (ListIterator<Point> iterator = listOfActiveCells.listIterator(); iterator.hasNext();) {
                    Point currPoint = iterator.next();

                    // Stop if zero rooms are left to be placed.
                    if (!(roomsLeftToPlace > 0)) {
                        break;
                    }

                    for (int i = 0; i < expansionDirections.size(); i++) {
                        try {
                            grid[currPoint.x + expansionDirections.get(i).x][currPoint.y + expansionDirections.get(i).y] += 0;
                        } catch (IndexOutOfBoundsException ex) {
                            continue;
                        }

                        if (grid[currPoint.x + expansionDirections.get(i).x][currPoint.y + expansionDirections.get(i).y] == 0) {
                            grid[currPoint.x + expansionDirections.get(i).x][currPoint.y + expansionDirections.get(i).y] = 1;
                            iterator.add(new Point(currPoint.x + expansionDirections.get(i).x, currPoint.y + expansionDirections.get(i).y));
                            roomsLeftToPlace--;
                            break;
                        }
                    }
                }
            }

            if (roomsLeftToPlace == 0) {
                break;
            }
        }

        return grid;
    }
    
    private Room graphifyGridMap(int[][] grid) {
        Room[][] roomGridMap = new Room[grid.length][grid.length];
        Room ret = null;
        
        // Instantiate rooms at positions used in the new 2d array to avoid NullPointerExceptions later.
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid.length; y++) {
                if(grid[x][y] == 1) {
                    Room r = new Room();
                    r.setDistanceFromStart(x+y); // since the first room is always the start room, this distance estimate is good enough
                    roomGridMap[x][y] = r;
                }
            }
        }
        
        // Link rooms based on their positions in the original 2d array.
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid.length; y++) {
                if(grid[x][y] == 1) {
                    // Pick the first room that is processed as the starting room.
                    if(ret == null)
                        ret = roomGridMap[x][y];
                    
                    // If the current room has a neighbour to the right then establish a link to it ..
                    if(x + 1 < grid.length)
                        if(grid[x][y] == 1)
                            roomGridMap[x][y].setEast(roomGridMap[x + 1][y]); 
                    
                    if(x - 1 >= 0) 
                        if(grid[x][y] == 1)
                            roomGridMap[x][y].setWest(roomGridMap[x - 1][y]);
                    
                    if(y + 1 < grid.length) 
                        if(grid[x][y] == 1)
                            roomGridMap[x][y].setSouth(roomGridMap[x][y + 1]);
                    
                    if(y - 1 >= 0) 
                        if(grid[x][y] == 1)
                            roomGridMap[x][y].setNorth(roomGridMap[x][y - 1]);
                }
            }
        }
        
        return ret;
    }
}
