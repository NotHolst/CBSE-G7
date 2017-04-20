/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.door;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.data.Direction;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import dk.gruppe7.mobcommon.Mob;
import dk.gruppe7.playercommon.Player;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author pc4
 */
@ServiceProvider(service = IProcess.class)

public class DoorSystem implements IProcess, IRender {

    InputStream doorClosedHorizontal = getClass().getResourceAsStream("doorClosed.png");
    InputStream doorClosedVertical = getClass().getResourceAsStream("doorClosed_vertical.png");
    InputStream doorOpenHorizontal = getClass().getResourceAsStream("doorOpen.png");
    InputStream doorOpenVertical = getClass().getResourceAsStream("doorOpen_vertical.png");

    Room currentRoom = null;
    List<Door> currentDoors = new ArrayList<>();

    Map<Direction, Vector2> doorPosition = new HashMap<>();

    @Override
    public void start(GameData gameData, World world) {
        doorPosition.put(Direction.SOUTH, new Vector2(gameData.getScreenWidth() / 2, 38));
        doorPosition.put(Direction.WEST, new Vector2(38, gameData.getScreenHeight() / 2));
        doorPosition.put(Direction.NORTH, new Vector2(gameData.getScreenWidth() / 2, gameData.getScreenHeight() - 38));
        doorPosition.put(Direction.EAST, new Vector2(gameData.getScreenWidth() - 38, gameData.getScreenHeight() / 2));

        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
    }

    @Override
    public void process(GameData gameData, World world) {
        if (currentRoom == null) {
            if (world.getCurrentRoom() != null) {
                currentRoom = world.getCurrentRoom();
                RoomChange(currentRoom, world);
            }
        }
    }

    int h = 125;
    int w = 25;

    private void RoomChange(Room newRoom, World world) {
        world.removeEntities(currentDoors);

        currentDoors = new ArrayList<>();
        if (newRoom.getNorth() != null) {
            currentDoors.add(new Door() {
                {
                    setCollidable(true);
                    setBounds(new Rectangle(h, w));
                    setPositionCentered(doorPosition.get(Direction.NORTH));
                    setDirection(Direction.NORTH);
                }
            });
        }
        if (newRoom.getWest() != null) {
            currentDoors.add(new Door() {
                {
                    setCollidable(true);
                    setBounds(new Rectangle(w, h));
                    setPositionCentered(doorPosition.get(Direction.WEST));

                    setRotation(180);
                    setDirection(Direction.WEST);
                }
            });
        }
        if (newRoom.getSouth() != null) {
            currentDoors.add(new Door() {
                {
                    setCollidable(true);
                    setBounds(new Rectangle(h, w));
                    setPositionCentered(doorPosition.get(Direction.SOUTH));
                    setDirection(Direction.SOUTH);
                    setRotation(180);

                }
            });
        }
        if (newRoom.getEast() != null) {
            currentDoors.add(new Door() {
                {
                    setCollidable(true);
                    setBounds(new Rectangle(w, h));
                    setPositionCentered(doorPosition.get(Direction.EAST));
                    //setRotation(270);
                    setDirection(Direction.EAST);
                }
            });

        }

        world.addEntities(currentDoors);

        Dispatcher.post(new RoomChangedEvent(newRoom), world);

        for (int i = world.getCurrentRoom().getEntities().size() - 1; i >= 0; i--) {
            world.addEntity(world.getCurrentRoom().getEntities().get(i));
            world.getCurrentRoom().getEntities().remove(i);
        }
    }

    ActionEventHandler<CollisionEvent> collisionHandler = new ActionEventHandler<CollisionEvent>() {
        @Override
        public void call(CollisionEvent event, World world) {
            if (world.getEntitiesByClass(Mob.class).size() > 0) {
                return;
            }
            
            currentRoom.setCleared(true);
            
            for (Door door : currentDoors) {
                if (event.getOtherID().equals(door.getId())) {
                    Entity entity = world.getEntityByID(event.getTargetID());
                    
                    Player player = entity instanceof Player ? (Player) entity : null;
                    if (player != null) {
                        // Adding all the entities that are not room persistent to the currentRoom.
                        for (Entity persistentEntity : world.getEntities()) {
                            if (!persistentEntity.isRoomPersistent()) {
                                currentRoom.getEntities().add(persistentEntity);
                            }
                        }
                        // Removing all the entities that are not room persisten from the world.
                        for (int i = world.getEntities().size() - 1; i >= 0; i--) {
                            if (!world.getEntities().get(i).isRoomPersistent()) {
                                world.getEntities().remove(i);
                            }
                        }
                        
                        switch (door.getDirection()) {
                            case NORTH:
                                world.setCurrentRoom(currentRoom.getNorth());
                                break;
                            case WEST:
                                world.setCurrentRoom(currentRoom.getWest());
                                break;
                            case SOUTH:
                                world.setCurrentRoom(currentRoom.getSouth());
                                break;
                            case EAST:
                                world.setCurrentRoom(currentRoom.getEast());
                                break;
                        }
                        
                        currentRoom = world.getCurrentRoom();
                        player.setPosition(new Vector2(650, 380));
                        RoomChange(currentRoom, world);
                    }
                }
            }
        }
    };

    @Override
    public void render(Graphics g, World world) {
        boolean cleared = (world.<Mob>getEntitiesByClass(Mob.class).size() <= 0);

        Vector2 size;
        for (Door currentDoor : currentDoors) {
            InputStream temp;

            if (currentDoor.getDirection() == Direction.EAST || currentDoor.getDirection() == Direction.WEST) {
                temp = (cleared) ? doorOpenVertical : doorClosedVertical;
            } else {
                temp = (cleared) ? doorOpenHorizontal : doorClosedHorizontal;
            }

            size = new Vector2(currentDoor.getBounds().getWidth(), currentDoor.getBounds().getHeight());

            g.drawSprite(/* Position    */currentDoor.getPosition(),
                    /* Size        */ size,
                    /* InputStream */ temp,
                    /* Rotation    */ currentDoor.getRotation(),
                    /* LayerHeight */ 3
            );
        }
    }
}
