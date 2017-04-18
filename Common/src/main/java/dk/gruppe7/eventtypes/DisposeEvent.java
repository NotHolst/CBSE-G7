/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.eventtypes;

import dk.gruppe7.common.data.Entity;
import dk.gruppe7.annotations.Event;
import java.util.UUID;

/**
 *
 * @author Mathies H
 */
@Event
public class DisposeEvent
{
    public DisposeEvent() {
        
    }
    //private UUID target;

    //public DisposeEvent(UUID target, UUID other)
    //{
    //    this.target = target;
    //}

    //public UUID getTargetEntity()
    //{
    //    return target;
    //}

}