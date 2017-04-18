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
    
    private float newMaxVelocity; 

    public float getNewMaxVelocity() {
        return newMaxVelocity;
    }

    public void setNewMaxVelocity(float newMaxVelocity) {
        this.newMaxVelocity = newMaxVelocity;
    }
}
