/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weapon;

import collision.CollisionData;
import static collision.CollisionData.getEvents;
import collision.CollisionEvent;
import dk.gruppe7.common.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Audio;
import static dk.gruppe7.data.MobType.MELEE;
import dk.gruppe7.mobcommon.Mob;
import dk.gruppe7.mobcommon.MobData;
import dk.gruppe7.mobcommon.MobEvent;
import dk.gruppe7.mobcommon.MobEventType;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.weaponcommon.Weapon;
import dk.gruppe7.weaponcommon.WeaponData;
import dk.gruppe7.weaponcommon.WeaponEvent;
import dk.gruppe7.weaponcommon.WeaponType;
import static dk.gruppe7.weaponcommon.WeaponType.CROSSBOW;
import static dk.gruppe7.weaponcommon.WeaponType.MACE;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class)
    , @ServiceProvider(service = IRender.class)
})
/**
 *
 * @author haral
 */
public class WeaponSystem implements IProcess, IRender {

    InputStream textureMace = getClass().getResourceAsStream("Mace.png");
    InputStream textureCrossbow = getClass().getResourceAsStream("Crossbow.png");
    List<ShootingEvent> sEvents = ShootingData.getEvents();
    List<WeaponEvent> wEvents = WeaponData.getEvents();
    Audio crossbowSound;

    @Override
    public void start(GameData gameData, World world) {
        //Standard weapon for the Player
        Weapon addedWeapon = generateWeapon(CROSSBOW);
        world.addEntity(addedWeapon);
        //addedWeapon.setOwner(world.getEntitiesByClass(Player.class).get(0).getId());
        
        crossbowSound = gameData.getResourceManager().addAudio("crossbowSound", getClass().getResourceAsStream("bow.wav"));

    }

