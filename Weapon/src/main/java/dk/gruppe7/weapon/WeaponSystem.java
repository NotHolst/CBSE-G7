/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.weapon;

import dk.gruppe7.common.eventtypes.CollisionEvent;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.IProcess;
import dk.gruppe7.common.IRender;
import dk.gruppe7.common.World;
import dk.gruppe7.common.audio.AudioPlayer;
import dk.gruppe7.common.eventhandlers.ActionEventHandler;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Room;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.data.VirtualKeyCode;
import dk.gruppe7.common.eventhandlers.KeyEventHandler;
import dk.gruppe7.common.eventtypes.KeyPressedEvent;
import dk.gruppe7.common.eventtypes.KeyReleasedEvent;
import dk.gruppe7.common.graphics.Graphics;
import dk.gruppe7.common.resources.Audio;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.common.utils.RandomUtil;
import static dk.gruppe7.data.MobType.MELEE;
import dk.gruppe7.levelcommon.events.RoomChangedEvent;
import dk.gruppe7.mobcommon.MobEvent;
import dk.gruppe7.mobcommon.MobEventType;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.powerupcommon.PowerupEvent;
import dk.gruppe7.powerupcommon.PowerupType;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingEvent;
import dk.gruppe7.shootingcommon.ShootingType;
import dk.gruppe7.weaponcommon.Weapon;
import dk.gruppe7.weaponcommon.WeaponEvent;
import dk.gruppe7.weaponcommon.WeaponType;
import static dk.gruppe7.weaponcommon.WeaponType.CROSSBOW;
import static dk.gruppe7.weaponcommon.WeaponType.MACE;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

@ServiceProviders(value = {
    @ServiceProvider(service = IProcess.class),
    @ServiceProvider(service = IRender.class)
})
/**
 *
 * @author haral
 */
public class WeaponSystem implements IProcess, IRender {

    private Image textureMace;
    private Image textureCrossbow;
    private Image textureArrow;
    private InputStream maceSwing = getClass().getResourceAsStream("MaceSwing.png");
    private Audio crossbowSound;
    private Weapon currentWeapon = null;
    private ArrayList<Room> roomBeenIn = new ArrayList<>();
    private boolean pressingG = false;

    private AudioPlayer audioPlayer;

    KeyEventHandler<KeyPressedEvent> gKeyPressedHandler = new KeyEventHandler<KeyPressedEvent>(VirtualKeyCode.VC_G) {
        @Override
        public void call(KeyPressedEvent event) {
            pressingG = event.getState();
        }
    };

    KeyEventHandler<KeyReleasedEvent> gKeyReleasedHandler = new KeyEventHandler<KeyReleasedEvent>(VirtualKeyCode.VC_G) {
        @Override
        public void call(KeyReleasedEvent event) {
            pressingG = event.getState();
        }
    };

    @Override
    public void start(GameData gameData, World world) {
        crossbowSound = gameData.getResourceManager().addAudio("crossbowSound", getClass().getResourceAsStream("bow.wav"));
        textureCrossbow = gameData.getResourceManager().addImage("crossbow", getClass().getResourceAsStream("Crossbow.png"));
        textureMace = gameData.getResourceManager().addImage("mace", getClass().getResourceAsStream("Mace.png"));
        textureArrow = gameData.getResourceManager().addImage("arrow", getClass().getResourceAsStream("arrow.png"));

        audioPlayer = gameData.getAudioPlayer();

        //Standard weapon for the Player
        Weapon addedWeapon = generateWeapon(CROSSBOW);
        world.addEntity(addedWeapon);


        Dispatcher.subscribe(this);
    }

    @Override
    public void stop(GameData gameData, World world) {
        Dispatcher.unsubscribe(this);
        world.removeEntities(world.<Weapon>getEntitiesByClass(Weapon.class));
    }

    @Override
    public void process(GameData gameData, World world) {
        Entity owner = null;
        for (Weapon weapon : world.<Weapon>getEntitiesByClass(Weapon.class)) {
            if ((owner = world.getEntityByID(weapon.getOwner())) != null) {
                Vector2 offset = new Vector2(
                    weapon.getOwnerOffset().x * owner.getVelocity().normalize().x,
                    weapon.getOwnerOffset().y * (.5f+owner.getVelocity().normalize().y/2)
                );
                weapon.setPositionCentered(owner.getPositionCentered()
                    .add(weapon.getOwnerOffset().rotated(owner.getRotation())
)
                );

                weapon.setRotation(owner.getRotation());
            }

            weapon.setCooldown(weapon.getCooldown() - gameData.getDeltaTime()); //Each update lowers the cooldown of the weapon.
        }

        // drops the currentWeapon if you have one and i pressing g.
        if (currentWeapon != null && pressingG) {
            currentWeapon.setOwner(null);
            currentWeapon.setCollidable(true);
            currentWeapon.setRoomPersistent(false);
            currentWeapon.setPosition(new Vector2(currentWeapon.getPosition()).add(0, RandomUtil.GetRandomInteger(-100, 100)));
            currentWeapon = null;
        }
    }

