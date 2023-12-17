package com.scc210.game.entities;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.scc210.game.entities.Bullet;
import com.scc210.game.entities.Weapon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Base weapon class for weapons that shoot bullets (projectiles)
 */
public abstract class Gun extends Weapon {

    protected float projectileSpeed;
    protected Texture projectileTexture;
    protected List<Bullet> projectileList = new ArrayList<Bullet>();

    protected int accuracyRange[];
    protected int bulletsShot;
    protected float damage;


    private Sprite ownerSprite;

    private Sprite debug;

    private RayHandler handler;

//    private ConeLight gunFlash;

    private float shootingTimer = 0.5f;


    /**
     * Gun construct
     * @param posX x position
     * @param posY y position
     * @param updatableEntities list of the updatable entities in the world
     * @param objectId id of the entity
     * @param handler ray handler
     */
    public Gun(float posX, float posY, List<UpdatableEntity> updatableEntities, int objectId, RayHandler handler) {
        super(posX, posY, objectId);
        this.updatableEntities = updatableEntities;
        this.handler = handler;
//        gunFlash = new ConeLight(handler, 50, Color.CYAN, 0, posX, posY, 0, 45);
    }

    public void update(float delta) {


        List<Bullet> expiredBullets = new ArrayList<>();
        //update bullets related to this weapon
        for(Bullet b : projectileList){


            if (b.isValid()){
                b.update();
            }
            else {
                expiredBullets.add(b);
            }

            //collision
            for(UpdatableEntity entity: updatableEntities){
                if(isCollide(b, entity)){
                    if(entity != null && entity.getObjectId() >= 10 && entity.getObjectId() <= 19) {
                        expiredBullets.add(b);
                        //ensure that zombie don't hit other zombie
                        if(!(owner != null && entity != null && owner.getObjectId() >= 10 && entity.getObjectId() >= 10 && entity.getObjectId() <= 19)) {
                            float radians = MathUtils.degreesToRadians * b.getAngle();
                            entity.getHit(damage, pushAmount, MathUtils.cos(radians),MathUtils.sin(radians));
                        }
                    }
                    //PLAYER GET HIT
                    if(owner != null && owner.getObjectId() >= 10) {
                        if (entity.getObjectId() == 0) {
                            float radians = MathUtils.degreesToRadians * b.getAngle();
                            entity.getHit(damage, 0, MathUtils.cos(radians), MathUtils.sin(radians));

                        }
                    }
                    if(entity != null && entity.getObjectId() >= 21 && entity.getObjectId() <= 29 || entity.getObjectId() == 42) {
                        if(owner != null && owner.getObjectId() == 0) {
                            entity.getHit(damage, 0, 0, 0);
                        }
                        expiredBullets.add(b);
                    }
                }
            }
        }

        //remove expired bullets
        projectileList.removeAll(expiredBullets);

        //gun updater

        if (owner == null){
            sprite.setPosition(posX, posY);
            return;
        }

        ownerSprite = owner.sprite;

        float relationFromPlayerCenterToHand = 0.80f;

        //get x distance between player center and its hands
        float additionX = MathUtils.cos(MathUtils.degreesToRadians * ownerSprite.getRotation());
        additionX *= ownerSprite.getWidth();
        additionX *= relationFromPlayerCenterToHand;

        //get y distance between player center and its hands
        float additionY = MathUtils.sin(MathUtils.degreesToRadians * ownerSprite.getRotation());
        additionY *= ownerSprite.getWidth();
        additionY *= relationFromPlayerCenterToHand;

        //set the gun position

        posX = owner.posX + ownerSprite.getWidth() / 2 - sprite.getWidth() / 2 + additionX * Math.max(1,sprite.getWidth()/13);
        posY = owner.posY + ownerSprite.getHeight() / 2 - sprite.getHeight() / 2 + additionY * Math.max(1,sprite.getWidth()/13);

        //get player center
        /*posX = owner.posX + ownerSprite.getWidth()/2;
        posY = owner.posY + ownerSprite.getHeight()/2;

        //get hand pos
        posX += ownerSprite.getWidth()/2 * ownerSprite.getScaleX() * MathUtils.cosDeg(ownerSprite.getRotation()) * relationFromPlayerCenterToHand;
        posY += ownerSprite.getHeight()/2 * ownerSprite.getScaleY() * MathUtils.sinDeg(ownerSprite.getRotation()) * relationFromPlayerCenterToHand;

        //add the gun size
        posX += sprite.getWidth()/2 * sprite.getScaleX() * MathUtils.cosDeg(ownerSprite.getRotation());
        posY += sprite.getHeight()/2 * sprite.getScaleY() * MathUtils.sinDeg(ownerSprite.getRotation());*/

        sprite.setPosition(posX, posY);

        //debug = new Sprite(new Texture(Gdx.files.internal("img/debugSquare.png")));
        //debug.setPosition(posX - debug.getWidth()/2, posY - debug.getHeight()/2);

        //set the gun rotation
        sprite.setRotation(ownerSprite.getRotation());

//        gunFlash.setDirection(ownerSprite.getRotation());
//        gunFlash.setPosition(posX, posY);
//
//        shootingTimer += delta;
//        gunFlash.setDistance(0);
//        if(shootingTimer < 0.15f){
//            float intensity = 400;
//            gunFlash.setDistance(intensity-(intensity*shootingTimer/0.15f));
//        }
    }

    /**
     * Shoot a projectile (creates bullet)
     */
    public void Shoot() {


        //check if time has passes since last shot
        if (!canShoot()) return;

        shootingTimer = 0;

        //get accuracy
        int accuracy = accuracyRange[0];

        //if moving
        if (owner.dirX != 0 || owner.dirY != 0){
            accuracy = accuracyRange[1];
        }

        //shoot
        for (int i = 0; i < bulletsShot; i++) {

            Bullet bullet = new Bullet(projectileTexture, this, accuracy);
            projectileList.add(bullet);
        }

        //play gunShot sound
        shotSound.play();

    }

    public void draw(SpriteBatch batch){

        sprite.draw(batch);

        if (debug != null) debug.draw(batch);

        for(Bullet bullet: projectileList) {
            bullet.draw(batch);
        }

        //new BitmapFont().draw(batch, Integer.toString(projectileList.size()), 10, 800 - 10);

    }

    public boolean isCollide(Bullet e1, Entity e2){
        if(e1.getPosX() < e2.getPosX() + e2.getWidth() &&
                e1.getPosX() + e1.getWidth() > e2.getPosX() &&
                e1.getPosY() < e2.getPosY() + e2.getHeight() &&
                e1.getHeight() + e1.getPosY() > e2.getPosY()){
            return true;
        }
        return false;
    }


}
