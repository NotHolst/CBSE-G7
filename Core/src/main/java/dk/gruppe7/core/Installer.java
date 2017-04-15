/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.core;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    private static Game game;
    
    @Override
    public void restored() {
        
        game = new Game();
        
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "CBSE G7";
        cfg.width = 1280;
        cfg.height = 720;
        cfg.x = -1; 
        cfg.y = -1; 
        cfg.useGL30 = true;
        cfg.resizable = false;
        cfg.samples = 4;
        
        new LwjglApplication(game, cfg);
    }
}