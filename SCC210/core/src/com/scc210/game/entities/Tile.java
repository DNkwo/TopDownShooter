package com.scc210.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

/**
 * Tile class, also generates flooring
 */
public class Tile extends UpdatableEntity{

    private Body body;
    private World world;

    //when hit
    protected float hurtTimer = 0, hurtDuration = 0.25f;

    /**
     *
     * @param world
     * @param posX
     * @param posY
     * @param objectId
     */
    public Tile(World world, float posX, float posY, int objectId) {
        super(posX, posY, objectId, 0);

        this.world = world;

        //set the tile sprite
        init();
        scale = 2;
        this.sprite.setScale(scale);

        this.width = sprite.getWidth()*scale;
        this.height = sprite.getHeight()*scale;

        sprite.setPosition(posX,posY);


    }

    /**
     *
     */
    private void init(){
        switch(objectId){
            case 20:
                Texture basicFloorTexture = new Texture(Gdx.files.internal("img/basicFloorTile.png"));
                this.sprite = new Sprite(basicFloorTexture);
                this.sprite.setColor(0.8f,0.7f,0.8f, 1);
                break;
            case 21:
                Texture basicTileTexture = new Texture(Gdx.files.internal("img/basicTile2.png"));
                this.sprite = new Sprite(basicTileTexture);
//                BodyDef bodyDef = new BodyDef();
//                bodyDef.type = BodyDef.BodyType.StaticBody;
//                bodyDef.position.set(sprite.getX() , sprite.getY());
////
//                body = world.createBody(bodyDef);
//
//                PolygonShape shape = new PolygonShape();
//                shape.setAsBox(sprite.getX()/2, sprite.getY()/2);
//
//                body.createFixture(shape, 1.0f);
//
//                body.setTransform(posX, posY, 0);
//                shape.dispose();
                break;
            case 22:
                this.totalHealth = 450;
                this.currentHealth = totalHealth;
                Texture destroyableTileTexture = new Texture(Gdx.files.internal("img/destroyable_tile.png"));
                this.sprite = new Sprite(destroyableTileTexture);
                break;

        }
    }

    /**
     *
     * @param delta
     */
    @Override
    public void update(float delta) {
        if(objectId == 22) {
            hurtTimer += delta;
            if (hurtTimer < hurtDuration) {
                sprite.setColor(0.8f, 0.4f, 0.4f, 1);
            } else {
                sprite.setColor(1, 1, 1, 1);
            }
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
        if (objectId == 22) {
            if (hurtTimer < hurtDuration) { //if not ready to be hit again
                return;
            }
            currentHealth -= damage;
            if(currentHealth < 0){
                alive = false;
            }
            hurtTimer = 0;
        }
    }
}
