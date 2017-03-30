package dk.gruppe7.common.data;

/**
 *
 * @author Mikkel
 */
public interface KeyEventHandler {
    // false -> not active.
    public abstract void call(boolean newKeyState);
}
