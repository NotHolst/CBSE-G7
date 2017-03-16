package dk.gruppe7.common;

/**
 *
 * @author Holst & Harald
 */
public interface IProcess {
    public void start(GameData gameData, World world);
    public void stop(GameData gameData, World world);
    public void process(GameData gameData, World world);
}
