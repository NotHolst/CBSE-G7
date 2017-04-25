/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.core;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.io.File;
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
        cfg.useGL30 = false;
        cfg.resizable = false;
        cfg.addIcon("dk/gruppe7/core/icon128.png", Files.FileType.Local);
        cfg.addIcon("dk/gruppe7/core/icon32.png", Files.FileType.Local);
        cfg.addIcon("dk/gruppe7/core/icon16.png", Files.FileType.Local);
        //Needs 3 sizes of icons, because different operating systems will accomodate different sizes (128x128, 32x32, and 16x16.
        
        new LwjglApplication(game, cfg);
    }
}