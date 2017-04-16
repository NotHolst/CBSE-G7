/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.playercommon;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.damagecommon.HealthData;

/**
 *
 * @author Mathies H
 */
public class Player extends Entity {
    
    private Animator animator;

    private HealthData healthData = new HealthData(10);
    private int score = 0;
    

    public HealthData getHealthData() {
        return healthData;
    }

    public void incrementScoreBy(int score) {
        this.score += score;
    }
    
    public int getScore() {
        return score;
    }

    public Animator getAnimator() {
        return animator;
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
    }
    
}
