package dk.gruppe7.powerupcommon;

import dk.gruppe7.common.data.Entity;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JOnes
 */
public class Powerup extends Entity {
    
    private PowerupData powerupData;

    public Powerup() {
        this.powerupData = new PowerupData();
    }

    public PowerupData getPowerupData() {
        return powerupData;
    }

    public void setPowerupData(PowerupData powerupData) {
        this.powerupData = powerupData;
    }
    
    
}
