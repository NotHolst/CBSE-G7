package dk.gruppe7.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.DrawCommand;
import dk.gruppe7.common.graphics.Graphics;
import java.awt.image.BufferedImage;
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
import java.util.Iterator;

/**
 *
 * @author Holst & Harald
 */
public class Game implements ApplicationListener{
    
    private static OrthographicCamera cam;
    private Graphics graphics;
    private SpriteBatch batch;
    
    private final GameData gameData = new GameData();
    private World world = new World();
    
    private final Lookup lookup = Lookup.getDefault();
    private List<IProcess> processors = new CopyOnWriteArrayList<>();
    private List<IRender> renderers = new CopyOnWriteArrayList<>();
    private ResourceManager resourceManager = new ResourceManager();
    
    private Lookup.Result<IProcess> processorsResult;
    private Lookup.Result<IRender> renderersResult;
    
    private final GameInputProcessor inputProcessor = new GameInputProcessor(gameData);
    
    @Override
    public void create() {
        
        gameData.setScreenWidth(Gdx.graphics.getWidth());
        gameData.setScreenHeight(Gdx.graphics.getHeight());
        
        gameData.setResourceManager(resourceManager);
        
        cam = new OrthographicCamera(gameData.getScreenWidth(), gameData.getScreenHeight());
        cam.translate(gameData.getScreenWidth()/2, gameData.getScreenHeight()/2);
        cam.update();
        
        graphics = new Graphics();
        batch = new SpriteBatch();
        
        processorsResult = lookup.lookupResult(IProcess.class);
        processorsResult.addLookupListener(lookupListener);
        processorsResult.allItems();
        
        for(IProcess processor : lookup.lookupAll(IProcess.class)){
            processor.start(gameData, world);
            processors.add(processor);
        }
        
        renderersResult = lookup.lookupResult(IRender.class);
        renderersResult.addLookupListener(lookupListener);
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
        Gdx.gl.glClearColor(.7f, .7f, .75f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        
        gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        
        //update entities
        update();
        //render entities
        batch.enableBlending();
        drawGraphics();

        gameData.incrementTickCount();
    }
    
    public void update(){
        for (IProcess processor : processors) {
            processor.process(gameData, world);
        }
        for (IRender renderer : renderers){
            renderer.render(graphics, world);
        }
    }
    
    public void drawGraphics(){
        
        while(graphics.getDrawCommands().size() > 0){
            DrawCommand cmd = graphics.getDrawCommands().poll();
            switch(cmd.getType()){
                case SPRITE:
                    batch.begin();
                    
                    Texture tex = inputStreamToTexture(cmd.getInputStream());
                    

                    float repeatX = 1;
                    float repeatY = 1;
                    if (cmd.getSpriteRenderType() == DrawCommand.SpriteRenderMode.REPEAT)
                    {
                        tex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                        repeatX = cmd.getSize().x /tex.getWidth();
                        repeatY = cmd.getSize().y/  tex.getHeight(); 
                        //LibGDX will always scale the texture to the bounds,
                        //Forcing this approach where amount of wraps are,
                        //calculated manually
                    }
                    
                    batch.draw(
                            /* Texture   */ tex, 
                            /* X         */ cmd.getPosition().x, 
                            /* Y         */ cmd.getPosition().y, 
                            /* originX   */ cmd.getSize().x/2f, 
                            /* originY   */ cmd.getSize().y/2f,
                            /* width     */ cmd.getSize().x, 
                            /* height    */ cmd.getSize().y, 
                            /* scaleX    */ 1.f, 
                            /* scaleY    */ 1.f, 
                            /* Rotation  */ cmd.getRotation(), 
                            /* srcX      */ 0, 
                            /* srcY      */ 0, 
                            /* srcWidth  */ (int)(tex.getWidth()*repeatX), 
                            /* srcHeight */ (int)(tex.getHeight()*repeatY), 
                            /* flipX     */ false, 
                            /* flipY     */ false
                    );
                    batch.end();
                    break;
            }
        }
       
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
    
    private final LookupListener lookupListener = new LookupListener() {
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
    
    private HashMap<Integer, Texture> cachedTextures = new HashMap<>();
    private Texture inputStreamToTexture(InputStream inputStream) {
        if(cachedTextures.containsKey(inputStream.hashCode()))
            return cachedTextures.get(inputStream.hashCode());
    
        Gdx2DPixmap gpm = null;
        
        try {
            gpm = new Gdx2DPixmap(inputStream, Gdx2DPixmap.GDX2D_FORMAT_RGBA8888);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        Pixmap pixmap = new Pixmap(gpm);
        Texture texture = new Texture(pixmap);
        
        cachedTextures.put(inputStream.hashCode(), texture);
        return texture;
    }
}
