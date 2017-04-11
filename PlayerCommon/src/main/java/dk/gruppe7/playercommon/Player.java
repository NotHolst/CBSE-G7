/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.playercommon;

import dk.gruppe7.common.Entity;
import dk.gruppe7.damagecommon.HealthData;
import java.util.UUID;

/**
 *
 * @author Mathies H
 */
public class Player extends Entity {
    private HealthData healthData = new HealthData(10);

    public HealthData getHealthData()
    {
        return healthData;
    }

    
    
}
