
package dk.gruppe7.particlesystem;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

public class ParticleSystem extends Entity{
    
    private ArrayList<Particle> particles = new ArrayList<>();
    
    private int particlesPerTick;
    private int particleRadius;
    
    private Entity parent;
    
    private Random r = new Random();
    
    public void update(float deltaTime){
        if(parent != null)setPosition(parent.getPositionCentered());
        for (int i = 0; i < particlesPerTick; i++) {
            Particle p = new Particle();
            p.setPos(getPosition());
            p.setVel(Vector2.randomInRadius(50));
            p.setLifetime(3f);
            p.setRadius(particleRadius);
            particles.add(p);
            
        }
        for(ListIterator<Particle> i = particles.listIterator(); i.hasNext();){
            Particle p = i.next();
            p.update(deltaTime);
            
            if(p.getLifetime() <= 0)i.remove();
        }
    }
    
    public void render(Graphics g){
        for(Particle p : particles){
            g.drawCircle(p.getPos(), p.getRadius(), p.getColor());
        }
    }

    public int getParticlesPerTick() {
        return particlesPerTick;
    }

    public void setParticlesPerTick(int particlesPerTick) {
        this.particlesPerTick = particlesPerTick;
    }

    public int getParticleRadius() {
        return particleRadius;
    }

    public void setParticleRadius(int particleRadius) {
        this.particleRadius = particleRadius;
    }
    
    
    
    
    
}
