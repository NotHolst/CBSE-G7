/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mob;

import collision.CollisionData;
import collision.CollisionEvent;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.data.MobType;
import static dk.gruppe7.data.MobType.DEFENDER;
import static dk.gruppe7.data.MobType.MELEE;
import static dk.gruppe7.data.MobType.RANGED;
import static dk.gruppe7.data.MobType.SUPPORT;
import dk.gruppe7.mobcommon.Mob;
import dk.gruppe7.mobcommon.MobData;
import dk.gruppe7.mobcommon.MobEvent;
import dk.gruppe7.mobcommon.MobEventType;
import static dk.gruppe7.mobcommon.MobEventType.SPAWN;
import dk.gruppe7.mobcommon.MobID;
import dk.gruppe7.obstaclecommon.Obstacle;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.weaponcommon.WeaponData;
import dk.gruppe7.weaponcommon.WeaponEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author benjaminmlynek
 */
@ServiceProvider(service = IProcess.class)
public class MobSystem implements IProcess, IRender {

    UUID mobID;

    Image textureSkeletonRanged;
    Image textureSkeletonMelee;
    Image textureKnightRanged;
    Image health;
    Image[] framesSkeleton;
    Image[] framesKnight;
    boolean up, down, left, right;
    Vector2 aimDirection = Vector2.zero;
    List<WeaponEvent> weaponEvents = WeaponData.getEvents();
    float dTimer, dTime;

    @Override
    public void start(GameData gameData, World world) {

        framesSkeleton = new Image[]{
            gameData.getResourceManager().addImage("frameSkeleton0", getClass().getResourceAsStream("SkeletonFeet0.png")),
            gameData.getResourceManager().addImage("frameSkeleton1", getClass().getResourceAsStream("SkeletonFeet1.png")),
            gameData.getResourceManager().addImage("frameSkeleton2", getClass().getResourceAsStream("SkeletonFeet2.png")),
            gameData.getResourceManager().addImage("frameSkeleton3", getClass().getResourceAsStream("SkeletonFeet3.png")),
            gameData.getResourceManager().addImage("frameSkeleton4", getClass().getResourceAsStream("SkeletonFeet4.png")),
            gameData.getResourceManager().addImage("frameSkeleton5", getClass().getResourceAsStream("SkeletonFeet5.png")),
            gameData.getResourceManager().addImage("frameSkeleton6", getClass().getResourceAsStream("SkeletonFeet6.png")),
            gameData.getResourceManager().addImage("frameSkeleton7", getClass().getResourceAsStream("SkeletonFeet7.png")),
            gameData.getResourceManager().addImage("frameSkeleton8", getClass().getResourceAsStream("SkeletonFeet8.png")),
            gameData.getResourceManager().addImage("frameSkeleton9", getClass().getResourceAsStream("SkeletonFeet9.png")),
            gameData.getResourceManager().addImage("frameSkeleton10", getClass().getResourceAsStream("SkeletonFeet10.png")),
            gameData.getResourceManager().addImage("frameSkeleton11", getClass().getResourceAsStream("SkeletonFeet11.png")),
            gameData.getResourceManager().addImage("frameSkeleton12", getClass().getResourceAsStream("SkeletonFeet12.png"))

        };
        framesKnight = new Image[]{
            gameData.getResourceManager().addImage("frameKnight0", getClass().getResourceAsStream("KnightFeet0.png")),
            gameData.getResourceManager().addImage("frameKnight1", getClass().getResourceAsStream("KnightFeet1.png")),
            gameData.getResourceManager().addImage("frameKnight2", getClass().getResourceAsStream("KnightFeet2.png")),
            gameData.getResourceManager().addImage("frameKnight3", getClass().getResourceAsStream("KnightFeet3.png")),
            gameData.getResourceManager().addImage("frameKnight4", getClass().getResourceAsStream("KnightFeet4.png")),
            gameData.getResourceManager().addImage("frameKnight5", getClass().getResourceAsStream("KnightFeet5.png")),
            gameData.getResourceManager().addImage("frameKnight6", getClass().getResourceAsStream("KnightFeet6.png")),
            gameData.getResourceManager().addImage("frameKnight7", getClass().getResourceAsStream("KnightFeet7.png")),
            gameData.getResourceManager().addImage("frameKnight8", getClass().getResourceAsStream("KnightFeet8.png")),
            gameData.getResourceManager().addImage("frameKnight9", getClass().getResourceAsStream("KnightFeet9.png")),
            gameData.getResourceManager().addImage("frameKnight10", getClass().getResourceAsStream("KnightFeet10.png")),
            gameData.getResourceManager().addImage("frameKnight11", getClass().getResourceAsStream("KnightFeet11.png")),
            gameData.getResourceManager().addImage("frameKnight12", getClass().getResourceAsStream("KnightFeet12.png"))

        };

        textureSkeletonRanged = gameData.getResourceManager().addImage("torso", getClass().getResourceAsStream("SkeletonRanged.png"));
        textureSkeletonMelee = gameData.getResourceManager().addImage("torso", getClass().getResourceAsStream("SkeletonMelee.png"));
        textureKnightRanged = gameData.getResourceManager().addImage("torso", getClass().getResourceAsStream("KnightRanged.png"));
        health = gameData.getResourceManager().addImage("healthBar", getClass().getResourceAsStream("healthGreen.png"));

        Entity mob;
        // Add mobs to the world

        for (int i = 0; i < getRandomNumberBetween(2, 7); i++) {
            mob = createMob(
                    getRandomNumberBetween(0, gameData.getScreenWidth()),
                    getRandomNumberBetween(0, gameData.getScreenHeight()),
                    pickRandomMobType(MobType.class));
            world.addEntity(mob);
            MobData.getEvents().add(new MobEvent((Mob) mob, SPAWN));
        }

    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove mobs from world.
        world.removeEntity(world.getEntityByID(mobID));
        mobID = null;

    }

