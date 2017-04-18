/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

import dk.gruppe7.common.annotations.Event;

/**
 *
 * @author haral
 */
@Event
public class MobEvent {
    private Mob mob;
    private MobEventType type;
    private long expirationTick;
    
    public MobEvent(Mob mob, MobEventType type){
        this.mob = mob;
        this.type = type;
    }

    public Mob getMob() {
        return mob;
    }

    public MobEventType getType() {
        return type;
    }

    public long getExpirationTick() {
        return expirationTick;
    }
}