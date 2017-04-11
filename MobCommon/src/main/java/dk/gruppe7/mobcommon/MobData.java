/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Haral
 */
public class MobData
{
    private static List<MobEvent> events = new ArrayList<>();
    private static long lastTick = -1;

    public static List<MobEvent> getEvents(long currentTick)
    {
        if(currentTick != lastTick) {
            lastTick = currentTick;
            
            events = events.stream()
                           .filter(event -> currentTick - event.getExpirationTick() < 2)
                           .collect(Collectors.toList());
        }
        
        return events;
    }
    
    @Deprecated
    public static List<MobEvent> getEvents()
    {
        return events;
    }
}