    @Override
    public void process(GameData gameData, World world) {
        // Need som help with this one. Cant seem to get other than the first mobID.
        ArrayList<Entity> remove = new ArrayList<>();

        Collection<Entity> entities = world.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof Mob) {
                Mob m = (Mob) entity;

                if (m.getVelocity().len() > .1f) {
                    m.getAnimator().setInterval(15 * 1.0f / m.getVelocity().len());
                    m.getAnimator().update(gameData);
                }

                if (m.getWanderTimer() <= 0) {
                    //m.setRotation((GetRandomNumberBetween(0, 100)));
                    m.setVelocity(new Vector2(getRandomNumberBetween(-10, 100), getRandomNumberBetween(-10, 100)));
                    m.setWanderTimer(getRandomNumberBetween(1, 4));
                } else {
                    m.setWanderTimer(m.getWanderTimer() - gameData.getDeltaTime());
                }

                m.setPosition(m.getPosition().add(m.getVelocity().mul(gameData.getDeltaTime())));

                if (m.getHealthData().getHealth() <= 0) {
                    remove.add(m);
                    MobData.getEvents(gameData.getTickCount()).add(new MobEvent(m, MobEventType.DEATH, gameData.getTickCount()));
                }

                // Get Mobs to shoot.
                // Having problems with direction. Mobs shoots themselfes.
                pickDirection(gameData);
                int shootTimer = getRandomNumberBetween(0, 250);
                if (shootTimer == 60 && !aimDirection.equals(Vector2.zero)) {
                    System.out.println("Shooting");
                    m.setRotation((float) Math.toDegrees(Math.atan2(aimDirection.y, aimDirection.x)));
                    weaponEvents.add(new WeaponEvent(m.getId()));
                } else if (m.getVelocity().len() > 50f) {
                    m.setRotation((float) Math.toDegrees(Math.atan2(m.getVelocity().y, m.getVelocity().x)));
                }

                checkCollision(world, gameData, m);
                
            }
            
        }

        for (Entity e : remove) {
            world.removeEntity(e);
        }

    }

    private void checkCollision(World world, GameData gameData, Mob mob) {
        for (ListIterator<CollisionEvent> iterator = CollisionData.getEvents(gameData.getTickCount()).listIterator(); iterator.hasNext();) {
            CollisionEvent tempi = iterator.next();
            
            if(Obstacle.class.isInstance(world.getEntityByID(tempi.getOtherID())) && tempi.getTargetID().equals(mob.getId())) {
                Entity targetEntity = world.getEntityByID(tempi.getTargetID());
                Entity otherEntity = world.getEntityByID(tempi.getOtherID());
                
                float sumY = (targetEntity.getBounds().getWidth() + otherEntity.getBounds().getWidth()) * (targetEntity.getPositionCentered().y - otherEntity.getPositionCentered().y);
                float sumX = (targetEntity.getBounds().getHeight() + otherEntity.getBounds().getHeight()) * (targetEntity.getPositionCentered().x - otherEntity.getPositionCentered().x);
                
                if(sumY > sumX) {
                        if(sumY > -sumX) {
                            targetEntity.setPosition(new Vector2(targetEntity.getPosition().x, otherEntity.getPosition().y + otherEntity.getBounds().getHeight() + 0)); 
                        } else {
                            targetEntity.setPosition(new Vector2((otherEntity.getPosition().x - targetEntity.getBounds().getWidth() - 0), targetEntity.getPosition().y));
                        }
                    } else {
                        if(sumY > -sumX) {
                            targetEntity.setPosition(new Vector2(otherEntity.getPosition().x + otherEntity.getBounds().getWidth() + 0, targetEntity.getPosition().y));
                        } else {
                            targetEntity.setPosition(new Vector2(targetEntity.getPosition().x, otherEntity.getPosition().y - targetEntity.getBounds().getHeight() - 0));
                    }
                }
                
                targetEntity.setVelocity(targetEntity.getVelocity().normalize());
                // Rotate and move away
                targetEntity.setRotation(targetEntity.getRotation() / 2);
                // targetEntity.setVelocity(new Vector2(getRandomNumberBetween(-10, 100), getRandomNumberBetween(-10, 100)));
                iterator.remove();
            }
            else if(Obstacle.class.isInstance(world.getEntityByID(tempi.getTargetID()))) {
                iterator.remove();
            }

            if (tempi.getOtherID().equals(mob.getId())) {
                Entity hitBy = world.getEntityByID(tempi.getTargetID());
                Bullet b = Bullet.class.isInstance(hitBy) ? (Bullet) hitBy : null;
                if (b != null) {
                    mob.getHealthData().setHealth(mob.getHealthData().getHealth() - b.getDamageData().getDamage());
                    //Temporary: to avoid bullets hitting multiple times
                    b.getDamageData().setDamage(0);
                }

            }
        }
    }

    private Entity createMob(float x, float y, MobType type) {
        Mob mob = new Mob();
        MobID.setMobID(mobID = mob.getId());
        mob.setPosition(new Vector2(x, y));
        mob.setMobType(type);
        mob.setMaxVelocity(300.f);
        mob.setAcceleration(80.f);
        mob.setCollidable(true);
        mob.setBounds(new Rectangle(64, 64));
        mob.setAnimator(new Animator(framesSkeleton, 1.f));

        // Create different types of mobs with different behavior
        if (type == SUPPORT) {
            // TO DO
            System.out.println("Support Mob " + mobID);
            mob.setMaxVelocity(250.f);
            mob.setAcceleration(90);
        }

        if (type == DEFENDER) {
            // TO DO
            System.out.println("Defender Mob " + mobID);
            mob.setMaxVelocity(200.f);
            mob.setAcceleration(70);
        }

        if (type == RANGED) {
            // TO DO
            System.out.println("Ranged Mob " + mobID);
            mob.setMaxVelocity(150.f);
            mob.setAcceleration(50);
        }

        if (type == MELEE) {
            // TO DO
            System.out.println("Melee Mob " + mobID);
            mob.setMaxVelocity(100.f);
            mob.setAcceleration(30);
        }

        return mob;
    }

    private int getRandomNumberBetween(int start, int end) {
        Random r = new Random();
        return start + r.nextInt(end - start + 1);
    }

    private float GetRandomNumber(float range) {
        Random r = new Random();
        return r.nextFloat() * range;
    }

    private static <T extends Enum<?>> T pickRandomMobType(Class<T> mobType) {
        Random r = new Random();
        int pick = r.nextInt(mobType.getEnumConstants().length);
        return mobType.getEnumConstants()[pick];
    }
    
    private void pickDirection(GameData g) {
        
        dTime = 3;
        Random r = new Random();
        
        if (dTimer > dTime || !up && !down && !left && !right) {
            up = false;
            down = false;
            left = false;
            right = false;
            dTimer = 0;
            
            int direction = r.nextInt(3);

            switch (direction) {
                case 0:
                    up = true;
                    System.out.println("Direction UP");
                case 1:
                    down = true;
                    System.out.println("Direction DOWN");
                case 2:
                    left = true;
                    System.out.println("Direction LEFT");
                case 3:
                    right = true;
                    System.out.println("Direction RIGHT");
            }
        } else {
            
            dTimer += g.getDeltaTime();

            if (up == true) {
                aimDirection = Vector2.up;
            } else if (aimDirection.equals(Vector2.up)) {
                aimDirection = Vector2.zero;
            }

            if (down == true) {
                aimDirection = Vector2.down;
            } else if (aimDirection.equals(Vector2.down)) {
                aimDirection = Vector2.zero;
            }

            if (left == true) {
                aimDirection = Vector2.left;
            } else if (aimDirection.equals(Vector2.left)) {
                aimDirection = Vector2.zero;
            }

            if (right == true) {
                aimDirection = Vector2.right;
            } else if (aimDirection.equals(Vector2.right)) {
                aimDirection = Vector2.zero;
            }
        }

      
    }
    
    private void checkNewRoom() {
        
    }

    @Override
    public void render(Graphics g, World world) {
        for (Entity e : world.getEntities()) {
            if (e instanceof Mob) {
                Mob mob = (Mob) e;
                Image texture;
                //Temporary, until we can distinguish between skeletons and knights.
                texture = textureSkeletonRanged;

                /*switch (mob.getMobType()){
                    case MELEE:
                        texture = textureSkeletonMelee;
                        break;
                        
                    default:
                        //Random selection of texture for the ranged mobs.
                        Random r = new Random(mob.getId().hashCode());
                        if(r.nextDouble() >= 0.51){
                            texture = textureSkeletonRanged;
                        }
                        else texture = textureKnightRanged;
                        break;
                }*/
                g.drawSprite(
                        /* Position    */e.getPosition(),
                        /* Size        */ new Vector2(e.getBounds().getWidth(), e.getBounds().getHeight()),
                        /* InputStream */ texture.getInputStream(),
                        /* Rotation    */ e.getRotation()
                );
                g.drawSprite(
                        /* Position    */e.getPosition(),
                        /* Size        */ new Vector2(e.getBounds().getWidth(), e.getBounds().getHeight()),
                        /* InputStream */ mob.getAnimator().getTexture(),
                        /* Rotation    */ (float) Math.toDegrees(Math.atan2(e.getVelocity().y, e.getVelocity().x))
                );
                g.drawSprite(e.getPosition().add(0, -2), new Vector2(64 * ((Mob) e).getHealthData().getHealth() / ((Mob) e).getHealthData().getStartHealth(), 5), health.getInputStream(), 0);
            }

        }
    }

}
