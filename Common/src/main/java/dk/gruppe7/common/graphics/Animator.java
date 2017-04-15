/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.graphics;

import dk.gruppe7.common.GameData;
import dk.gruppe7.common.resources.Image;
import java.io.InputStream;

/**
 *
 * @author haral
 */
public class Animator {
    private Image[] frames;
    private float interval;
    
    private float timer;
    private int currentFrame;
    
    public Animator(Image[] frames, float interval){
        this.frames = frames;
        this.interval = interval;
    }
    
    public void update(GameData gameData){
        timer += gameData.getDeltaTime();
        if(timer >= interval){
            timer = 0;
            incrementFrame();
        }
    }
    public InputStream getTexture(){
        return frames[currentFrame].getInputStream();
    }

    private void incrementFrame() {
        currentFrame++;
        if(currentFrame > frames.length - 1){
            currentFrame = 0;
        }
    }

    public float getInterval() {
        return interval;
    }

    public void setInterval(float interval) {
        this.interval = interval;
    }
    
    
}
