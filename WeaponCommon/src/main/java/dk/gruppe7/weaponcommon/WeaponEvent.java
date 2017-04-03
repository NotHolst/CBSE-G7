/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weaponcommon;

import java.util.UUID;

/**
 *
 * @author haral
 */
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
