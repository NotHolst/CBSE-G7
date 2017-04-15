package dk.gruppe7.particlesystem;

import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Color;

public class Particle {
    
    private ParticleSystem particleSystem;
    private Vector2 pos;
    private Vector2 vel;
    private float radius;
    private float lifetime;

    public void update(float deltaTime) {
        pos = pos.add(vel.mul(deltaTime));
        lifetime -= deltaTime;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setVel(Vector2 vel) {
        this.vel = vel;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setLifetime(float lifetime) {
        this.lifetime = lifetime;
    }

    public float getLifetime() {
        return lifetime;
    }
    
    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getVel() {
        return vel;
    }
   
    public float getRadius(){
        return radius;
    }
    
    public Color getColor(){
        return new Color(255, 255, 255, 255);
    }
    
    public void setParticleSystem(ParticleSystem particleSystem){
        this.particleSystem = particleSystem;
    }
    
    
}
