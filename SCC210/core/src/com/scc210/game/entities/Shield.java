package com.scc210.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import java.util.List;

/**
 *  Shield Class
 */
public class Shield extends Weapon{
    List<UpdatableEntity> updatableEntities;
    private Sprite ownerSprite;

    public Shield(Texture texture,float posX, float posY, List<UpdatableEntity> updatableEntities) {
        super(posX, posY,36 );
        this.updatableEntities = updatableEntities;
        sprite = new Sprite(texture);

    }


    /**
     * Method used to check if the player is holding the shield and if so updating the sprite.
     * @param delta
     */
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

        posX = owner.getPosX() + ownerSprite.getWidth() / 2 - sprite.getWidth() / 2 + additionX * Math.max(1,sprite.getWidth()/22);
        posY = owner.getPosY() + ownerSprite.getHeight() / 2 - sprite.getHeight() / 2 + additionY * Math.max(1,sprite.getWidth()/22);

        sprite.setPosition(posX, posY);

        sprite.setRotation(ownerSprite.getRotation());
    }

    @Override
    public void Shoot() {
        return;
    }

}
