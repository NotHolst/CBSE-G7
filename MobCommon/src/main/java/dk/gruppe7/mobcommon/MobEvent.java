/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

/**
 *
 * @author haral
 */
public class MobEvent {
    private Mob mob;
    private MobEventType type;
    
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

    
    
    
}
