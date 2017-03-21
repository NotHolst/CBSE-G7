/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package collision;

import dk.gruppe7.common.Entity;
import java.util.UUID;

/**
 *
 * @author Mathies H
 */
public class CollisionEvent
{
    private UUID target;
    private UUID other;

    public CollisionEvent(UUID target, UUID other)
    {
        this.target = target;
        this.other = other;
    }

    public UUID getTargetEntity()
    {
        return target;
    }

    public UUID getOtherEntity()
    {
        return other;
    }
}
