/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.common.audio;

import dk.gruppe7.common.resources.Audio;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author haral
 */
public class AudioPlayer {

    public void play(Audio audio){
        play(audio, 1);
    }
    //StackOverflow
    public void play(Audio audio, float volume) {
        try {
            Clip clip = audio.getAudioClip();
            if(clip.isRunning()){
                clip.stop();
            }
            clip.setMicrosecondPosition(0);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
            clip.start();
            
        } catch (Exception exc) {
            exc.printStackTrace(System.out);
        }
    }

}
