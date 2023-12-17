package com.scc210.game.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.scc210.game.scenes.SceneBase;

/**
 * Coin object for pickups
 */
public class Coin extends UpdatableEntity{

    private Animation<TextureRegion> animation;
    private Texture spritesheet;
    private TextureRegion[] animationFrames;
    private float elapsedTime;
    private TextureRegion currentFrame;
    private Player player;
    private float speed = 0;
    private SceneBase scene;

    /**
     * Coin constructor
     * @param posX - x position of the coin
     * @param posY - y position of the coin
     * @param scene - the scene that the coin is in
     * @param handler - the rayhandler used for lighting
     */
    public Coin(float posX, float posY, SceneBase scene, RayHandler handler, Player player) {
        super(posX, posY, 40, 0);
        this.scene = scene;


        spritesheet = new Texture(Gdx.files.internal("img/coin_spritesheet.png"));

        int ROW = 1, COLUMN = 6;
        TextureRegion[][] tmpFrames = TextureRegion.split(spritesheet, spritesheet.getWidth()/6, spritesheet.getHeight()/ROW);

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

        this.player = player;

    }

    @Override
    public void update(float delta) {
        speed += 5 * delta;
        this.sprite.setPosition(posX, posY);
        elapsedTime += delta;
        this.currentFrame = animation.getKeyFrame(elapsedTime, true);
        this.sprite.setRegion(currentFrame);

        if (player != null) {
            if (Math.abs(player.getPosX() - posX) < 50 && Math.abs(player.getPosY() - posY) < 50) {
                alive = false;
                scene.addScore(1);
                player.setUltimateAbilityAmount(player.getUltimateAbilityAmount()+ 5);
                if(player.getUltimateAbilityAmount() > player.getGetUltimateAbilityAmountTotal()){
                    player.setUltimateAbilityAmount(player.getGetUltimateAbilityAmountTotal());
                }
            }
        }
    }

    @Override
    public void getHit(float damage, float pushAmount, float dirX, float dirY) {

    }
}
