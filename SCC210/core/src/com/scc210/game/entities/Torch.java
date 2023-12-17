package com.scc210.game.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Torch extends  UpdatableEntity{

    private Animation<TextureRegion> animation;
    private Texture spritesheet;
    private TextureRegion[] animationFrames;
    private float elapsedTime;
    private TextureRegion currentFrame;

//    private PointLight light;
    public Torch(RayHandler handler, float posX, float posY) {
        super(posX, posY, 41, 0);

//        light = new PointLight(handler, 100, Color.ORANGE, 300, posX, posY);
//        light.setSoftnessLength(0);



        spritesheet = new Texture(Gdx.files.internal("img/torch_spritesheet.png"));

        int ROW = 1, COLUMN = 9;
        TextureRegion[][] tmpFrames = TextureRegion.split(spritesheet, spritesheet.getWidth()/COLUMN, spritesheet.getHeight()/ROW);
//
        animationFrames = new TextureRegion[ROW * COLUMN];
        int index = 0;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COLUMN; j++) {
                animationFrames[index++] = tmpFrames[i][j];
            }
        }
        animation = new Animation(1f / 4f, animationFrames);
        currentFrame = animation.getKeyFrame(0);

        this.sprite = new Sprite(currentFrame);
        sprite.setScale(2);
        sprite.setPosition(posX,posY);

    }

    @Override
    public void update(float delta) {
        elapsedTime += delta;
        this.currentFrame = animation.getKeyFrame(elapsedTime, true);
        this.sprite.setRegion(currentFrame);

    }

    @Override
    public void getHit(float damage, float pushAmount, float dirX, float dirY) {

    }
}
