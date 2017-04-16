package dk.gruppe7.common.data;

import dk.gruppe7.common.World;

public interface ActionEventHandler<T> {
    public abstract void call(T event, World world);
}