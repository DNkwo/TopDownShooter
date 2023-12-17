package com.scc210.game.entities.weapons;

import com.badlogic.gdx.math.MathUtils;
import com.scc210.game.MathsClass;
import com.scc210.game.entities.UpdatableEntity;
import com.scc210.game.entities.Weapon;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.scc210.game.entities.Weapon;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;


public class Knife extends Weapon {

    private Sprite ownerSprite;

    public Knife(Texture texture, float posX, float posY, List<UpdatableEntity> updatableEntities) {
        super(posX, posY, 34);
        this.updatableEntities = updatableEntities;
        this.damage = 50;
        this.pushAmount = 400;
        sprite = new Sprite(texture);
        sprite.setScale(4, 2);
        this.width = sprite.getWidth()*4;
        this.height = sprite.getHeight()*2;
    }

    @Override
    public void update(float delta) {
        if (owner == null){
            sprite.setPosition(posX, posY);
            return;
        }

        ownerSprite = owner.getSprite();

        float relationFromPlayerCenterToHand = 1f;

        //get x distance between player center and its hands
        float additionX = MathUtils.cos(MathUtils.degreesToRadians * ownerSprite.getRotation());
        additionX *= ownerSprite.getWidth();
        additionX *= relationFromPlayerCenterToHand;

        //get y distance between player center and its hands
        float additionY = MathUtils.sin(MathUtils.degreesToRadians * ownerSprite.getRotation());
        additionY *= ownerSprite.getWidth();
        additionY *= relationFromPlayerCenterToHand;

        //set the knife position

        //

        posX = owner.getPosX() + ownerSprite.getWidth() / 2 - sprite.getWidth() / 2 + additionX * Math.max(1,sprite.getWidth()/22);
        posY = owner.getPosY() + ownerSprite.getHeight() / 2 - sprite.getHeight() / 2 + additionY * Math.max(1,sprite.getWidth()/22);

        sprite.setPosition(posX, posY);

        sprite.setRotation(ownerSprite.getRotation());
    }




    @Override
    public void Shoot() {
        if(!canShoot()){return;}

        for(UpdatableEntity entity: updatableEntities) {
            if (MathsClass.isCollide(this, entity)) {
                if(entity.getObjectId() >= 10 && entity.getObjectId() <= 19){
                    float radians = MathUtils.degreesToRadians * this.getSprite().getRotation();
                    entity.getHit(damage, pushAmount, MathUtils.cos(radians),MathUtils.sin(radians));
                }
                if(entity.getObjectId() >= 21 && entity.getObjectId() <= 29 || entity.getObjectId() == 42) {
                    entity.getHit(damage, 0, 0, 0);
                }
            }
        }
    }
}
