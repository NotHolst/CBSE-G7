/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weaponcommon;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author haral
 */
public class WeaponData {
    private static List<WeaponEvent> events = new ArrayList<>();
    
    public static List<WeaponEvent> getEvents()
    {
        return events;
    }
}
