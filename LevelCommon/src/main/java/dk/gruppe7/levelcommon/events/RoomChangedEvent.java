package dk.gruppe7.levelcommon.events;

import dk.gruppe7.common.annotations.Event;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.levelcommon.LevelEvent;

@Event
public class RoomChangedEvent extends LevelEvent{
    Room room = null;
    
    public RoomChangedEvent(Room room){
        this.room = room;
    }
    
    public Room getRoom(){
        return this.room;
    }
}
