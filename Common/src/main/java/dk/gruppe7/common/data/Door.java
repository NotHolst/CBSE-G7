/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.data;

import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.data.Direction;

/**
 *
 * @author pc4
 */
public class Door extends Entity
{
    private Direction direction;

    public Direction getDirection()
    {
        return direction;
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }
    
    
}
