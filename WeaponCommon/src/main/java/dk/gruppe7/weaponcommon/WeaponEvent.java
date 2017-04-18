/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weaponcommon;

import dk.gruppe7.common.annotations.Event;
import java.util.UUID;

/**
 *
 * @author haral
 */
@Event
public class WeaponEvent {
    private UUID shooter;
    
    public WeaponEvent(UUID shooter)
    {
        this.shooter = shooter;
    }
    public UUID getShooter()
    {
        return shooter;
    }
   
}
