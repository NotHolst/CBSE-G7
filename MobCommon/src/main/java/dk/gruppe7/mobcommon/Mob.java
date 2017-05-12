/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mobcommon;

import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.damagecommon.HealthData;
import dk.gruppe7.data.MobType;
import java.util.UUID;

/**
 *
 * @author benjaminmlynek
 */
public class Mob extends Entity {
    
    private MobType type;
    private HealthData health = new HealthData(5);
    private Animator animator;
    private UUID target;
    private float attackRange;

    public HealthData getHealthData()
    {
        return health;
    }

    public MobType getMobType() {
        return type;
    }

    public void setMobType(MobType type) {
        this.type = type;
    }

    public Animator getAnimator() {
        return animator;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }
    
    
    
}
