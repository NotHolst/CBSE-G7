package dk.gruppe7.common;

import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import java.io.InputStream;
import java.util.UUID;

/**
 *
 * @author Holst & Harald
 */


public class Entity {
    
    private UUID id = UUID.randomUUID();
    private Vector2 position = new Vector2(0, 0);
    private float rotation;
    private Vector2 velocity = new Vector2(0, 0); 
    private float acceleration;
    private float maxVelocity;
    private Rectangle bounds;
    private boolean collidable;
    private InputStream inputStream;
    
    public UUID getId()
    {
        return id;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }

    public float getRotation()
    {
        return rotation;
    }

    public void setRotation(float rotation)
    {
        this.rotation = rotation;
    }

    public Vector2 getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
    }

    public float getAcceleration()
    {
        return acceleration;
    }

    public void setAcceleration(float acceleration)
    {
        this.acceleration = acceleration;
    }

    public float getMaxVelocity()
    {
        return maxVelocity;
    }

    public void setMaxVelocity(float maxVelocity)
    {
        this.maxVelocity = maxVelocity;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void setBounds(Rectangle bounds)
    {
        this.bounds = bounds;
    }

    public boolean isCollidable()
    {
        return collidable;
    }

    public void setCollidable(boolean isColliable)
    {
        this.collidable = isColliable;
    }
    
    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
