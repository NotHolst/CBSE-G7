package dk.gruppe7.common;

/**
 *
 * @author Holst & Harald
 */
public class GameData {
    private int screenWidth;
    private int screenHeight;
    private float deltaTime;
    private final Input input = new Input();

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public Input getInput() {
        return input;
    }

    
    
}
