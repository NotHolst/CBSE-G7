/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weaponcommon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author haral
 */
public class WeaponData {
    private static List<WeaponEvent> events = new ArrayList<>();
    private static long lastTick = -1;
    
    @Deprecated
    public static List<WeaponEvent> getEvents()
    {
        return events;
    }
    
    public static List<WeaponEvent> getEvents(long currentTick) {
        if(currentTick != lastTick) {
            lastTick = currentTick;
            
            events = events.stream()
                           .filter(event -> currentTick - event.getExpirationTick() < 2)
                           .collect(Collectors.toList());
        }
        
        return events;
    }
}
