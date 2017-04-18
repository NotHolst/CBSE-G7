package dk.gruppe7.eventtypes;

import dk.gruppe7.annotations.Event;

@Event
public class KeyPressedEvent {
    private final int virtualKeyCode;
    private final boolean state = true;
    
    public KeyPressedEvent(int virtual_keycode) {
        this.virtualKeyCode = virtual_keycode;
    }
    
    public int getVirtualKeyCode() {
        return virtualKeyCode;
    }
    
    public boolean getState() {
        return state;
    }
}