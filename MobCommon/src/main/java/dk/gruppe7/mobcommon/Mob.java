/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

import dk.gruppe7.common.Entity;
import dk.gruppe7.data.MobType;

/**
 *
 * @author benjaminmlynek
 */
public class Mob extends Entity {
    
    private MobType type;

    public MobType getMobType() {
        return type;
    }

    public void setMobType(MobType type) {
        this.type = type;
    }
    
}
