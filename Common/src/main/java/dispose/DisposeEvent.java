/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dispose;

import collision.*;
import dk.gruppe7.common.Entity;
import java.util.UUID;

/**
 *
 * @author Mathies H
 */
public class DisposeEvent
{
    private UUID target;

    public DisposeEvent(UUID target, UUID other)
    {
        this.target = target;
    }

    public UUID getTargetEntity()
    {
        return target;
    }

}
