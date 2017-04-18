package dk.gruppe7.common.eventhandlers;

import dk.gruppe7.common.annotations.EventHandler;

@EventHandler
public abstract class KeyEventHandler<T> {
    private final int virtualKeyCode;
    
    public KeyEventHandler(int virtualKeyCode) {
        this.virtualKeyCode = virtualKeyCode;
    }
    
    public abstract void call(T event);
}