    ActionEventHandler<MobEvent> mobEventHandler = (event, world) -> {
        if (event.getType() == MobEventType.SPAWN) {
            switch (event.getMob().getMobType()) {
                case MELEE:
                    Weapon mobWeaponMelee = generateWeapon(MACE);
                    mobWeaponMelee.setOwner(event.getMob().getId());
                    mobWeaponMelee.setCollidable(false);
                    world.addEntity(mobWeaponMelee);
                    //System.out.println("Melee mob equipped with weapon");
                    break;

                default:
                    Weapon mobWeaponRanged = generateWeapon(CROSSBOW);
                    mobWeaponRanged.setOwner(event.getMob().getId());
                    mobWeaponRanged.setCollidable(false);
                    world.addEntity(mobWeaponRanged);
                    //System.out.println("Ranged mob equipped with weapon");
                    break;
            }
        } else if (event.getType() == MobEventType.DEATH) {
            for (Weapon weapon : world.<Weapon>getEntitiesByClass(Weapon.class)) {
                if (weapon.getOwner() == event.getMob().getId()) {
                    weapon.setCollidable(true);
                }
            }
        }
    };

    ActionEventHandler<CollisionEvent> weaponPickupHandler = (event, world) -> {
        if (world.isEntityOfClass(event.getTargetID(), Player.class) && world.isEntityOfClass(event.getOtherID(), Weapon.class) && currentWeapon == null) {
            Weapon weapon = world.<Weapon>getEntityByID(event.getOtherID());
            weapon.setOwner(event.getTargetID());
            weapon.setCollidable(false);
            weapon.setRoomPersistent(true);
            currentWeapon = weapon;
        }
    };

    ActionEventHandler<WeaponEvent> weaponEventHandler = (event, world) -> {
        for (Weapon weapon : world.<Weapon>getEntitiesByClass(Weapon.class)) {
            if (weapon.getOwner() != null && weapon.getOwner().equals(event.getShooter()) && weapon.getCooldown() <= 0) {
                weapon.setCooldown(weapon.getFireRate());
                ShootingEvent sEvent = new ShootingEvent(new Bullet() {
                    {
                        setTexture(textureArrow);
                        Vector2 directionVel = new Vector2((float) Math.cos(Math.toRadians(weapon.getRotation())), (float) Math.sin(Math.toRadians(weapon.getRotation())));
                        setOwner(weapon.getOwner());
                        setBounds(new Rectangle(weapon.getBarrelRadius(), weapon.getBarrelRadius()));
                        setPositionCentered(weapon.getPositionCentered().add(weapon.getBarrelOffset().rotated(weapon.getRotation())));
                        setCollidable(true);
                        setRotation(weapon.getRotation());
                        Vector2 ownerVel = Vector2.zero;
                        if (weapon.getOwner() != null) {
                            ownerVel = world.getEntityByID(weapon.getOwner()).getVelocity();
                        }

                        switch (weapon.getType()) {
                            case CROSSBOW:
                                setBulletType(ShootingType.PROJECTILE);
                                setAcceleration(1.f);
                                setVelocity(
                                        directionVel.mul(666.f)
                                                .add((ownerVel).div(2))
                                );
                                audioPlayer.play(crossbowSound, (float) (Math.random() * 0.1f) + 0.75f);
                                break;

                            case MACE:
                                setBulletType(ShootingType.MELEE);
                                setAcceleration(0.f);
                                setVelocity(
                                        directionVel.mul(0.f)
                                );
                                setDespawnTimer(.3f);
                                break;

                        }
                    }
                });
                Dispatcher.post(sEvent, world);
            }
        }
    };

    // Will check if we have been in the room before. If we haven't we spawn some new weapons
    ActionEventHandler<RoomChangedEvent> RoomChangeHandler = (event, world) -> {
        if (!roomBeenIn.contains(world.getCurrentRoom())) {
            roomBeenIn.add(world.getCurrentRoom());
            world.addEntity(generateWeapon(CROSSBOW));
        }
    };

    ActionEventHandler<PowerupEvent> PowerupHandler = (event, world) -> {
        if (event.getPowerupData().getPowerupType() == PowerupType.WEAPON) {
            if(currentWeapon != null) {
                currentWeapon.setFireRate(currentWeapon.getFireRate() * event.getPowerupData().getFireRate());
            }
        }
    };

    @Override
    public void render(Graphics g, World world) {
        for (Weapon weapon : world.<Weapon>getEntitiesByClass(Weapon.class
        )) {
            g.drawSprite(
                    /* Position    */weapon.getPosition(),
                    /* Size        */ new Vector2(weapon.getBounds().getWidth(), weapon.getBounds().getHeight()),
                    /* InputStream */ weapon.getInputStream(),
                    /* Rotation    */ weapon.getRotation(),
                    /* LayerHeight */ 4,
                    weapon.getPositionCentered().y
            );
        }
    }

    private Weapon generateWeapon(WeaponType type) {
        return new Weapon() {
            {
                setType(type);
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
                        setInputStream(textureMace.getInputStream());
                        setRoomPersistent(false);
                        break;

                    case CROSSBOW:
                        setPosition(new Vector2((float) Math.random() * 500.f + 64, (float) Math.random() * 500.f + 64));
                        setMaxVelocity(0.f);
                        setAcceleration(0.f);
                        setCollidable(true);
                        setBounds(new Rectangle(48, 48));
                        setBarrelRadius(19);
                        setBarrelOffset(new Vector2(-4, 8));
                        setOwnerOffset(new Vector2(16, 0));
                        setFireRate(0.3f);
                        setCooldown(0);
                        setInputStream(textureCrossbow.getInputStream());
                        setRoomPersistent(false);
                        break;
                }
            }
        };
    }

}
