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
import dk.gruppe7.common.data.Rectangle;
import dk.gruppe7.common.data.Vector2;
import dk.gruppe7.common.graphics.Animation;
import dk.gruppe7.common.graphics.Animator;
import dk.gruppe7.common.resources.Image;
import dk.gruppe7.damagecommon.DamageData;
import dk.gruppe7.damagecommon.HealthData;
import dk.gruppe7.shootingcommon.Bullet;
import dk.gruppe7.shootingcommon.ShootingEvent;

public class Dragon extends Boss{
    StateMachine sm = new StateMachine();
    
    Dragon dragon = this;
    
    GameData gameData;
    World world;
    
    Animation walk_down, walk_up, walk_left, walk_right;
    
    Image bulletTexture;
    
    public Dragon(GameData gameData, World world){
        this.gameData = gameData;
        this.world = world;
        
        
        walk_down = new Animation(new Image[]{
            gameData.getResourceManager().addImage("walk_down1", BossSystem.class.getResourceAsStream("D1.png")),
            gameData.getResourceManager().addImage("walk_down2", BossSystem.class.getResourceAsStream("D2.png")),
            gameData.getResourceManager().addImage("walk_down3", BossSystem.class.getResourceAsStream("D3.png")),
            gameData.getResourceManager().addImage("walk_down4", BossSystem.class.getResourceAsStream("D4.png")),
        }, 0.2f);
        
        bulletTexture = gameData.getResourceManager().addImage("minotaurBullet", BossSystem.class.getResourceAsStream("bullet.png"));
        
        sm.setState(bulletHell);
        this.setBounds(new Rectangle(120, 86));
        this.setCollidable(true);
        this.setHealthData(new HealthData(256));
    }
    
    State chaseTarget = new State("Chase Target", new Runnable() {
        boolean first = true;
        
        float timeLeft;
        @Override
        public void run() {
            if(first){
                dragon.animator.setInterval(0.1f);
                timeLeft = 3f;
                first = false;
            }
            if(dragon.target == null){
                sm.setState(null);
                return;
            }
            Vector2 diff = dragon.target.getPositionCentered().sub(dragon.getPositionCentered());
            dragon.setVelocity(diff.normalize().mul(200));
            timeLeft -= gameData.getDeltaTime();
            
            if(diff.len() < 100 || timeLeft <= 0){
                sm.setState(bulletHell);
                first = true;
            }
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
            if(first){
                dragon.animator.play(walk_down);
                dragon.animator.setInterval(0.01f);
                timeLeft = 3f;
                first = false;
                dragon.setVelocity(Vector2.zero);
            }

            interval -= gameData.getDeltaTime();
            timeLeft -= gameData.getDeltaTime();
            
            if(interval <= 0){
                angleAdd++;
                for(i = 0; i <= 10; i++)
                    Dispatcher.post(new ShootingEvent(new Bullet(){{
                        Vector2 dir = Vector2.fromAngle((i*36)+(float)Math.sin(angleAdd/3.032f)/2f).normalize();
                        setPositionCentered(dragon.getPositionCentered().add(dir.mul(50)));
                        setVelocity(dir.mul(250));
                        setBounds(new Rectangle(16, 16));
                        setCollidable(true);
                        setOwner(dragon.getId());
                        setTexture(bulletTexture);
                        setDamage(new DamageData(6));
                    }}), world);
                interval = .25f;
            }
            
            if(timeLeft <= 0){
                sm.setState(chaseTarget);
                first = true;
            }
        }
    });
    

    @Override
    public void process() {
        sm.process();
    }
    
    
}
