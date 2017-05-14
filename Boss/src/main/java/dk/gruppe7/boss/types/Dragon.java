/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.boss.types;

import dk.gruppe7.AI.State;
import dk.gruppe7.AI.StateMachine;
import dk.gruppe7.boss.BossSystem;
import dk.gruppe7.bosscommon.Boss;
import dk.gruppe7.common.Dispatcher;
import dk.gruppe7.common.GameData;
import dk.gruppe7.common.World;
import dk.gruppe7.common.data.Entity;
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Animation;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.damagecommon.DamageData;
import dk.gruppe7.damagecommon.HealthData;
import dk.gruppe7.playercommon.Player;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingEvent;

public class Dragon extends Boss {

    StateMachine sm = new StateMachine();

    Dragon dragon = this;

    GameData gameData;
    World world;

    Animation walk_down, spin;

    Image bulletTexture;

    int stage = 1;

    public Dragon(GameData gameData, World world) {
        this.gameData = gameData;
        this.world = world;

        walk_down = new Animation(new Image[]{
            gameData.getResourceManager().addImage("walk_down1", BossSystem.class.getResourceAsStream("D1.png")),
            gameData.getResourceManager().addImage("walk_down2", BossSystem.class.getResourceAsStream("D2.png")),
            gameData.getResourceManager().addImage("walk_down3", BossSystem.class.getResourceAsStream("D3.png")),
            gameData.getResourceManager().addImage("walk_down4", BossSystem.class.getResourceAsStream("D4.png")),}, 0.2f);

        spin = new Animation(new Image[]{
            gameData.getResourceManager().addImage("spin1", BossSystem.class.getResourceAsStream("D1.png")),
            gameData.getResourceManager().addImage("spin1", BossSystem.class.getResourceAsStream("L1.png")),
            gameData.getResourceManager().addImage("spin1", BossSystem.class.getResourceAsStream("U1.png")),
            gameData.getResourceManager().addImage("spin1", BossSystem.class.getResourceAsStream("R1.png")),}, .05f);

        bulletTexture = gameData.getResourceManager().addImage("minotaurBullet", BossSystem.class.getResourceAsStream("bullet.png"));

        sm.setState(bulletHell);
        this.setBounds(new Rectangle(120, 86));
        this.setCollidable(true);
        this.setHealthData(new HealthData(1000));
    }

    State chaseTarget = new State("Chase Target", new Runnable() {
        boolean first = true;

        float timeLeft;

        @Override
        public void run() {
            if (first) {
                dragon.animator.setInterval(0.1f);
                dragon.getAnimator().play(walk_down);
                timeLeft = 3f;
                first = false;
            }
            if (dragon.getTarget() == null || world.<Player>getEntityByID(dragon.getTarget()) == null) {
                sm.setState(null);
                return;
            }

            Vector2 diff = world.<Player>getEntityByID(dragon.getTarget()).getPositionCentered().sub(dragon.getPositionCentered());
            dragon.setVelocity(diff.normalize().mul(200));
            timeLeft -= gameData.getDeltaTime();

            if (diff.len() < 100 || timeLeft <= 0) {
                sm.setState(bulletHell);
                first = true;
            }

            checkState();

        }
    });

    State bulletHell = new State("BulletHell", new Runnable() {
        float timeLeft;
        float interval;
        int i;
        int angleAdd;

        boolean first = true;

        @Override
        public void run() {
            if (first) {
                dragon.animator.play(walk_down);
                dragon.animator.setInterval(0.01f);
                timeLeft = 3f;
                first = false;
                dragon.setVelocity(Vector2.zero);
            }

            interval -= gameData.getDeltaTime();
            timeLeft -= gameData.getDeltaTime();

            if (interval <= 0) {
                angleAdd++;
                for (i = 0; i <= 2.5 * stage; i++) {
                    Dispatcher.post(new ShootingEvent(new Bullet() {
                        {
                            Vector2 dir = Vector2.fromAngle((i * 36) + (float) Math.sin(angleAdd / 3.032f) / 2f).normalize();
                            setPositionCentered(dragon.getPositionCentered().add(dir.mul(50)));
                            setVelocity(dir.mul(250));
                            setBounds(new Rectangle(16, 16));
                            setCollidable(true);
                            setOwner(dragon.getId());
                            setTexture(bulletTexture);
                            setDamage(new DamageData(6));
                        }
                    }), world);
                }
                interval = .25f;
            }

            if (timeLeft <= 0) {
                sm.setState(chaseTarget);
                first = true;
            }

            checkState();

        }
    });

    State stageTransition = new State("stageTransition", new Runnable() {

        float zSpeed = 300;
        boolean first = true;

        @Override
        public void run() {
            if (first) {
                zSpeed = 300;
                first = false;
            }
            dragon.getAnimator().play(spin);
            zSpeed -= 5;
            dragon.height += zSpeed * gameData.getDeltaTime();
            Vector2 diff = new Vector2(1280f / 2f, 350).sub(dragon.getPositionCentered());
            dragon.setVelocity(diff.normalize().mul(120));
            dragon.setCollidable(false);

            if (dragon.height < 0) {
                dragon.height = 0;
                dragon.setCollidable(true);
                dragon.stage++;
                sm.setState(chaseTarget);
                first = true;
            }
        }
    });

    private void checkState() {
        if (dragon.stage == 1 && dragon.getHealthData().getHealth() < 0.75f * dragon.getHealthData().getStartHealth()) {
            sm.setState(stageTransition);
        }
        else if (dragon.stage == 2 && dragon.getHealthData().getHealth() < 0.50f * dragon.getHealthData().getStartHealth()) {
            sm.setState(stageTransition);
        }
        else if (dragon.stage == 3 && dragon.getHealthData().getHealth() < 0.25f * dragon.getHealthData().getStartHealth()) {
            sm.setState(stageTransition);
        }

    }

    @Override
    public void process() {
        sm.process();
    }

}
