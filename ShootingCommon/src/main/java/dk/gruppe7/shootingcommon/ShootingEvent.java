/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.shootingcommon;

import dk.gruppe7.common.Entity;

/**
 *
 * @author Mathies H
 */
public class ShootingEvent
{
    private BulletBluePrint blueprint;

    public ShootingEvent(BulletBluePrint bluePrint)
    {
        this.blueprint = bluePrint;
    }

    public BulletBluePrint getBlueprint()
    {
        return blueprint;
    }
    
    
    
    
}