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
public class HealthData
{
    private float health;
    private float startHealth;

    public HealthData(float startHealth)
    {
        this.startHealth = startHealth;
        this.health = startHealth;
    }

    public float getHealth()
    {
        return health;
    }

    public void setHealth(float health)
    {
        this.health = health;
    }
    
    public void decreaseHealth(float amount)
    {       
        this.health-=amount;
        if(this.health <= 0){
            this.health=0;
        }        
    }

    public float getStartHealth()
    {
        return startHealth;
    }
    
    
}
