/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision;

import java.util.UUID;

/**
 *
 * @author Mathies H
 */
public class CollisionEvent
{
    private UUID targetID;
    private UUID otherID;
    private long expirationTick;

    public CollisionEvent(UUID targetID, UUID otherID, long currentTick)
    {
        this.targetID = targetID;
        this.otherID = otherID;
        this.expirationTick = currentTick + 2;
    }

    public UUID getTargetID()
    {
        return targetID;
    }

    public UUID getOtherID()
    {
        return otherID;
    }
    
    public long getExpirationTick() 
    {
        return expirationTick;
    }
}
