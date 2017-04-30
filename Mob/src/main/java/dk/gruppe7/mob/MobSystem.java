/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.mob;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.eventtypes.DisposeEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.common.utils.RandomUtil;
import dk.gruppe7.damagecommon.DamageEvent;
import dk.gruppe7.data.MobType;
import static dk.gruppe7.data.MobType.RANGED;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import dk.gruppe7.mobcommon.Mob;
import dk.gruppe7.mobcommon.MobEvent;
import dk.gruppe7.mobcommon.MobEventType;
import static dk.gruppe7.mobcommon.MobEventType.SPAWN;
import dk.gruppe7.obstaclecommon.Obstacle;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.weaponcommon.WeaponEvent;
import java.util.ArrayList;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author benjaminmlynek
 */
@ServiceProvider(service = IProcess.class)
public class MobSystem implements IProcess, IRender {

    ArrayList<Mob> listOfMobsToBeRemoved = new ArrayList<>();

    Image textureSkeletonRanged;
    Image textureSkeletonMelee;
    Image textureKnightRanged;
    Image health;
    Image[] framesSkeleton;
    Image[] framesKnight;

    private int screenHeight,screenWidth;
    private int difficulty;
    
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
        
        Dispatcher.subscribe(this);
        
        screenWidth = gameData.getScreenWidth();
        screenHeight = gameData.getScreenHeight();
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
        world.removeEntities(world.<Mob>getEntitiesByClass(Mob.class));
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Mob mob : world.<Mob>getEntitiesByClass(Mob.class)) {
            if (mob.getVelocity().len() > .1f) {
                mob.getAnimator().setInterval(15 * 1.0f / mob.getVelocity().len());
                mob.getAnimator().update(gameData);
            }

            if (mob.getTarget() != null) {
                //The mob has a target, follow it
                Vector2 newVel = mob.getTarget().getPositionCentered()
                        .sub(mob.getPositionCentered())
                        .normalize().mul(mob.getMaxVelocity());
                //if the player is within the mobs attack range, stop moving
                if (mob.getPositionCentered().distance(mob.getTarget().getPositionCentered()) < mob.getAttackRange()) {
                    newVel = Vector2.zero;
                }
                mob.setVelocity(newVel);
                mob.setRotation(mob.getPositionCentered().getAngleTowards(mob.getTarget().getPositionCentered()));

                //shoot if they feel like it
                if (Math.random() < 0.1f) {
                    Dispatcher.post(new WeaponEvent(mob.getId()), world);
                }

            } else {
                Player p = world.<Player>getEntitiesByClass(Player.class).get(0);
                if (p != null) {
                    //set the target of the mob as the player entity
                    mob.setTarget(p);
                }

            }

            mob.setPosition(mob.getPosition().add(mob.getVelocity().mul(gameData.getDeltaTime())));

            if (mob.getHealthData().getHealth() <= 0) {
                Dispatcher.post(new MobEvent(mob, MobEventType.DEATH), world);
            }
        }
        if(! (world.getCurrentLevel() > difficulty)){
        difficulty = world.getCurrentLevel();
        }
    }

    ActionEventHandler<DamageEvent> damageHandler = (event, world) -> {
        if (world.isEntityOfClass(event.getTarget(), Mob.class))
            world.<Mob>getEntityByID(event.getTarget()).getHealthData().decreaseHealth(event.getDamageDealt().getDamage());
    };
    
    ActionEventHandler<CollisionEvent> obstacleCollisionHandler = (event, world) -> {
        if(world.getEntityByID(event.getTargetID()) instanceof Mob
        && world.getEntityByID(event.getOtherID()) instanceof Obstacle){
            Entity targetEntity = world.getEntityByID(event.getTargetID());
            Entity otherEntity = world.getEntityByID(event.getOtherID());

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
                            
            targetEntity.setVelocity(Vector2.zero);
        }
    };
    
    ActionEventHandler<RoomChangedEvent> roomChangeHandler = (event, world) ->{
        if(event.getRoom().isCleared()) return;
        spawnMobs(world);
    };
    
    ActionEventHandler<MobEvent> mobEventHandler = (event, world) -> {
        if(event.getType().equals(MobEventType.DEATH)) {
            listOfMobsToBeRemoved.add(event.getMob());
        }
    };
    
    ActionEventHandler<DisposeEvent> disposalHandler = (event, world) -> {
        world.removeEntities(listOfMobsToBeRemoved);
        listOfMobsToBeRemoved.clear();
    };
    
    private void spawnMobs(World world){
        for (int i = 0; i < RandomUtil.GetRandomInteger(2 + (difficulty/3), 7 + (difficulty/2)); i++) {
            Entity mob = createMob(
            new Vector2(
                    RandomUtil.GetRandomInteger(100, screenWidth-100), 
                    RandomUtil.GetRandomInteger(100, screenHeight-200)), 
            MobType.getRandom()
            );
            world.addEntity(mob);
            Dispatcher.post(new MobEvent((Mob) mob, SPAWN), world);
        }
    }

    private Entity createMob(Vector2 position, MobType type) {
        Mob mob = new Mob();
        mob.setPosition(new Vector2(position.x, position.y));
        mob.setMobType(type);
        mob.setMaxVelocity(1.f);
        mob.setAcceleration(80.f);
        mob.setCollidable(true);
        mob.setBounds(new Rectangle(64, 64));
        mob.setAnimator(new Animator(framesSkeleton, 1.f));
        mob.setMaxVelocity(250.f * 1+(difficulty/80));
        mob.setAcceleration(90);
        mob.setAttackRange((type == RANGED)?600:50);
        System.out.println(type);
        return mob;
    }

    @Override
    public void render(Graphics g, World world) {
        for (Mob mob : world.<Mob>getEntitiesByClass(Mob.class)) {
            Image texture;
            //Temporary, until we can distinguish between skeletons and knights.
            texture = textureSkeletonRanged;

            g.drawSprite(
                    /* Position    */mob.getPosition(),
                    /* Size        */ new Vector2(mob.getBounds().getWidth(), mob.getBounds().getHeight()),
                    /* InputStream */ texture.getInputStream(),
                    /* Rotation    */ mob.getRotation(),
                    /* LayerHeight */ 2
            );
            g.drawSprite(
                    /* Position    */mob.getPosition(),
                    /* Size        */ new Vector2(mob.getBounds().getWidth(), mob.getBounds().getHeight()),
                    /* InputStream */ mob.getAnimator().getTexture(),
                    /* Rotation    */ (float) Math.toDegrees(Math.atan2(mob.getVelocity().y, mob.getVelocity().x)),
                    /* LayerHeight */ 1
            );

            g.drawSprite(mob.getPosition().add(0, -2), new Vector2(64 * mob.getHealthData().getHealth() / mob.getHealthData().getStartHealth(), 5), health.getInputStream(), 0, 3);
        }
    }
}