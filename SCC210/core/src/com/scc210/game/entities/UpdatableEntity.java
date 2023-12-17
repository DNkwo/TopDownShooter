package com.scc210.game.entities;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Entities that can be updated in the game loop
 */
public abstract class UpdatableEntity extends Entity{

    protected float pushedSpeed;
    protected float pushAmount;
    protected float currentHealth;
    protected float totalHealth;
    protected boolean alive = true;
    protected boolean isPushed = false;



    /**
     *
     * @param posX x position in the world
     * @param posY y position in the world
     * @param objectId id of entity
     * @param health health value of the entity
     */
    public UpdatableEntity(float posX, float posY, int objectId, float health){
        super(posX, posY, objectId);
        totalHealth = health;
        currentHealth = health;
    }

    public abstract void update(float delta);

    public float getCurrentHealth() {
        return currentHealth;
    }

    public float getTotalHealth() {
        return totalHealth;
    }

    /**
     * Method that is used to calculate the effects on an entity
     * when they get hit by another entity(mainly bullets)
     * @param damage - damage taken
     * @param pushAmount - float value used to "knock-back" the entity
     * @param dirX - x direction of knock back
     * @param dirY - y direction of knock back
     */
    public abstract void getHit(float damage, float pushAmount, float dirX, float dirY);


    public boolean isAlive() {
        return alive;
    }

    public float getPushAmount() {
        return pushAmount;
    }

    public boolean isPushed() {
        return isPushed;
    }

    public void setPushed(boolean pushed) {
        isPushed = pushed;
    }



}
