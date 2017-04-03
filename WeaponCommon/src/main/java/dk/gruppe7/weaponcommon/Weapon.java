/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weaponcommon;

import dk.gruppe7.common.Entity;
import dk.gruppe7.common.data.Vector2;
import java.io.InputStream;
import java.util.UUID;

/**
 *
 * @author haral
 */
public class Weapon extends Entity {
    private UUID owner;
    private int barrelRadius;
    private InputStream texture;
    private Vector2 ownerOffset = Vector2.zero;
    private Vector2 barrelOffset = Vector2.zero;
    private float fireRate;
    private float cooldown;

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getBarrelRadius() {
        return barrelRadius;
    }

    public void setBarrelRadius(int barrelRadius) {
        this.barrelRadius = barrelRadius;
    }

    public InputStream getTexture() {
        return texture;
    }

    public void setTexture(InputStream texture) {
        this.texture = texture;
    }

    public Vector2 getOwnerOffset() {
        return ownerOffset;
    }

    public void setOwnerOffset(Vector2 ownerOffset) {
        this.ownerOffset = ownerOffset;
    }

    public Vector2 getBarrelOffset() {
        return barrelOffset;
    }

    public void setBarrelOffset(Vector2 barrelOffset) {
        this.barrelOffset = barrelOffset;
    }

    public float getFireRate() {
        return fireRate;
    }

    public void setFireRate(float fireRate) {
        this.fireRate = fireRate;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }
    
}
