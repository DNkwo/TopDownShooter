package com.scc210.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.scc210.game.CharacterController;
import com.scc210.game.Inventory;
import com.scc210.game.MathsClass;
import com.scc210.game.entities.weapons.SimpleGun;
import com.scc210.game.scenes.SceneBase;

import java.util.List;
import java.util.Random;

/**
 * Player class :)
 */
public class Player extends ControllableEntity{

    private SceneBase scene;
    private CharacterController control;
    private Inventory inventory;

    private float lastInteract = 99f;
    private float lastHitMeleeTimer = 99f;

    private Weapon weapon;

    private float speed, acceleration = 0.25f, dirX, dirY;

    private float totalEnergy, currentEnergy;
    private float outOfBreath;
    private boolean isSpinning = false;
    private float spinCycle = 0;

    private float ultimateAbilityAmount = 100;
    private float getUltimateAbilityAmountTotal = 100;
    private boolean isCastingUltimate = false;
    private float ultimateTimer = 0;
    private float ultimateLength = 3f;
    private float zoomOutTimer = 5f;
    private float zoomOutLength = 5f;
    private EnergyField energyField;


    private boolean canCollide = false;

    List<UpdatableEntity> updatableEntities;

    private Body body;

    //for camera shake shooting
    private Camera camera;
    private float cameraShakeTimer = 0;
    private float cameraShakePower = 1.5f;

