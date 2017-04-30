package dk.gruppe7.common.graphics;

import dk.gruppe7.common.resources.Image;
import java.io.InputStream;

public class Animation {
    private Image[] frames;
    private float interval;
    private int currentFrame;

    public Animation(Image[] frames, float interval) {
        this.frames = frames;
        this.interval = interval;
    }

    public Image[] getFrames() {
        return frames;
    }

    public float getInterval() {
        return interval;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }
    public void incrementFrame(){
        this.currentFrame++;
    }
    public int length(){
        return frames.length;
    }
    public InputStream getFrame(){
        return frames[currentFrame].getInputStream();
    }
}
