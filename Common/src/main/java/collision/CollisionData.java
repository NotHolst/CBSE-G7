/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies H
 */
public class CollisionData
{
    private static List<CollisionEvent> events = new ArrayList<>();

    public static List<CollisionEvent> getEvents()
    {
        return events;
    }
}
