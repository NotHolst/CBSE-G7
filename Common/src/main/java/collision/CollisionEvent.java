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

    public CollisionEvent(UUID targetID, UUID otherID)
    {
        this.targetID = targetID;
        this.otherID = otherID;
    }

    public UUID getTargetID()
    {
        return targetID;
    }

    public UUID getOtherID()
    {
        return otherID;
    }
}
