package com.scc210.game.entities.weapons;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.scc210.game.entities.Gun;
import com.scc210.game.entities.UpdatableEntity;

import java.util.List;

public class SimpleGun extends Gun {

    public SimpleGun(Texture texture, float posX, float posY, List<UpdatableEntity> entities, RayHandler handler){

        super(posX, posY, entities, 30, handler);
        
        sprite = new Sprite(texture);
        sprite.setScale(2);
        this.width = sprite.getWidth()*2;
        this.height = sprite.getHeight()*2;

        /*Gun variables*/
        this.projectileSpeed = 1000;
        this.accuracyRange = new int[]{0, 20};
        this.bulletsShot = 1;
        this.fireRate = 0.3f;
        this.damage = 50f;
        this.pushAmount = 65;

        projectileTexture = new Texture(Gdx.files.internal("img/bullet.png"));
        shotSound = Gdx.audio.newSound(Gdx.files.internal("sound/gunshot.mp3"));

    }

}
