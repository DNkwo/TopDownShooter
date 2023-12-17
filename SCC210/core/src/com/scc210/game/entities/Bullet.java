package com.scc210.game.entities;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

/**
 * Bullet object universal to all weapons
 */
public class Bullet {

    //sprite
    Sprite sprite;

    //respective gun
    private Sprite gunSprite;

    //position in the world
    private float posX;
    private float posY;
    private float width, height;

    //start point
    private float startX;
    private float startY;

    //direction
    private float angle;

    //accuracy
    private int accuracy;

    //damage
    public float damage;
    private float speed;

    //other variables
    private boolean isValid = true;

    //list of updatable entities for collision

    private PointLight bulletLight;

    /**
     * Class constructor
     * @param texture Bullet texture
     * @param gun The gun the bullet is coming out of
     * @param accuracy the accuracy value
     */
    public Bullet(Texture texture, Gun gun, int accuracy){
        sprite = new Sprite(texture);
        sprite.setScale(2);

        this.width = sprite.getWidth()*2;
        this.height = sprite.getHeight()*2;

        this.gunSprite = gun.sprite;

        this.accuracy = accuracy;

        this.damage = gun.damage;
        this.speed = gun.projectileSpeed;



        spawnBullet();

    }

    /**
     * Spawns the bullet depending on sprite location of the gun.
     */
    private void spawnBullet(){

        posX = gunSprite.getX();
        posY = gunSprite.getY();

        int devianceDir = new Random().nextInt(2);
        if (devianceDir == 0){
            devianceDir = -1;
        }

        angle = gunSprite.getRotation();
        if (accuracy != 0) {
            angle = gunSprite.getRotation() + (new Random().nextInt(accuracy) * devianceDir);
        }

        sprite.setX(posX);
        sprite.setY(posY);

    }

    /**
     * Update the bullet position based on its variables
     */
    public void update(){

        //move bullet
        float radians = MathUtils.degreesToRadians * angle;

        posX += speed * Gdx.graphics.getDeltaTime() * MathUtils.cos(radians);
        posY += speed * Gdx.graphics.getDeltaTime() * MathUtils.sin(radians);

        sprite.setX(posX);
        sprite.setY(posY);
        sprite.setRotation(angle);

    }

    /**
     * Draws the sprite batch
     * @param batch - Sprite batch variable from libGDX
     */
    public void draw(Batch batch){
        sprite.draw(batch);
    }

    public boolean isValid() {
        return isValid;
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

    public float getAngle() {
        return angle;
    }
}
