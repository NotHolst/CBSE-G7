package dk.gruppe7.common.data;

public interface ActionEventHandler<T> {
    public abstract void call(T event);
}
