/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.powerupcommon;

import dk.gruppe7.common.annotations.Event;
import java.util.UUID;

/**
 *
 * @author JOnes
 */
@Event
public class PowerupEvent {

    private PowerupData powerupData;
    private UUID target;

    public PowerupEvent(PowerupData powerupData, UUID target)
    {
        this.powerupData = powerupData;
        this.target = target;
    }

    public PowerupData getPowerupData()
    {
        return powerupData;
    }

    public UUID getTarget()
    {
        return target;
    }
    
    
    
}