    @Override
    public void stop(GameData gameData, World world) {

    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity e : world.getEntities()) {
            if (!(e instanceof Weapon)) {
                continue;
            }
            Boolean shoot = false;
            Weapon weaponEntity = (Weapon) e;
            Entity owner = null;
            if (weaponEntity.getOwner() != null) {
                owner = world.getEntityByID(weaponEntity.getOwner());
            }

            //else {
            //    for (Entity e2 : world.getEntities()) {
            //        if (e2 instanceof Player) {
            //            weaponEntity.setOwner(e2.getId());
            //            break;
            //        }
            //    }
            //}
            if (owner != null) {
                //Vector2 offset = new Vector2((float)Math.cos(Math.toRadians(weaponEntity.getRotation()-20)), (float)Math.sin(Math.toRadians(weaponEntity.getRotation()-20)));
                weaponEntity.setPositionCentered(
                        owner.getPositionCentered()
                                .add(weaponEntity.getOwnerOffset().rotated(owner.getRotation()))
                //.sub(new Vector2(weaponEntity.getBounds().getWidth(), weaponEntity.getBounds().getHeight()).div(2))
                );
                weaponEntity.setRotation(owner.getRotation());
            }

            for (int i = wEvents.size() - 1; i > 0; i--) {
                if (wEvents.get(i).getShooter() != weaponEntity.getOwner()) { //Will only send off a ShootingEvent if the shooter in the WeaponEvent that is being examined, is the owner of the Weapon.
                    continue;
                }
                if (weaponEntity.getCooldown() <= 0) //If the cooldown is 0 and there is a WeaponEvent pertaining to the Weapon, the Weapon fires, and resets the cooldown.
                {
                    shoot = true;
                    weaponEntity.setCooldown(weaponEntity.getFireRate());
                }
                wEvents.remove(i);
            }
            weaponEntity.setCooldown(weaponEntity.getCooldown() - gameData.getDeltaTime()); //Each update lowers the cooldown of the weapon.
            if (shoot) {
                Vector2 ownerPos = owner.getPosition();
                Vector2 ownerVel = owner.getVelocity();
                Vector2 directionVel = new Vector2((float) Math.cos(Math.toRadians(weaponEntity.getRotation())), (float) Math.sin(Math.toRadians(weaponEntity.getRotation())));
                sEvents.add(new ShootingEvent(new Bullet() {
                    {
                                setBounds(new Rectangle(weaponEntity.getBarrelRadius(), weaponEntity.getBarrelRadius()));
                                setPositionCentered(weaponEntity.getPositionCentered().add(weaponEntity.getBarrelOffset().rotated(weaponEntity.getRotation())));
                                setCollidable(true);
                                setRotation(weaponEntity.getRotation());
                        switch (weaponEntity.getType()){
                            case CROSSBOW:
                                setBulletType(ShootingType.PROJECTILE);
                                setAcceleration(1.f);
                                setVelocity(
                                    directionVel.mul(666.f)
                                        .add((ownerVel).div(2))                                       
                                );
                                
                                gameData.getAudioPlayer().play(crossbowSound);
                                break;
                        
                            case MACE:
                                setBulletType(ShootingType.MELEE);
                                setAcceleration(0.f);
                                setVelocity(
                                    directionVel.mul(0.f)
                                );
                                break;
                                
                        }
                    }
                }));
                shoot = false;
            }

            for (ListIterator<CollisionEvent> i = CollisionData.getEvents(gameData.getTickCount()).listIterator(); i.hasNext();) {
                CollisionEvent colEv = i.next(); //Iterates CollisionEvents to see if there are any pertaining to the Weapon and a possible Owner.
                if (colEv.getOtherID().equals(weaponEntity.getId())) {
                    if (world.getEntityByID(colEv.getTargetID()) instanceof Player) {
                        weaponEntity.setOwner(colEv.getTargetID());
                        weaponEntity.setCollidable(false);
                    }
                    i.remove();
                }
            }

        }
        for (ListIterator<MobEvent> i = MobData.getEvents().listIterator(); i.hasNext();) {
            MobEvent mobEvent = i.next();
            if (mobEvent.getType() == MobEventType.SPAWN) {
                Mob mob = mobEvent.getMob();
                switch (mob.getMobType()) {
                    case MELEE:
                        Weapon mobWeaponMelee = generateWeapon(MACE);
                        mobWeaponMelee.setOwner(mob.getId());
                        mobWeaponMelee.setCollidable(false);
                        world.addEntity(mobWeaponMelee);
                        System.out.println("Melee mob equipped with weapon");
                        break;

                    default:
                        Weapon mobWeaponRanged = generateWeapon(CROSSBOW);
                        mobWeaponRanged.setOwner(mob.getId());
                        mobWeaponRanged.setCollidable(false);
                        world.addEntity(mobWeaponRanged);
                        System.out.println("Ranged mob equipped with weapon");
                        break;
                }
                i.remove();
            }
        }

    }

    @Override
    public void render(Graphics g, World world) {
        for (Entity e : world.getEntities()) {
            if (!(e instanceof Weapon)) {
                continue;
            }
            g.drawSprite(
                    /* Position    */e.getPosition(),
                    /* Size        */ new Vector2(e.getBounds().getWidth(), e.getBounds().getHeight()),
                    /* InputStream */ e.getInputStream(),
                    /* Rotation    */ e.getRotation(),
                    /* LayerHeight */ 4
            );
        }
    }

    private Weapon generateWeapon(WeaponType type) {
        return new Weapon() {
            {
                setType(CROSSBOW);
                switch (type) {
                                                      
                    case MACE:
                        setPosition(new Vector2(200.f, 200.f));
                        setMaxVelocity(0.f);
                        setAcceleration(0.f);
                        setCollidable(true);
                        setBounds(new Rectangle(32, 32));
                        setBarrelRadius(32);
                        setBarrelOffset(new Vector2(-4, 8));
                        setOwnerOffset(new Vector2(16, -8));
                        setFireRate(0.5f);
                        setCooldown(0);
                        setInputStream(textureMace);
                        break;
                    
                    case CROSSBOW:
                        setPosition(new Vector2(200.f, 200.f));
                        setMaxVelocity(0.f);
                        setAcceleration(0.f);
                        setCollidable(true);
                        setBounds(new Rectangle(48, 48));
                        setBarrelRadius(19);
                        setBarrelOffset(new Vector2(-4, 8));
                        setOwnerOffset(new Vector2(16, -8));
                        setFireRate(0.3f);
                        setCooldown(0);
                        setInputStream(textureCrossbow);
                        break; 
                }
            }
        };
    }

}
