/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.minimap;

import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Point;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.eventtypes.KeyReleasedEvent;
import dk.gruppe7.common.graphics.Color;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import java.util.HashSet;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author benjaminmlynek
 */

@ServiceProviders(value ={
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})

public class MiniMapSystem implements IProcess, IRender {

    boolean mKeyPressed = false;
    boolean miniMapActive = false;
    
    Vector2 miniMapPos = new Vector2(1000, 530);
    Vector2 miniMapSize = new Vector2(240, 150);
    Vector2 miniatureRoomSize = new Vector2(15,15);
    int zIndex = 400;
    
    int directionX = 0;
    int directionY = 0;
    
    HashSet<Point> roomMapping = new HashSet<>();
    Room oldRoom;
    
    @Override
    public void start(GameData gameData, World world) {
        Dispatcher.subscribe(this);
        System.out.println("MiniMap Component Loaded");
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
    }

    @Override
    public void process(GameData gameData, World world) {
    }

    @Override
    public void render(Graphics g, World world) {
        if (!miniMapActive) {
            g.drawRectangle(miniMapPos, miniMapSize, new Color(.2f, .2f, .2f, .2f), true, zIndex);

            for (Point point : roomMapping) {
                Vector2 temp = new Vector2(point.x, point.y).mul(miniatureRoomSize.add(1, 1));
                if (point.x == directionX && point.y == directionY) {
                    g.drawRectangle(miniMapPos.add(miniMapSize.div(2f)).sub(miniatureRoomSize.div(2f)).add(temp), miniatureRoomSize, new Color(.3f, .3f, .3f, .5f), true, zIndex + 1);
                } else {
                    g.drawRectangle(miniMapPos.add(miniMapSize.div(2f)).sub(miniatureRoomSize.div(2f)).add(temp), miniatureRoomSize, new Color(.75f, .75f, .75f, .5f), true, zIndex + 1);
                }
            }
        }

    }
    
    ActionEventHandler<RoomChangedEvent> roomChangedHandler = (event, world) -> {
        Room newRoom = event.getRoom();
        if(oldRoom == null) {
            roomMapping.add(new Point(directionX, directionY));
        } else {
            addRoomCoordinates(oldRoom, newRoom);
        }
        
        oldRoom = newRoom;
    };
    
    KeyEventHandler<KeyPressedEvent> mKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_M) {
        @Override
        public void call(KeyPressedEvent event) {
            mKeyPressed = event.getState();
            if(miniMapActive == false) {
                miniMapActive = true;
            } else {
                miniMapActive = false;
            }
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> mKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_M) {
        @Override
        public void call(KeyReleasedEvent event) {
            mKeyPressed = event.getState();
        }
    };
    
    private void addRoomCoordinates(Room oldRoom, Room newRoom) {
        
        if(newRoom.equals(oldRoom.getNorth())) {
            directionY += 1;
            System.out.println("Point added north");
        }
        
        if(newRoom.equals(oldRoom.getSouth())) {
            directionY -= 1;
            System.out.println("Point added south");
        }
        
        if(newRoom.equals(oldRoom.getWest())) {
            directionX -= 1;
            System.out.println("Point added west");
        }
        
        if(newRoom.equals(oldRoom.getEast())) {
            directionX += 1;
            System.out.println("Point added east");
        }
        
        roomMapping.add(new Point(directionX, directionY));
        
    }
    
}
