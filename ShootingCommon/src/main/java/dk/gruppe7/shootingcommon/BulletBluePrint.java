/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.shootingcommon;

import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;

/**
 *
 * @author Mathies H
 */
public class BulletBluePrint
{
    private Rectangle bounds;
    private Vector2 velocity;
    private float maxVelocity;
    private float accerleration;
    private Vector2 position;
    private BulletType bulletType;

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void setBounds(Rectangle bounds)
    {
        this.bounds = bounds;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }

    public float getMaxVelocity()
    {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity)
    {
        this.maxVelocity = maxVelocity;
    }

    public float getAccerleration()
    {
        return accerleration;
    }

    public void setAccerleration(float accerleration)
    {
        this.accerleration = accerleration;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }

    public BulletType getBulletType()
    {
        return bulletType;
    }

    public void setBulletType(BulletType bulletType)
    {
        this.bulletType = bulletType;
    }
    
    


}
