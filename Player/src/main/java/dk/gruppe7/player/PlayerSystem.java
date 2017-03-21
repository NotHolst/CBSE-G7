/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.player;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mathies H
 */
@ServiceProvider (service = IProcess.class) 
 
public class PlayerSystem implements IProcess
{
    List<ShootingEvent> events = ShootingData.getEvents();
    Entity player;
    
    @Override
    public void start(GameData gameData, World world)
    {
        makePlayer();
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, World world)
    {
        world.removeEntity(player);
        player = null;
    }

    @Override
    public void process(GameData gameData, World world)
    {
        events.add(new ShootingEvent(player));
    }
    
    private void makePlayer()
    {
        player = new Entity();
        player.setPosition(new Vector2(50, 50));
        player.setCollidable(true);
        
        
        
        
    }
    
    
    
}
