package dk.gruppe7.bosscommon;

import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.damagecommon.HealthData;
import dk.gruppe7.mobcommon.Mob;
import java.util.UUID;

public abstract class Boss extends Entity{
    protected HealthData healthData = new HealthData(5);
    protected Animator animator = new Animator();
    protected boolean invulnerable = false;
    protected UUID target;
    
    protected float height;

    public HealthData getHealthData() {
        return healthData;
    }

    public void setHealthData(HealthData healthData) {
        this.healthData = healthData;
    }

    public Animator getAnimator() {
        return animator;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }
    
    public float getHeight(){
        return this.height;
    }
    
    public void setHeight(float height){
        this.height = height;
    }

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }
    
    
    
    public abstract void process();
    
    
    
}
