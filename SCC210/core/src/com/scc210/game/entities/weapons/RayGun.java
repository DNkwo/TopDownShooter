package com.scc210.game.entities.weapons;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.scc210.game.entities.Gun;
import com.scc210.game.entities.UpdatableEntity;
import com.scc210.game.entities.Weapon;

import java.util.List;

public class RayGun extends Weapon {
//this might actually have to just be a subclass of weapon as Guns are hard coded with projectiles
    public RayGun(Texture texture, float posX, float posY, List<UpdatableEntity> entities, RayHandler handler){

        super(posX, posY,32);
        sprite = new Sprite(texture);
        sprite.setScale(2);
        this.width = sprite.getWidth()*2;
        this.height = sprite.getHeight()*2;

        /*Gun variables*/
        //this.accuracyRange = new int[]{10, 25};
        this.fireRate = 0.6f;
        this.damage = 50f;
        this.pushAmount = 50;

        shotSound = Gdx.audio.newSound(Gdx.files.internal("sound/gunshot.mp3"));

    }


    @Override
    public void update(float delta) {

    }

    @Override
    public void Shoot() {

    }
}
