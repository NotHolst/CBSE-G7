/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Haral
 */
public class MobData
{
    private static List<MobEvent> events = new ArrayList<>();
    
    public static List<MobEvent> getEvents()
    {
        return events;
    }
}
