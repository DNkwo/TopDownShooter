package com.scc210.game.entities;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.scc210.game.scenes.SceneBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Responsible for spawining enemies from it
 */
public class EnemyBeacon extends UpdatableEntity {

    private Animation<TextureRegion> animation;
    private Texture spritesheet;
    private TextureRegion[] animationFrames;
    private float elapsedTime;
    private TextureRegion currentFrame;

    //spawntimer
    protected float spawnTimer = 3;
    private int[] enemies;

    //when hit
    protected float hurtTimer = 0, hurtDuration = 0.25f;

    private RayHandler handler;
    private PointLight light;

    private SceneBase scene;

    private World world;

    /**
     *
     * @param handler
     * @param posX
     * @param posY
     * @param enemies
     * @param scene
     * @param world
     */
    public EnemyBeacon(RayHandler handler, float posX, float posY, int[] enemies, SceneBase scene, World world) {
        super(posX, posY, 42, 0);

//        light = new PointLight(handler, 100, Color.PURPLE, 450, posX, posY);
//        light.setSoftnessLength(0);

        spritesheet = new Texture(Gdx.files.internal("img/beacon_spritesheet.png"));

        this.world = world;
        this.scene = scene;
        this.enemies = enemies;
        this.handler = handler;

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
        animation = new Animation(1f / 4f, animationFrames);
        currentFrame = animation.getKeyFrame(0);

        this.sprite = new Sprite(currentFrame);
        this.totalHealth = 1000;
        this.currentHealth = totalHealth;
        sprite.setScale(4f);
        this.width = sprite.getWidth()*4;
        this.height = sprite.getHeight()*4;
        sprite.setPosition(posX, posY);


    }

    /**
     *
     * @param index
     * @return
     */
    public Enemy loadEnemy(int index){
        Enemy enemy = null;
        switch (index){
            case 0:
                enemy = new Enemy(world,scene, (int)posX, (int)posY, 250, 100, 10, scene.getUpdatableEntities(), handler);
                break;
            case 1:
                enemy = new Enemy(world,scene, (int)posX, (int)posY, 250, 100, 11, scene.getUpdatableEntities(), handler);
                break;
        }

        if(enemy == null){
            enemy = new Enemy(world,scene, (int)posX, (int)posY, 250, 100, 10, scene.getUpdatableEntities(), handler);
        }

        return enemy;
    }

    @Override
    public void update(float delta) {
        elapsedTime += delta;
        this.currentFrame = animation.getKeyFrame(elapsedTime, true);
        this.sprite.setRegion(currentFrame);

        if(spawnTimer > 0f && enemies != null){
            spawnTimer -= delta;
            if(spawnTimer <= 0 && enemies.length > 0) {
                spawnEnemy();
                spawnTimer = 3f;
            }
        }

        hurtTimer += delta;
        if (hurtTimer < hurtDuration) {
            sprite.setColor(0.4f, 0.4f, 0.4f, 1);
        } else {
            sprite.setColor(1, 1, 1, 1);
        }

    }

    /**
     *
     * @param damage - damage taken
     * @param pushAmount - float value used to "knock-back" the entity
     * @param dirX - x direction of knock back
     * @param dirY - y direction of knock back
     */
    @Override
    public void getHit(float damage, float pushAmount, float dirX, float dirY) {
        if (hurtTimer < hurtDuration) { //if not ready to be hit again
            return;
        }
        currentHealth -= damage;
        if(currentHealth < 0){
            alive = false;
            scene.removeFromEnemyList(this);
        }
        hurtTimer = 0;
    }

    /**
     *
     */
    public void spawnEnemy(){
        Random random = new Random();


        int index = random.nextInt(enemies.length);

        Enemy enemy = loadEnemy(index);

        scene.getAddToUpdatableEntities().add(enemy);
        scene.addToEnemyList(enemy);
    }
}