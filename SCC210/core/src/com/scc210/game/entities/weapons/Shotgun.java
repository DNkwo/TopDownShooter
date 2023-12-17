package com.scc210.game.entities.weapons;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.scc210.game.entities.Gun;
import com.scc210.game.entities.UpdatableEntity;

import java.util.List;

/**
 * Shotgun Class
 */
public class Shotgun extends Gun {

    public Shotgun(Texture texture, float posX, float posY, List<UpdatableEntity> entities, RayHandler handler){

        super(posX, posY, entities, 31, handler);
        sprite = new Sprite(texture);
        sprite.setScale(2);
        this.width = sprite.getWidth()*2;
        this.height = sprite.getHeight()*2;

        /*Gun variables*/
        this.projectileSpeed = 1000;
        this.accuracyRange = new int[]{10, 25};
        this.bulletsShot = 4;
        this.fireRate = 0.6f;
        this.damage = 50f;
        this.pushAmount = 200;

        projectileTexture = new Texture(Gdx.files.internal("img/bullet.png"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("sound/gunshot.mp3"));

    }


}