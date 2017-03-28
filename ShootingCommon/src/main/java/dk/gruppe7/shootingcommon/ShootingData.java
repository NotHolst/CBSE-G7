/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.shootingcommon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathies H
 */
public class ShootingData
{
    private static List<ShootingEvent> events = new ArrayList<>();
    
    public static List<ShootingEvent> getEvents()
    {
        return events;
    }
}
