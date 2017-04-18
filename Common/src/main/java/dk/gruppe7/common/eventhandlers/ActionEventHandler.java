package dk.gruppe7.common.eventhandlers;

import dk.gruppe7.common.annotations.EventHandler;
import dk.gruppe7.common.World;

@EventHandler
public interface ActionEventHandler<T> {
    public abstract void call(T event, World world);
}