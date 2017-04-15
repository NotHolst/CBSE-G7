/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.shootingcommon;

import dk.gruppe7.common.Entity;
import dk.gruppe7.damagecommon.DamageData;

/**
 *
 * @author Mathies H
 */
public class Bullet extends Entity {

    private ShootingType bulletType;
    private DamageData damage = new DamageData() {{setDamage(1);}};
    private float despawnTimer = 10;

    public DamageData getDamageData()
    {
        return damage;
    }

    public void setDamage(DamageData damage)
    {
        this.damage = damage;
    }

    public ShootingType getBulletType() {
        return bulletType;
    }

    public void setBulletType(ShootingType bulletType) {
        this.bulletType = bulletType;
    }

    public float getDespawnTimer() {
        return despawnTimer;
    }

    public void setDespawnTimer(float despawnTimer) {
        this.despawnTimer = despawnTimer;
    }

}
