/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppe7.damagecommon;

/**
 *
 * @author pc4
 */
public class DamageData
{
    private float damage;

    public DamageData(float damage)
    {
        this.damage = damage;
    }

    public float getDamage()
    {
        return damage;
    }

    public void setDamage(float damage)
    {
        this.damage = damage;
    }
    
    public DamageData copy()
    {
        return new DamageData(damage);
    }
}
