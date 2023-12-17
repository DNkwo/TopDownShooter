package com.scc210.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Class for entities that are movable
 */
public abstract class ControllableEntity extends UpdatableEntity{

    protected int dirX;
    protected int dirY;

    protected double baseSpeed;

    /**
     *
     * @param posX x position in the world
     * @param posY y position in the world
     * @param baseSpeed speed of the entity as units per second
     * @param health health of the entity
     * @param objectId id of the entity
     */
    public ControllableEntity(float posX, float posY, double baseSpeed, float health, int objectId){

        super(posX, posY, objectId, health);

        this.baseSpeed = baseSpeed;

    }

}
