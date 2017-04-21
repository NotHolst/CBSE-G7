
package dk.gruppe7.debugtools;

import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.eventtypes.KeyReleasedEvent;
import dk.gruppe7.common.graphics.Color;
import dk.gruppe7.common.graphics.Graphics;
import java.util.ListIterator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})
public class DebugToolsSystem implements IProcess, IRender {

    boolean menuActive;
    int menuKey = VirtualKeyCode.VC_ALT;
    
    int menuZindex = 500;
    Vector2 menuPos = new Vector2(32, 32);
    Vector2 menuSize = new Vector2(180, 240);
    
    Color on = new Color(.5f,1,0.5f);
    Color off = new Color(1, .5f, .5f);
    
    //Options
    boolean collisionBounds = false;
    
    @Override
    public void start(GameData gameData, World world) {
        
        Dispatcher.subscribe(this);
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
        if(menuActive){
            g.drawRectangle(menuPos, menuSize, new Color(.2f, .2f, .2f, .5f), true, menuZindex);
            
            g.drawString(menuPos.add(menuSize).sub(new Vector2(menuSize.x-10, 20)), "1. Collision Bounds", new Color(1,1,1), menuZindex+1);
            g.drawString(menuPos.add(menuSize).sub(new Vector2(40, 20)), collisionBounds?"ON":"OFF", collisionBounds?on:off, menuZindex+1);
        }
        
        for(Entity entity : world.getEntities()){
            
            if(collisionBounds){
                Rectangle bounds = entity.getBounds();
                g.drawRectangle(entity.getPosition().add(new Vector2(bounds.getX(), bounds.getY())), new Vector2(bounds.getWidth(), bounds.getHeight()), on, false, 9999999-1);
            }
        }
    }

    KeyEventHandler<KeyPressedEvent> menuKeyHandler = new KeyEventHandler<KeyPressedEvent>(menuKey) {
        @Override
        public void call(KeyPressedEvent event) {
            menuActive = true;
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> menuRelKeyHandler = new KeyEventHandler<KeyReleasedEvent>(menuKey) {
        @Override
        public void call(KeyReleasedEvent event) {
            menuActive = false;
        }
    };
    
    KeyEventHandler<KeyReleasedEvent> OneKeyHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_1) {
        @Override
        public void call(KeyReleasedEvent event) {
            if(!menuActive)return;
            collisionBounds = !collisionBounds;
        }
    };

}
