package com.scc210.game.entities;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.scc210.game.MathsClass;
import com.scc210.game.entities.weapons.SimpleGun;
import com.scc210.game.scenes.SceneBase;

import java.util.List;
import java.util.Random;

/**
 * Base enemy functionality Class
 */
public class Enemy extends ControllableEntity{

    private SceneBase scene;
    private float baseSpeed;
    private Player player;
    private List<UpdatableEntity> updatableEntities;
    private Weapon weapon;
    private RayHandler handler;

    private float dirX = 0, dirY = 0;
    private float acceleration = 0.3f;

    private Body body;
    private World world;

    //when hit
    protected float hurtTimer = 0, hurtDuration = 0.25f;

    //death animation
    private float deathTimer = 0;
    private boolean dying = false;


    //weapon stats
    private long lastShot = System.currentTimeMillis();

    /**
     * Class constructor
     * @param world -
     * @param scene -
     * @param posX - x position
     * @param posY - y position
     * @param health - entity health
     * @param baseSpeed - entity speed
     * @param objectId - objectID
     * @param updatableEntities - the entity list used in the current level
     * @param rayHandler - rayhandler used for lighting
     */
    public Enemy(World world, SceneBase scene, int posX, int posY, float health, float baseSpeed, int objectId, List<UpdatableEntity> updatableEntities, RayHandler rayHandler){

        super(posX, posY, baseSpeed, health, objectId);
        this.handler = handler;

        this.scene = scene;
        this.world = world;

        this.player = scene.getPlayer();

        init();
        this.updatableEntities = updatableEntities;
        this.totalHealth = health;
        this.baseSpeed = baseSpeed;

//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.position.set(sprite.getX() , sprite.getY());
////
//        body = world.createBody(bodyDef);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width/2, height/2);
//
//        body.createFixture(shape, 1.0f);
//        shape.dispose();

    }

    private void init(){
        switch(objectId){
            case 10: //Basic Zombie
                //set the enemy sprite
                Texture zombieTexture = new Texture(Gdx.files.internal("img/enemy2.png"));
                this.sprite = new Sprite(zombieTexture);
                this.sprite.setScale(2);
                this.width = sprite.getWidth();
                this.height = sprite.getHeight();
                meleeDamage = 200;
                pushAmount = 200;
                this.name = "[LEVEL 1] Zombie";
                break;
            case 11: //Basic Zombie
                //set the enemy sprite
                Texture alienTexture = new Texture(Gdx.files.internal("img/alien.png"));
                this.sprite = new Sprite(alienTexture);
                this.sprite.setScale(2);
                this.width = sprite.getWidth();
                this.height = sprite.getHeight();
                meleeDamage = 200;
                pushAmount = 200;
                this.name = "[LEVEL 2] Alien";
                break;
            case 12:
                Texture zombieBossTexture = new Texture(Gdx.files.internal("img/zombieMiniB.png"));
                this.sprite = new Sprite(zombieBossTexture);
                this.sprite.setScale(4);
                this.width = sprite.getWidth();
                this.height = sprite.getHeight();
                meleeDamage = 500;
                pushAmount = 600;
                this.name = "[LEVEL 3] Zombie Boss";
                break;
            case 13:
                Texture alienBossTexture = new Texture(Gdx.files.internal("img/alienBoss.png"));
                this.sprite = new Sprite(alienBossTexture);
                this.sprite.setScale(12);
                this.width = sprite.getWidth();
                this.height = sprite.getHeight();
                meleeDamage = 500;
                pushAmount = 800;
                this.name = "[LEVEL 5] Alien Boss";
                break;
        }

    }