    /**
     *
     * @param world Was for box2d stuff
     * @param scene Refers to GameArea
     * @param posX
     * @param posY
     * @param baseSpeed
     * @param camera
     * @param inventory Player inventory
     */
    public Player(World world, SceneBase scene, int posX, int posY, float baseSpeed, Camera camera, Inventory inventory){

        super(posX, posY, baseSpeed, 2500, 0);

        //get the scene to be able to access all level variables
        this.scene = scene;
        this.camera = camera;
        ((OrthographicCamera)camera).zoom = 1.4f;
        this.inventory = inventory;
        this.totalEnergy = 100;
        this.currentEnergy = this.totalEnergy;
        scale = 2;

        inventory.setPlayer(this);

        //set the player sprite
        Texture playerTexture = new Texture(Gdx.files.internal("img/player.png"));
        this.sprite = new Sprite(playerTexture);
        this.sprite.setScale(scale);
        this.updatableEntities = scene.getUpdatableEntities();

        this.width = sprite.getWidth();
        this.height = sprite.getHeight();



        //box 2d stuff
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

    /**
     *
     * @param delta
     */
    public void update(float delta){

        //set the position
        Vector3 pos = calculatePlayerPosition(delta);
        sprite.setPosition(pos.x, pos.y);


        //code for spinning
        if(isSpinning && currentEnergy > 0){
            spinCycle += delta * 360;
            currentEnergy -= delta * 15;
            if(currentEnergy < 0){
                isSpinning = false;
                outOfBreath = 0;
            }
            else {
                sprite.setRotation(sprite.getRotation() + delta * 1440);
                if (spinCycle >= 90) {
                    isSpinning = false;
                    spinCycle = 0;
                }
            }
        }
        else {
            sprite.setRotation(calculatePlayerAngle());
        }

//        body.setTransform(pos.x, pos.y, 0);

        /*gun logic*/
        if(weapon != null && weapon == inventory.getWeapon(inventory.getItemSelected())) {
            //shoot (can't be sprinting)
            if (weapon != null && control.shooting && !control.sprinting) {
                //if weapon melee
                if(weapon.getObjectId() == 34 && outOfBreath > 1f){
                    isSpinning = true;
                    weapon.Shoot();
                }
                else if (weapon.getObjectId() != 34) {
                    weapon.Shoot();
                    cameraShakeTimer = 0.25f;
                    cameraShakePower = 1.5f;
                }
            }

            if (cameraShakeTimer >= 0f) {
                cameraShakeTimer -= delta;
                Random random = new Random();
                float currentPower = cameraShakePower * ((1000 - cameraShakeTimer) / 1000);
                camera.translate((random.nextFloat() - 0.5f) * 2 * currentPower, (random.nextFloat() - 0.5f) * 2 * currentPower, 0);
            }
        }

        //attempt to interact

        //if player is interacting
        if (control.interacting && canInteract()){

            //if the player doesn't have a weapon
            if (weapon == null){

                //attempt to pick a weapon
                pickWeapon();

            }
            //if the player has a weapon
            else if (weapon != null){

                //drop it
                dropWeapon();


            }
        }

        if(control.castingUltimate && !isCastingUltimate){
            castUltimate();
        }

        //ultimate stuff

        if(ultimateTimer > 0){
            ((OrthographicCamera)camera).zoom =MathUtils.lerp(((OrthographicCamera)camera).zoom,0.88f, 1-ultimateTimer/ultimateLength);
            ultimateTimer -= delta;
            if(ultimateTimer <= 0){
                isCastingUltimate = false;
                energyField.alive = false;
                zoomOutTimer = 1f;
                zoomOutLength = 1f;
            }
        }

        if(zoomOutTimer > 0){
            zoomOutTimer -= delta;
            ((OrthographicCamera)camera).zoom = MathUtils.lerp(((OrthographicCamera)camera).zoom,1f, 1-zoomOutTimer/zoomOutLength);
        }

        lastHitMeleeTimer+= delta;

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
//        if(weapon.getObjectId() == 36){
//            damage *= 0.8;
//            pushAmount *= 0.1;
//        }

        currentHealth -= damage;
        if(currentHealth < 0){
            alive = false;
        }

        if(!isPushed) {
            this.dirX = dirX;
            this.dirY = dirY;
            isPushed = true;
            pushedSpeed = pushAmount;
        }

    }

    /**
     *
     * @return
     */
    private boolean willCollide(){

        //if player has collision enabled
        if (canCollide == false) return false;

        //get the level wall array
        int walls[][] = scene.getWalls();

        //if there aren't any walls
        if (walls == null) return false;

        //wall size in pixels
        int wallSize = 50;

        //booleans to check where the player is colliding
        boolean xCollision;
        boolean yCollision;

        //new wanted position for the player
        float possibleMovementX = (speed * dirX * Gdx.graphics.getDeltaTime());
        float possibleMovementY = (speed * dirY * Gdx.graphics.getDeltaTime());

        //player positions (Right, Left, Top and Bottom)
        float pR = posX + sprite.getWidth() + possibleMovementX;
        float pL = posX + possibleMovementX;
        float pT = posY + sprite.getHeight() + possibleMovementY;
        float pB = posY + possibleMovementY;

        for (int i = 0; i < walls.length; i++){

            //calculate wall X position in the world based on its size
            int wallPosX = i * wallSize;

            for (int j = 0; j < walls[0].length; j++){

                //calculate wall Y position in the world based on its size
                int wallPosY = j * wallSize;

                xCollision = false;
                yCollision = false;

                //if it is a wall
                if (walls[i][j] == 1) {

                    //wall positions (Right, Left, Top and Bottom)
                    float wR = wallPosX + wallSize;
                    float wL = wallPosX;
                    float wT = wallPosY + wallSize;
                    float wB = wallPosY;

                    //check left collision
                    if (pL < wR && pL > wL){
                        xCollision = true;
                    }
                    //check right collision
                    else if (pR > wL && pR < wR){
                        xCollision = true;
                    }

                    //check bot collision
                    if (pB < wT && pB > wB){
                        yCollision = true;
                    }
                    //check top collision
                    else if (pT > wB && pT < wT){
                        yCollision = true;
                    }

                    //if both collide it's a valid collision
                    if (xCollision && yCollision){
                        return true;
                    }

                }

            }
        }

        return false;
    }

    /**
     *
     * @param control
     */
    public void setController(CharacterController control) {
        this.control = control;
    }

    /**
     *
     * @param delta
     * @return
     */
    private Vector3 calculatePlayerPosition(float delta){

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
            //player speed
            speed = (float) baseSpeed;

            //possible directions
            if (control.down && dirY >= -1) dirY += -acceleration;
            if (control.up && dirY <= 1) dirY += acceleration;
            if (control.left && dirX >= -1) dirX += -acceleration;
            if (control.right && dirX <= 1) dirX += acceleration;

            if (!control.down && !control.up) dirY = 0;
            if (!control.right && !control.left) dirX = 0;


            //adapt vertical speed
            if (dirX != 0 && dirY != 0) {
                //hypotenuse multiplied by the relation between hypotenuse and the other sides
                speed = (float) baseSpeed * 0.71f;
            }


            //SPRINTING AND ENERGY MECHANICS



            if (outOfBreath < 1f) {
                outOfBreath += delta;
            } else {
                //check if player is sprinting
                if (control.sprinting && currentEnergy > 0) {
                    currentEnergy -= delta * 25;
                    speed = speed * 1.75f;
                    if (currentEnergy < 0) {
                        outOfBreath = 0;
                    }
                } else if (currentEnergy < totalEnergy) {
                    currentEnergy += delta * 15;
                }
            }

//        //check collision with walls
//        if (!willCollide()) {
//            //move
//            posX += speed * Gdx.graphics.getDeltaTime() * dirX;
//            posY += speed * Gdx.graphics.getDeltaTime() * dirY;
//        }


            //calculate new position
            posX += speed * Gdx.graphics.getDeltaTime() * dirX;
            posY += speed * Gdx.graphics.getDeltaTime() * dirY;
        }

        //wall collision
        for(UpdatableEntity entity : updatableEntities){
            if(entity.getObjectId() >= 21 && entity.getObjectId() <= 29){ //if wall
                if(MathsClass.isCollide(this, entity)){
                    if(isPushed){
                        posX -= pushedSpeed * Gdx.graphics.getDeltaTime() * dirX;
                        posY -= pushedSpeed * Gdx.graphics.getDeltaTime() * dirY;
                    }
                    else {
                        posX -= speed * Gdx.graphics.getDeltaTime() * dirX;
                        posY -= speed * Gdx.graphics.getDeltaTime() * dirY;
                    }
                }
            }
            //player collision with enemy
            if(entity.getObjectId() >= 10 && entity.getObjectId() <= 19){
                if(MathsClass.isCollide(this, entity) && lastHitMeleeTimer > 1f){
                    lastHitMeleeTimer = 0;
                    cameraShakeTimer = 0;
                    cameraShakePower = 3;
                    float relX = (entity.getPosX() - entity.getWidth() / 2 - posX - entity.getWidth() / 2);
                    float relY = (entity.getPosY() - entity.getHeight() / 2 - posY - entity.getHeight() / 2);
                    float radians = MathUtils.radiansToDegrees * MathUtils.atan2(-relY, -relX);
                    getHit(entity.getMeleeDamage(), entity.getPushAmount(), MathUtils.cos(radians),MathUtils.sin(radians));
                }
            }
        }


        //check collision with screen
        /*if(posX < 0) posX = 0;
        if(posX > 1200 - sprite.getWidth()) posX = 1200 - sprite.getWidth();

        if(posY < 0) posY = 0;
        if(posY > 800 - sprite.getHeight()) posY = 800 - sprite.getHeight();*/

        Vector3 pos = new Vector3(posX, posY, 0);

        return pos;
    }

