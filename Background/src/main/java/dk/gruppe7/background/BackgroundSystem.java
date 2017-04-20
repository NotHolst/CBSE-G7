/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.background;

import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Audio;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author haral
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class)
    , @ServiceProvider(service = IRender.class)
})

public class BackgroundSystem implements IProcess, IRender{
    
    Image[] tiles;
    int currentSeed;
    int screenWidth, screenHeight;
    Audio backgroundSound;
    
    @Override
    public void start(GameData gameData, World world) {
        tiles = new Image[]{
            gameData.getResourceManager().addImage("tile0", getClass().getResourceAsStream("Tile0.png")),
            gameData.getResourceManager().addImage("tile1", getClass().getResourceAsStream("Tile1.png")),
            gameData.getResourceManager().addImage("tile2", getClass().getResourceAsStream("Tile2.png")),
            gameData.getResourceManager().addImage("tile3", getClass().getResourceAsStream("Tile3.png")),
            gameData.getResourceManager().addImage("tile4", getClass().getResourceAsStream("Tile4.png")),
            gameData.getResourceManager().addImage("tile5", getClass().getResourceAsStream("Tile5.png")),
            gameData.getResourceManager().addImage("tile6", getClass().getResourceAsStream("Tile6.png")),
            gameData.getResourceManager().addImage("tile7", getClass().getResourceAsStream("Tile7.png"))
        };
        
        screenHeight = gameData.getScreenHeight();
        screenWidth = gameData.getScreenWidth();
        
        backgroundSound = gameData.getResourceManager().addAudio("backgroundSound", getClass().getResourceAsStream("background.wav"));
        gameData.getAudioPlayer().play(backgroundSound, .65f);
        
        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
    }

    @Override
    public void process(GameData gameData, World world) {
    }
    
    ActionEventHandler<RoomChangedEvent> roomChangedHandler = (RoomChangedEvent event, World world) -> {
        currentSeed = new Random().nextInt();
    };

    @Override
    public void render(Graphics g, World world) {
        Random r = new Random(currentSeed);
        int padding = 2;
        int tileSize = 32;
        int tilesX = screenWidth/(tileSize+padding);
        int tilesY = screenHeight/(tileSize+padding);
        
        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {
                g.drawSprite(
                        new Vector2(x*(tileSize+padding), y*(tileSize+padding)),
                        new Vector2(tileSize, tileSize),
                        tiles[r.nextInt(tiles.length)].getInputStream(),
                        0,
                        -9001
                );
                
            }
            
        }
    }
    
}
