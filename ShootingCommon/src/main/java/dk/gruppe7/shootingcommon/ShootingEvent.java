/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.shootingcommon;

import dk.gruppe7.common.annotations.Event;

/**
 *
 * @author Mathies H
 */
@Event
public class ShootingEvent
{
    private Bullet bullet;

    public ShootingEvent(Bullet bluePrint)
    {
        this.bullet = bluePrint;
    }


    public Bullet getBlueprint()
    {
        return bullet;
    }
}
