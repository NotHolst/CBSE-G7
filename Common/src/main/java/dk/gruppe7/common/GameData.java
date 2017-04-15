package dk.gruppe7.common;

import dk.gruppe7.common.resources.ResourceManager;

/**
 *
 * @author Holst & Harald
 */
public class GameData {
    private int screenWidth;
    private int screenHeight;
    private long tickCount = 0;
    private float deltaTime;
    private final Input input = new Input();
    private ResourceManager resourceManager;

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
    
    public void incrementTickCount() {
        this.tickCount++;
    }
    
    public long getTickCount() {
        return this.tickCount;
    }

    public Input getInput() {
        return input;
    }

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }
    
}