    /**
     *
     * @return
     */
    private float calculatePlayerAngle(){
        //get mouse coordinates
        float mouseX = control.mouseX;
        float mouseY = control.mouseY;

        //get mouse position relative to the world
        Vector3 mouseInWorld = scene.getCamera().unproject(new Vector3(mouseX, mouseY, 0));

        //find difference between the player position and the mouse position
        float relX = mouseInWorld.x - posX - sprite.getWidth()/2;
        float relY = mouseInWorld.y - posY - sprite.getHeight()/2;

        //calculate angle between player and mouse
        float angle = MathUtils.radiansToDegrees * MathUtils.atan2(relY, relX);

        return angle;
    }

    /**
     *
     */
    private void pickWeapon(){

        //check every weapon
        for (Weapon w : scene.getWeaponList()) {

            //if weapon isn't owned
            if (w.owner == null){

                //if player colliding with weapon
                if (isColliding(w)) {

                    //acquire ownership
                    setWeapon(w);
                    lastInteract = TimeUtils.nanoTime();

                    //add to inventory
                    inventory.addWeapon(w);

                }
            }
        }
    }

    /**
     *
     */
    private void dropWeapon(){

        //tell the weapon that it doesn't have a owner
        weapon.owner = null;

        //nmake the player not have a weapon
        this.weapon = null;

        inventory.removeWeapon();
        lastInteract = TimeUtils.nanoTime();

    }

