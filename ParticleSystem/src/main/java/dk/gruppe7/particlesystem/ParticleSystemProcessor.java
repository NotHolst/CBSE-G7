
package dk.gruppe7.particlesystem;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Color;
import dk.gruppe7.common.graphics.Graphics;
import java.util.ListIterator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class)
    , @ServiceProvider(service = IRender.class)
})
public class ParticleSystemProcessor implements IProcess, IRender{
    
    @Override
    public void start(GameData gameData, World world) {
        
        //Add a test particle system
        ParticleSystem ps = new ParticleSystem();
        ps.setPosition(new Vector2(300,300));
        ps.setParticlesPerTick(1);
        ps.setParticleRadius(3);
        world.addEntity(ps);
        
    }

    @Override
    public void stop(GameData gameData, World world) {
        for (ListIterator<Entity> i = world.getEntities().listIterator(); i.hasNext();) {
            Entity e = i.next();
            if(e instanceof ParticleSystem) i.remove();
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        for (ListIterator<Entity> i = world.getEntities().listIterator(); i.hasNext();) {
            Entity e = i.next();
            if(!(e instanceof ParticleSystem)) continue;
            ParticleSystem ps = (ParticleSystem) e;
            
            ps.update(gameData.getDeltaTime());
        }
    }

    @Override
    public void render(Graphics g, World world) {
        for(Entity e : world.getEntities()){
            if(!(e instanceof ParticleSystem)) continue;
            ((ParticleSystem)e).render(g);
        }
    }

}
