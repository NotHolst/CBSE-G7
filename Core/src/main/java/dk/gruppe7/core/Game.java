package dk.gruppe7.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.World;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

    @Override
    public void render() {
        //Clear screen before drawing
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        gameData.setDeltaTime(Gdx.graphics.getDeltaTime());
        
        //update entities
        update();
        
        //draw entites
        for (IProcess processor : processors) {
            //vi mangler spritebatch osv.
        }
        
        for (Entity entity : world.getEntities())
        {
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.circle(entity.getPosition().x, entity.getPosition().y, 5);
            sr.end();
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
    
}
