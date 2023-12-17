package com.scc210.game.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.scc210.game.MathsClass;
import com.scc210.game.entities.Player;
import com.scc210.game.entities.Weapon;

import java.util.List;

/**
 * The players ultimate skill
 */
public class EnergyField extends Weapon {

    private Animation<TextureRegion> animation;
    private Texture spritesheet;
    private TextureRegion[] animationFrames;
    private float elapsedTime;
    private TextureRegion currentFrame;

    private Player player;
    private List<UpdatableEntity> updatableEntities;


    /**
     *
     * @param player
     * @param posX
     * @param posY
     * @param updatableEntities
     */
    public EnergyField(Player player, float posX, float posY, List<UpdatableEntity> updatableEntities) {
        super(posX, posY, 50);
        this.player = player;
        spritesheet = new Texture(Gdx.files.internal("img/ultimate_spritesheet.png"));

        this.updatableEntities = updatableEntities;
        int ROW = 1, COLUMN = 5;
        TextureRegion[][] tmpFrames = TextureRegion.split(spritesheet, spritesheet.getWidth() / COLUMN, spritesheet.getHeight() / ROW);
//
        animationFrames = new TextureRegion[ROW * COLUMN];
        int index = 0;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                animationFrames[index++] = tmpFrames[i][j];
            }
        }
        animation = new Animation(1f / 8f, animationFrames);
        currentFrame = animation.getKeyFrame(0);

        this.sprite = new Sprite(currentFrame);
        this.currentHealth = totalHealth;
        sprite.setScale(2f);
        this.width = sprite.getWidth()*2;
        this.height = sprite.getHeight()*2;
        this.damage = 25;
        this.pushAmount = 300;
        sprite.setPosition(posX, posY);

    }

    /**
     *
     * @param delta
     */
    @Override
    public void update(float delta) {
        elapsedTime += delta;
        this.currentFrame = animation.getKeyFrame(elapsedTime, true);
        this.sprite.setRegion(currentFrame);
        posX = player.getPosX() - width/4;
        posY = player.getPosY() - height/4;
        sprite.setPosition(posX, posY);

        Shoot();


    }

    /**
     * Treated as a weapon, so shoot :)
     */
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

    @Override
    public void getHit(float damage, float pushAmount, float dirX, float dirY) {

    }
}
