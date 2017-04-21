/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.damagecommon;

import dk.gruppe7.common.annotations.Event;
import java.util.UUID;

/**
 *
 * @author pc4
 */
@Event
public class DamageEvent
{
    private DamageData damageDealt;
    private UUID target;

    public DamageEvent(DamageData damageDealt, UUID target)
    {
        this.damageDealt = damageDealt;
        this.target = target;
    }

    public DamageData getDamageDealt()
    {
        return damageDealt;
    }

    public UUID getTarget()
    {
        return target;
    }
    
    
    
}
