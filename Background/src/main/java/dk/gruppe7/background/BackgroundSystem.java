/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.background;

import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.levelcommon.LevelData;
import dk.gruppe7.levelcommon.LevelEvent;
import java.util.ListIterator;
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
    
    @Override
    public void start(GameData gameData, World world) {
        tiles = new Image[]{
            gameData.getResourceManager().addImage("tile0", getClass().getResourceAsStream("tile0.png")),
            gameData.getResourceManager().addImage("tile1", getClass().getResourceAsStream("tile1.png")),
            gameData.getResourceManager().addImage("tile2", getClass().getResourceAsStream("tile2.png")),
            gameData.getResourceManager().addImage("tile3", getClass().getResourceAsStream("tile3.png")),
            gameData.getResourceManager().addImage("tile4", getClass().getResourceAsStream("tile4.png")),
            gameData.getResourceManager().addImage("tile5", getClass().getResourceAsStream("tile5.png")),
            gameData.getResourceManager().addImage("tile6", getClass().getResourceAsStream("tile6.png")),
            gameData.getResourceManager().addImage("tile7", getClass().getResourceAsStream("tile7.png"))
        };
        
        screenHeight = gameData.getScreenHeight();
        screenWidth = gameData.getScreenWidth();
        
    }

    @Override
    public void stop(GameData gameData, World world) {
    }

    @Override
    public void process(GameData gameData, World world) {
        for(ListIterator<LevelEvent> i = LevelData.getEvents().listIterator(); i.hasNext();){
            LevelEvent lvlEvent = i.next();
            currentSeed = new Random().nextInt();
            i.remove();
        }
    }

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
