package dk.gruppe7.common.data;

import dk.gruppe7.common.EventHandler;
import dk.gruppe7.common.World;

@EventHandler
public interface ActionEventHandler<T> {
    public abstract void call(T event, World world);
}