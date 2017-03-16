package dk.gruppe7.common;

import dk.gruppe7.common.data.EventType;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Holst & Harald
 */
public class Event implements Serializable {
    private EventType eventType;
    private UUID entityID;
    
    public Event(EventType eventType, UUID entityID){
        this.eventType = eventType;
        this.entityID = entityID;
    }

    public EventType getEventType() {
        return eventType;
    }

    public UUID getEntityID() {
        return entityID;
    }
    
    
}
