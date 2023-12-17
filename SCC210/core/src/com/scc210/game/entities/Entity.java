package com.scc210.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * General class for everything that we created in game
 */
public abstract class Entity {

    protected Sprite sprite;

    protected float posX;
    protected float posY;
    protected float width, height;
    protected int objectId;
    protected int scale = 1;
    protected int meleeDamage;
    //Player = 0
    //Basic Enemy = 10
    //Basic Floor Tile = 20, Basic Wall Tile = 21
    //Simple Gun = 30, Shotgun = 31, RayGun = 32, AssaultRifle = 33, Knife = 34
    //Coin = 40
    protected String name = "";


    /**
     * Create an Entity
     * @param posX x position in the world
     * @param posY y position in the world
     * @param objectId identifier of the object
     */
    public Entity(float posX, float posY, int objectId){
        this.posX = posX;
        this.posY = posY;
        this.objectId = objectId;
    }

    /**
     * LibGDX method to draw sprite
     * @param batch spritebatch variable from LibGDX
     */
    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }


    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getMeleeDamage() {
        return meleeDamage;
    }
}
