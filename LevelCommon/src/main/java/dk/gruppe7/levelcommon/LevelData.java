package dk.gruppe7.levelcommon;

import java.util.ArrayList;
import java.util.List;

public class LevelData {
    
    private static List<LevelEvent> events = new ArrayList<>();
    
    public static List<LevelEvent> getEvents(){
        return events;
    }
    
}
