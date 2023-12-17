package com.scc210.game.entities;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;
import com.scc210.game.CharacterController;
import com.scc210.game.entities.ControllableEntity;
import com.scc210.game.entities.UpdatableEntity;

import java.util.List;

/**
 * Class to represent weapons in general, contains basic functions
 */
public abstract class Weapon extends UpdatableEntity {

    //Entity possessing the weapon (NULL = dropped)
    protected ControllableEntity owner;

    //Weapon variables
    protected float damage;
    protected float fireRate;
    protected Sound shotSound = null;

    //high number to make the canShoot function work the first time it is called
    private float lastShotTime = 99f;

    protected List<UpdatableEntity> updatableEntities;

    //weapon constructor

    /**
     *
     * @param posX x position in the world
     * @param posY y position in the world
     * @param objectId id of the object
     */
    public Weapon(float posX, float posY, int objectId){

        super(posX, posY, objectId, 0);

    }

    //shoot function (can be different for each weapon type)

    /**
     * method used to shoot the weapon
     */
    public abstract void Shoot();

    //check if the weapon can shoot based on the fire rate

    /**
     * Method to check if we are able to shoot based on the firerate variable
     * @return
     */
    public boolean canShoot(){

        //if the last time the weapon was shot exceeds the fire rate time in seconds
        if (!(TimeUtils.nanoTime() - lastShotTime > fireRate * 1000000000)) return false;

        //record current time stamp as last shot
        lastShotTime = TimeUtils.nanoTime();

        return true;

    }

    /**
     * sets the ownership of a weapon to a controllable entity moving it to its hands
     * @param owner controllable entity that owns the gun
     */
    public void setOwner(ControllableEntity owner) {
        this.owner = owner;
    }

    public ControllableEntity getOwner(){
        return this.owner;
    }

    public void getHit(float damage, float pushAmount, float dirX, float dirY) {

    }

    public void setUpdatableEntities(List<UpdatableEntity> updatableEntities) {
        this.updatableEntities = updatableEntities;
    }
}