    /**
     *
     * @param weapon
     */
    public void setWeapon(Weapon weapon){

        if (weapon == null){
            dropWeapon();
            return;
        }

        this.weapon = weapon;
        weapon.owner = this;
    }

    /**
     *
     * @return
     */
    private boolean canInteract(){

        //be able to pick only every 0.15 seconds to prevent picking and dropping overlap
        if (!(TimeUtils.nanoTime() - lastInteract > 0.15 * 1000000000)) return false;

        return true;

    }


    //check if entity is colliding with current object

    /**
     *
     * @param target
     * @return
     */
    private boolean isColliding(UpdatableEntity target){

        //get target center position
        float targetCenterX = target.posX + target.sprite.getWidth()/2;
        float targetCenterY = target.posY + target.sprite.getHeight()/2;

        //booleans
        boolean checkX = false;
        boolean checkY = false;

        //check if target center between current object on X
        if (targetCenterX > posX && targetCenterX < posX + this.getWidth()){
            checkX = true;
        }
        //check if target center between current object on Y
        if (targetCenterY > posY && targetCenterY < posY + this.getHeight()){
            checkY = true;
        }

        //if both collide
        if (checkX && checkY) return true;

        return false;

    }

    /**
     *
     */
    public void castUltimate(){
        if(ultimateAbilityAmount >= getUltimateAbilityAmountTotal){
            isCastingUltimate = true;
            ultimateAbilityAmount = 0;
            energyField = new EnergyField(this, posX, posY, updatableEntities);
            scene.getAddToUpdatableEntities().add(energyField);
            ultimateTimer = ultimateLength;
            cameraShakeTimer += ultimateLength;
        }
    }

    /**
     *
     * @param b
     */
    public void canCollideWithWalls(boolean b){
        canCollide = b;
    }

    /**
     *
     * @return
     */
    public float getTotalEnergy() {
        return totalEnergy;
    }

    /**
     *
     * @param totalEnergy
     */
    public void setTotalEnergy(float totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    /**
     *
     * @return
     */
    public float getCurrentEnergy() {
        return currentEnergy;
    }

    /**
     *
     * @param currentEnergy
     */
    public void setCurrentEnergy(float currentEnergy) {
        this.currentEnergy = currentEnergy;
    }

    /**
     *
     * @param ultimateAbilityAmount
     */
    public void setUltimateAbilityAmount(float ultimateAbilityAmount) {
        this.ultimateAbilityAmount = ultimateAbilityAmount;
    }

    /**
     *
     * @return
     */
    public float getUltimateAbilityAmount() {
        return ultimateAbilityAmount;
    }

    /**
     *
     * @return
     */
    public float getGetUltimateAbilityAmountTotal() {
        return getUltimateAbilityAmountTotal;
    }
}
