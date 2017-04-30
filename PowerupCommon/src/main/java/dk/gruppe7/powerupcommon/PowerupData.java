/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.powerupcommon;

/**
 *
 * @author JOnes
 */
public class PowerupData {
    
    private float boostMaxVelocity;
    private float fireRate;
    private PowerupType powerupType;

    public PowerupData() {
        this.boostMaxVelocity = 1;
        this.fireRate = 1;
    }
    
    public float getBoostMaxVelocity() {
        return boostMaxVelocity;
    }

    public void setBoostMaxVelocity(float boostMaxVelocity) {
        this.boostMaxVelocity = boostMaxVelocity;
    }
    
    

    

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public PowerupType getPowerupType() {
        return powerupType;
    }

    public void setPowerupType(PowerupType powerupType) {
        this.powerupType = powerupType;
    }

}
