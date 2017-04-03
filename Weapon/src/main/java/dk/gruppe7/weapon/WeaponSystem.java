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
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingData;
import dk.gruppe7.shootingcommon.ShootingEvent;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.weaponcommon.Weapon;
import dk.gruppe7.weaponcommon.WeaponData;
import dk.gruppe7.weaponcommon.WeaponEvent;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
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

    //String weaponSprite = "weapon.png";
    InputStream texture = getClass().getResourceAsStream("weapon.png");
    //Boolean shoot = false;
    List<ShootingEvent> sEvents = ShootingData.getEvents();
    
    List<WeaponEvent> wEvents = WeaponData.getEvents();
    
    @Override
    public void start(GameData gameData, World world) {
        //Weapon for testing
        Weapon addedWeapon = generateWeapon();
        world.addEntity(addedWeapon);
        //addedWeapon.setOwner(world.getEntitiesByClass(Player.class).get(0).getId());

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
            Player owner = null;
            if (weaponEntity.getOwner() != null) {
                owner = (Player) world.getEntityByID(weaponEntity.getOwner());
            } else {
                for (Entity e2 : world.getEntities()) {
                    if (e2 instanceof Player) {
                        weaponEntity.setOwner(e2.getId());
                        break;
                    }
                }
            }
            
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
                if (wEvents.get(i).getShooter() != weaponEntity.getOwner()) {
                    continue;
                }
                shoot = true;
                wEvents.remove(i);
            }
            
            if (shoot) {
                Vector2 ownerPos = owner.getPosition();
                Vector2 ownerVel = owner.getVelocity();
                Vector2 directionVel = new Vector2((float)Math.cos(Math.toRadians(weaponEntity.getRotation())), (float)Math.sin(Math.toRadians(weaponEntity.getRotation())));
                sEvents.add(new ShootingEvent(new Bullet() {
                    {
                        setBulletType(ShootingType.PROJECTILE);
                        setAcceleration(1.f);
                        setVelocity(
                                directionVel.mul(666.f)                                
                                .add((ownerVel).div(2))
                        );
                        setBounds(new Rectangle(weaponEntity.getBarrelRadius(), weaponEntity.getBarrelRadius()));
                        setPositionCentered(weaponEntity.getPositionCentered().add(weaponEntity.getBarrelOffset().rotated(weaponEntity.getRotation())));
                    }
                }));
                shoot = false;
            }
            
            for (Iterator<CollisionEvent> i = CollisionData.getEvents().iterator(); i.hasNext();) {
                CollisionEvent colEv = i.next();
                if (colEv.getOtherID().equals(weaponEntity.getId()) || colEv.getTargetID().equals(weaponEntity.getId())) {
                    
                    if (world.getEntityByID(colEv.getOtherID()).getClass().equals(Player.class)) {
                        weaponEntity.setOwner(colEv.getOtherID());
                    }
                    if (world.getEntityByID(colEv.getTargetID()).getClass().equals(Player.class)) {
                        weaponEntity.setOwner(colEv.getTargetID());
                    }
                }
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
                    /* Position    */ e.getPosition(),
                    /* Size        */ new Vector2(e.getBounds().getWidth(), e.getBounds().getHeight()),
                    /* InputStream */ texture,
                    /* Rotation    */ e.getRotation()
            );
        }
    }
    
    private Weapon generateWeapon() {
        return new Weapon() {
            {
                setPosition(new Vector2(50.f, 50.f));
                setMaxVelocity(0.f);
                setAcceleration(0.f);
                setCollidable(true);
                setBounds(new Rectangle(48, 48));
                setBarrelRadius(19);
                setBarrelOffset(new Vector2(0, 0));
                setOwnerOffset(new Vector2(16, -8));
            }
        };
    }
    
}
