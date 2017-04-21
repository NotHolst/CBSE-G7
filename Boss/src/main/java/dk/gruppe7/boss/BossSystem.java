
package dk.gruppe7.boss;

import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.levelcommon.events.LevelChangedEvent;
import dk.gruppe7.levelcommon.events.LevelGenerationEvent;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})

public class BossSystem implements IProcess, IRender{
        
    Room currentLevelBossRoom;
    boolean bossDead = false;
    
    @Override
    public void start(GameData gameData, World world) {
        findBossRoom(world);
        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
    }

    @Override
    public void process(GameData gameData, World world) {
        if(bossDead){
            Dispatcher.post(new LevelChangedEvent(), world);
            bossDead = false;
        }
    }

    @Override
    public void render(Graphics g, World world) {
        if(world.getCurrentRoom().equals(currentLevelBossRoom))
            g.drawString(new Vector2(300, 300), "U IN DA BAWS RUM! PRESS ENTER TO DEFEAT THE MIGHTY  BOSS");
    }
    
    ActionEventHandler<LevelGenerationEvent> levelGenerationHandler = (event, world) -> {
        findBossRoom(world);
    };
    
    ActionEventHandler<LevelChangedEvent> levelChangedHandler = (event, world) -> {
        //TODO: Remove this
        System.out.println("Level should change now, this is from bossSystem line 63");
    };
    
    private void findBossRoom(World world){
        currentLevelBossRoom = findHighestDistanceRoom(world.getCurrentRoom());
    }
    
    private Room findHighestDistanceRoom(Room startRoom){
       
       ArrayList<Room> openList = new ArrayList<>();
       ArrayList<Room> closedList = new ArrayList<>();
       
       Room highest = startRoom;
       openList.add(highest);
       while(openList.size() > 0){
           Room current = openList.remove(0);
           closedList.add(current);
           
           if(current.getDistanceFromStart() > highest.getDistanceFromStart())
               highest = current;
           
           openList.addAll(
                getNeighbours(current).stream()
                   .filter(
                       room -> !closedList.contains(room)
                   ).collect(Collectors.toList())
           );
       }
       return highest;
    }
    
    private ArrayList<Room> getNeighbours(Room room){
        ArrayList<Room> result = new ArrayList<>();
            if(room.getNorth() != null)result.add(room.getNorth());
            if(room.getSouth() != null)result.add(room.getSouth());
            if(room.getWest()!= null)result.add(room.getWest());
            if(room.getEast()!= null)result.add(room.getEast());
        return result;
    }
    
    KeyEventHandler<KeyPressedEvent> enterKeyHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_ENTER) {
        @Override
        public void call(KeyPressedEvent event) {
            bossDead = true;
        }
    };

}