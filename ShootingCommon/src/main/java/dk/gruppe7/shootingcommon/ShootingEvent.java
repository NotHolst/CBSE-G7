/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.shootingcommon;

/**
 *
 * @author Mathies H
 */
public class ShootingEvent
{
    private Bullet bullet;
    private long expirationTick;

    @Deprecated
    public ShootingEvent(Bullet bluePrint)
    {
        this.bullet = bluePrint;
    }
    
    public ShootingEvent(Bullet bluePrint, long currentTick) {
        this.bullet = bluePrint;
        this.expirationTick = currentTick + 2;
    }

    public Bullet getBlueprint()
    {
        return bullet;
    }
    
    public long getExpirationTick() {
        return expirationTick;
    }
}
