package dk.gruppe7.levelcommon.events;

import dk.gruppe7.common.annotations.Event;
import dk.gruppe7.common.data.Room;

@Event
public class RoomChangedEvent {
    Room room = null;
    
    public RoomChangedEvent(Room room){
        this.room = room;
    }
    
    public Room getRoom(){
        return this.room;
    }
}
