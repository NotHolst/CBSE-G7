package dk.gruppe7.core;

import dk.gruppe7.common.GameInputProcessor;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.gruppe7.common.eventtypes.DisposeEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.audio.AudioPlayer;
import dk.gruppe7.common.graphics.DrawCommand;
import dk.gruppe7.common.graphics.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import dk.gruppe7.common.resources.ResourceManager;

/**
 *
 * @author Holst & Harald
 */
public class Game implements ApplicationListener{
    
    private static OrthographicCamera cam;
    private Graphics graphics;

    
    private final GameData gameData = new GameData();
    private World world = new World();
    
    private final Lookup lookup = Lookup.getDefault();
    private List<IProcess> processors = new CopyOnWriteArrayList<>();
    private List<IRender> renderers = new CopyOnWriteArrayList<>();
    private ResourceManager resourceManager = new ResourceManager();
    
    private LibGDXGraphicsInterpreter interpreter;
    
    
    private Lookup.Result<IProcess> processorsResult;
    private Lookup.Result<IRender> renderersResult;
    
    private final GameInputProcessor inputProcessor = new GameInputProcessor(gameData);
    
    private AudioPlayer audioPlayer = new AudioPlayer();
    
    @Override
    public void create() {

        
        interpreter  = new LibGDXGraphicsInterpreter();
        
        gameData.setScreenWidth(Gdx.graphics.getWidth());
        gameData.setScreenHeight(Gdx.graphics.getHeight());
        
        gameData.setResourceManager(resourceManager);
        gameData.setAudioPlayer(audioPlayer);
        
        cam = new OrthographicCamera(gameData.getScreenWidth(), gameData.getScreenHeight());
        cam.translate(gameData.getScreenWidth()/2, gameData.getScreenHeight()/2);
        cam.update();
        
        graphics = new Graphics();

        
        processorsResult = lookup.lookupResult(IProcess.class);
        processorsResult.addLookupListener(lookupIProcessListener);
        processorsResult.allItems();
        
        for(IProcess processor : lookup.lookupAll(IProcess.class)){
            processor.start(gameData, world);
            processors.add(processor);
        }
        
        renderersResult = lookup.lookupResult(IRender.class);
        renderersResult.addLookupListener(lookupIRenderListener);
        renderersResult.allItems();
        
        for(IRender renderer : lookup.lookupAll(IRender.class)){
            renderers.add(renderer);
        }

        inputProcessor.start();
    }

    @Override
    public void resize(int i, int i1) {
        //TODO:
    }

    @Override
    public void render() {
        //Clear screen before drawing
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        
        gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        
        //update entities
        update();
        //render entities
        interpreter.render(graphics);
    }
    
    public void update(){
        for (IProcess processor : processors) {
            processor.process(gameData, world);
        }
        
        for (IRender renderer : renderers){
            renderer.render(graphics, world);
        }
        
        Dispatcher.post(new DisposeEvent(), world);
    }
    
    public void drawGraphics(){
        
        
       
    }

    @Override
    public void pause() {
        //TODO:
    }

    @Override
    public void resume() {
        //TODO:
    }

    @Override
    public void dispose() {
        //TODO:
    }
    
    private final LookupListener lookupIProcessListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IProcess> updated = processorsResult.allInstances();

            for (IProcess us : updated) {
                // Newly installed modules
                if (!processors.contains(us)) {
                    us.start(gameData, world);
                    processors.add(us);
                }
            }
            
            // Stop and remove module
            for (IProcess gs : processors) {
                if (!updated.contains(gs)) {
                    gs.stop(gameData, world);
                    processors.remove(gs);
                }
            }
        }
    };
    
    private final LookupListener lookupIRenderListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IRender> updated = renderersResult.allInstances();

            for (IRender us : updated) {
                // Newly installed modules
                if(!renderers.contains(us)) {
                    renderers.add(us);
                }
            }
            
            // Stop and remove module
            for(IRender gs : renderers) {
                if(!updated.contains(gs)) {
                    renderers.remove(gs);
                }
            }
        }
    };
}
