/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.powerup;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.eventtypes.DisposeEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.powerupcommon.Powerup;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.eventtypes.KeyReleasedEvent;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.powerupcommon.PowerupData;
import dk.gruppe7.powerupcommon.PowerupEvent;
import dk.gruppe7.powerupcommon.PowerupType;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author JOnes
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})

public class PowerupSystem implements IProcess, IRender {

    ArrayList<Powerup> listOfPowerupsToBeRemoved = new ArrayList<>();
    InputStream texture = getClass().getResourceAsStream("speedBoost.png");
    UUID player = null;
    private ArrayList<Room> roomBeenIn = new ArrayList<>();

    @Override
    public void start(GameData gameData, World world) {
        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
        world.removeEntitiesByClassRecursively(Powerup.class);
    }

    @Override
    public void process(GameData gameData, World world) {

        if (player == null) {
            // finds and sets the player object
            for (Entity element : world.getEntities()) {
                if (element instanceof Player) {
                    player = element.getId();
                }
            }
        }
        
    }

    ActionEventHandler<RoomChangedEvent> RoomChangeHandler = (event, world) -> {
        if (!roomBeenIn.contains(world.getCurrentRoom())) {
            roomBeenIn.add(world.getCurrentRoom());
            if(Math.random() <= 1) {
                Powerup powerup = (Powerup) makePowerup();
                double chance = Math.random();
                if(chance <= 0.5) { // movementspeedboost
                    powerup.getPowerupData().setPowerupType(PowerupType.PLAYER);
                    powerup.getPowerupData().setBoostMaxVelocity(1.5f);
                }else {
                    powerup.getPowerupData().setPowerupType(PowerupType.WEAPON);
                    powerup.getPowerupData().setFireRate(0.5f);
                }
                world.addEntity(powerup);
            }
        }
    };

    ActionEventHandler<CollisionEvent> pickupCollisionHandler = (event, world) -> {
        Entity targetEntity = world.getEntityByID(event.getTargetID());
        // if the targetEntity is a Powerup and the "other" is the player we wanna do something
        if (targetEntity instanceof Powerup && event.getOtherID().equals(player)) {
            Powerup powerup = (Powerup) targetEntity;
            Dispatcher.post(new PowerupEvent(powerup.getPowerupData(), event.getOtherID()), world);
            listOfPowerupsToBeRemoved.add(powerup);
        }
    };

    ActionEventHandler<DisposeEvent> disposalHandler = (event, world) -> {
        world.removeEntities(listOfPowerupsToBeRemoved);
        listOfPowerupsToBeRemoved.clear();
    };

    @Override
    public void render(Graphics g, World world) {
        for (Powerup powerup : world.<Powerup>getEntitiesByClass(Powerup.class)) {
            g.drawSprite(powerup.getPosition(), new Vector2(powerup.getBounds().getWidth(), powerup.getBounds().getHeight()), texture, 0);
        }
    }

    private Entity makePowerup() {
        return new Powerup() {
            {
                setPosition(new Vector2((float) Math.random() * 500.f + 64, (float) Math.random() * 500.f + 64));
                setCollidable(true);
                setBounds(new Rectangle(21, 36));
                setRoomPersistent(false);
            }
        };
    }

}
