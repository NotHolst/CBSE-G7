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
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
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

/**
 *
 * @author Holst & Harald
 */
public class Game implements ApplicationListener{
    
    private static OrthographicCamera cam;
    private ShapeRenderer sr;
    private SpriteBatch batch;
    
    private final GameData gameData = new GameData();
    private World world = new World();
    
    private final Lookup lookup = Lookup.getDefault();
    private List<IProcess> processors = new CopyOnWriteArrayList<>();
    private Lookup.Result<IProcess> result;
    
    private final GameInputProcessor inputProcessor = new GameInputProcessor(gameData);
    
    @Override
    public void create() {
        
        gameData.setScreenWidth(Gdx.graphics.getWidth());
        gameData.setScreenHeight(Gdx.graphics.getHeight());
        
        cam = new OrthographicCamera(gameData.getScreenWidth(), gameData.getScreenHeight());
        cam.translate(gameData.getScreenWidth()/2, gameData.getScreenHeight()/2);
        cam.update();
        
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        
        result = lookup.lookupResult(IProcess.class);
        result.addLookupListener(lookupListener);
        result.allItems();
        
        for(IProcess processor : lookup.lookupAll(IProcess.class)){
            processor.start(gameData, world);
            processors.add(processor);
        }
        
        inputProcessor.start();
    }

    @Override
    public void resize(int i, int i1) {
        //TODO:
    }

    InputStream defaultInputStream = getClass().getResourceAsStream("default.png");
    
    @Override
    public void render() {
        //Clear screen before drawing
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        
        gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        
        //update entities
        update();
        
        //draw entites
        for (IProcess processor : processors) {
            //vi mangler spritebatch osv.
        }
        
        for (Entity entity : world.getEntities())
        {
            //sr.begin(ShapeRenderer.ShapeType.Filled);
            //sr.circle(entity.getPosition().x, entity.getPosition().y, 5);
            //sr.end();
            
            batch.enableBlending();
            batch.begin();
            
            InputStream temp = (entity.getInputStream() == null) ? defaultInputStream : entity.getInputStream();
            
            batch.draw(
                    /* Texture   */ inputStreamToTexture(temp), 
                    /* X         */ entity.getPosition().x, 
                    /* Y         */ entity.getPosition().y, 
                    /* originX   */ 0.f, 
                    /* originY   */ 0.f,
                    /* width     */ 64, 
                    /* height    */ 64, 
                    /* scaleX    */ 1.f, 
                    /* scaleY    */ 1.f, 
                    /* Rotation  */ 0.f, 
                    /* srcX      */ 0, 
                    /* srcY      */ 0, 
                    /* srcWidth  */ inputStreamToTexture(temp).getWidth(), 
                    /* srcHeight */ inputStreamToTexture(temp).getHeight(), 
                    /* flipX     */ false, 
                    /* flipY     */ false
            );
            
            batch.end();
        }
    }
    
    public void update(){
        for (IProcess processor : processors) {
            processor.process(gameData, world);
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

            Collection<? extends IProcess> updated = result.allInstances();

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
    
    // Taget fra PlayerSystem
    private int booleanToInt(boolean b) {
        return (b) ? 1 : 0;
    }
}