    public void update(float delta) {

        sprite.setPosition(posX, posY);

        //follow player code (just testing)

        if(!dying) {
            if (player != null) {


                if(isPushed){
                    pushedSpeed -= delta * 450;
                    posX += pushedSpeed * Gdx.graphics.getDeltaTime() * dirX;
                    posY += pushedSpeed * Gdx.graphics.getDeltaTime() * dirY;
                    if(pushedSpeed <= 0){
                        isPushed = false;
                        dirX = 0;
                        dirY = 0;
                    }
                }
                else {
                    //move towards player
                    if (posX < player.getPosX() && dirX <= 1) {
                        dirX += acceleration;
                    }
                    if (posX > player.getPosX() && dirX >= -1) {
                        dirX += -acceleration;
                    }
                    if (posY < player.getPosY() && dirY <= 1) {
                        dirY += acceleration;
                    } else if (posY > player.getPosY() && dirY >= -1) {
                        dirY += -acceleration;
                    }


                    //prevent the enemy from getting too close
//                    double distance = Math.sqrt(Math.pow((posX - player.posX), 2) + Math.pow((posY - player.posY), 2));
//                    if (distance < player.getHeight() + sprite.getHeight()) {
//                        dirX = 0;
//                        dirY = 0;
//                    }

                    //calculate new position
                    posX += baseSpeed * Gdx.graphics.getDeltaTime() * dirX;
                    posY += baseSpeed * Gdx.graphics.getDeltaTime() * dirY;
                }

                //wall collision
                for (UpdatableEntity entity : updatableEntities) {
                    if (entity.getObjectId() >= 21 && entity.getObjectId() <= 29) { //if wall
                        if (MathsClass.isCollide(this, entity)) {
                            if(isPushed){
                                posX -= pushedSpeed * Gdx.graphics.getDeltaTime() * dirX;
                                posY -= pushedSpeed * Gdx.graphics.getDeltaTime() * dirY;
                            }
                            else {
                                posX -= baseSpeed * Gdx.graphics.getDeltaTime() * dirX;
                                posY -= baseSpeed * Gdx.graphics.getDeltaTime() * dirY;
                            }
                        }
                    }
                }

                //angle towards player
                //find difference between the player position and the mouse position
                float relX = (player.getPosX() - player.getWidth() / 2 - posX - player.getWidth() / 2);
                float relY = (player.getPosY() - player.getHeight() / 2 - posY - player.getHeight() / 2);
                //calculate angle between player and mous


                float end = MathUtils.radiansToDegrees * MathUtils.atan2(relY, relX);
                float start = this.sprite.getRotation();

                float angle = ((((end - start) % 360) + 540) % 360) - 180; //rotational interpolaion
                sprite.rotate(angle * 0.025f);

//                body.setTransform(posX, posY, sprite.getRotation());

                hurtTimer += delta;
                if(hurtTimer < hurtDuration){
                    sprite.setColor(0.8f,0.4f,0.4f,1);
                }
                else{
                    sprite.setColor(1,1,1,1);
                }
            }

            Random rand = new Random();
            if (weapon != null && (System.currentTimeMillis() - lastShot > (1000 + rand.nextInt(4000)))) {
                lastShot = System.currentTimeMillis();
                weapon.Shoot();
            }
        }
        else{
            float deathDuration = 0.5f;
            deathTimer += delta;
            sprite.setColor(1,1,1,1-deathTimer/deathDuration);
            if(deathTimer > deathDuration){
                alive = false;
                scene.removeFromEnemyList(this);
            }
        }



    }

    @Override
    public void getHit(float damage, float pushAmount, float dirX, float dirY) {
//        if(hurtTimer < hurtDuration) { //if not ready to be hit again
//            return;
//        }
        currentHealth -= damage;
        hurtTimer = 0;

        //getting pushed so opposite
        if(!isPushed) {
            this.dirX = dirX;
            this.dirY = dirY;
            isPushed = true;
            pushedSpeed = pushAmount;
        }

        if(currentHealth <= 0){
            dying = true;
            if(weapon != null){
                //add to the top of updatable entities list
                scene.getRemoveFromUpdatableEntities().add(weapon);
                //add to the floor
                weapon.setOwner(null);
                scene.getWeaponList().add(weapon);
                scene.getAddToUpdatableEntities().add(weapon);
            }
            scene.getAddToUpdatableEntities().add(new Coin(posX,posY, scene, handler, player));
//            world.destroyBody(body);
        }
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }



}

