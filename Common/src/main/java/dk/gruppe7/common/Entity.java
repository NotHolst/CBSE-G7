package dk.gruppe7.common;

import dk.gruppe7.common.data.EntityType;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.data.Point;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.UUID;

/**
 *
 * @author Holst & Harald
 */
public class Entity {
    private UUID id;
    private EntityType entityType;
    private Vector2 position;
    private float Rotation;
    private Point velocity;
    private float acceleration;
    private float maxVelocity;
    private int health;
    private int maxHealth;
    private Rectangle bounds; //Harald vil gerne have det som en list af bounds, i tilfælde at at hans headset skal være med i spillet.
    private Image texture; //Hvis vi senere stræber efter at have animationer i form af spritesheets, 
                            //ville det være smartere at have ét stor samlet spritesheet for alle entities, 
                            //hvor der peges på rækken i spritesheet. alternativt kunne der benytte en vector til at ngive rækken sammen med antal frames
    private Point roomLocation;
    private float lifeTime;
    private float attackSpeed;
    private UUID owner;
    
    
}
