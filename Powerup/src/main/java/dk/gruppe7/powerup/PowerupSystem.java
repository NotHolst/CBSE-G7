/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.powerup;

import collision.CollisionData;
import collision.CollisionEvent;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.powerupcommon.Powerup;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.playercommon.Player;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author JOnes
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class)
    , @ServiceProvider(service = IRender.class)
})

public class PowerupSystem implements IProcess, IRender {

    private List<Powerup> powerups;
    InputStream texture = getClass().getResourceAsStream("speedBoost.png");

    Entity player = null;

    @Override
    public void start(GameData gameData, World world) {
        powerups = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Entity e = makePowerup();
            powerups.add((Powerup) e);
            world.addEntity(e);
        }

    }

    @Override
    public void stop(GameData gameData, World world) {
        for (Powerup powerup : powerups) {
            world.removeEntity(powerup);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        if (player == null) {
            // finds and sets the player object
            for (Entity element : world.getEntities()) {
                if (element instanceof Player) {
                    player = (Player) element;
                }
            }
        }
        // Iterates over all the CollisionEvents backwards because we are removing from it while iterating;
        for (int i = CollisionData.getEvents(gameData.getTickCount()).size() - 1; i >= 0; i--) {
            // Sets the current target of the CollisionEvent
            Entity targetEntity = world.getEntityByID(CollisionData.getEvents(gameData.getTickCount()).get(i).getTargetID());
            // if the targetEntity is a Powerup and the "other" is the player we wanna do something
            if (targetEntity instanceof Powerup && CollisionData.getEvents(gameData.getTickCount()).get(i).getOtherID().equals(player.getId())) {
                Powerup powerup = (Powerup) targetEntity; // we know it is a Powerup so we set it to a Powerup Object type;
                player.setMaxVelocity(powerup.getNewMaxVelocity()); // sets the new value;
                // Cleanup
                world.removeEntity(powerup);
                powerups.remove(powerup);
                CollisionData.getEvents(gameData.getTickCount()).remove(i);
            }

        }
    }

    @Override
    public void render(Graphics g, World world) {
        for (Powerup powerup : powerups) {
            g.drawSprite(powerup.getPosition(), new Vector2(powerup.getBounds().getWidth(), powerup.getBounds().getHeight()), texture, 0);
        }
    }

    private Entity makePowerup() {
        return new Powerup() {
            {
                setPosition(new Vector2(200.f, 50.f));
                setCollidable(true);
                setBounds(new Rectangle(21, 36));
                setNewMaxVelocity(1000);
            }
        };
    }

}
