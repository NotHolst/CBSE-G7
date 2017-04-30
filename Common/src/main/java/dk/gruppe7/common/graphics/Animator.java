/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.graphics;

import dk.gruppe7.common.GameData;
import dk.gruppe7.common.resources.Image;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author haral
 */
public class Animator {
    
    private Animation animation;
    
    private float timer;
    
    public Animator(){
        
    }
    
    @Deprecated
    public Animator(Image[] frames, float interval){
        animation = new Animation(frames, interval);
    }
    
    public void update(GameData gameData){
        if(animation == null) return ;
        timer += gameData.getDeltaTime();
        if(timer >= animation.getInterval()){
            timer = 0;
            incrementFrame();
        }
    }
    public InputStream getTexture(){
        if(animation == null) return null;
        return animation.getFrame();
    }

    private void incrementFrame() {
        animation.incrementFrame();
        if(animation.getCurrentFrame() > animation.length() - 1){
            animation.setCurrentFrame(0);
        }
    }

    public float getInterval() {
        return animation.getInterval();
    }

    public void setInterval(float interval) {
        if(animation == null) return;
        animation.setInterval(interval);
    }
    
    public void play(Animation animation){
        this.animation = animation;
    }
    
    
}
