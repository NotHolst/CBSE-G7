/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Mathies H
 */
public class CollisionData
{
    private static List<CollisionEvent> events = new ArrayList<>();
    private static long lastTick = -1;

    public static List<CollisionEvent> getEvents(long currentTick)
    {
        if(currentTick == lastTick) {
            return events;
        } else {
            return events = events.stream()
                                  .filter(event -> currentTick - event.getExpirationTick() < 2)
                                  .collect(Collectors.toList());
        }
    }
}
