package com.scc210.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.scc210.game.CharacterController;
import com.scc210.game.Game;
import com.scc210.game.Inventory;
import com.scc210.game.entities.*;
import com.scc210.game.entities.weapons.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is essentially the actual game arena.
 */

public class GameArea extends  SceneBase{

    private Game game;
    private int[][] level;
    private int[][][] levels;
    private int currentLevelValue;
    private Inventory playerInventory;

    /**
     *
     * @param game
     * @param levels Levels are loaded from here and generated using the level generator
     * @param currentLevel
     * @param playerInventory
     */
    public GameArea(Game game, int[][][] levels, int currentLevel, Inventory playerInventory){
        super(game);
        this.game = game;
        this.level = levels[currentLevel];
        this.levels = levels;
        this.currentLevelValue = currentLevel;
        this.playerInventory = playerInventory;
    }

    //controller
    CharacterController controller;


    //weapons
    private AssaultRifle simpleGun;


    //temporary place for textures
    private Texture playerTexture;
    private Sprite playerSprite;


    //health and energy bar
    private Texture playerBarTexture;
    private Sprite playerBarSprite;
    private Texture enemyBarTexture;

    //inventory
    private Texture inventoryTexture;
    private Sprite inventorySprite;
    private Texture itemSelectedTexture;
    private Sprite itemSelectedSprite;

    //pause menu
    private Stage stage;
    private Skin skin;

    //Player State
    private boolean isPlayerDead = false;
    private boolean stageCompleted = false;
    private float popUpTimer = 0f;
    private float timerToContemplateMistakes = 0f;
    private float timerToNextLevel = 0;


    /**
     * Loads all the game entities
     */

    public void show() {

        /*create objects*/

        //create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        //create controller
        controller = new CharacterController(this);
        Gdx.input.setInputProcessor(controller);

        //health and energy bar
        playerBarTexture = new Texture(Gdx.files.internal("img/player_bar.png"));
        playerBarSprite = new Sprite(playerBarTexture);
        playerBarSprite.setScale(2.8f,2.8f);
        playerBarSprite.setPosition(310,32);
        playerBarSprite.setColor(1,1,1,0.8f);
        enemyBarTexture = new Texture(Gdx.files.internal("img/enemy_healthbar.png"));


        //player Inventory
        if(playerInventory == null) {
            playerInventory = new Inventory(updatableEntities);
        }
        else{
            playerInventory.setUpdatableEntities(updatableEntities);
            playerInventory.reload();
        }

        inventoryTexture = new Texture(Gdx.files.internal("img/inventory_main.png"));
        inventorySprite = new Sprite(inventoryTexture);
        inventorySprite.setScale(3);
        inventorySprite.setColor(1, 1, 1, 0.8f);
        inventorySprite.setPosition(940, 32);

        itemSelectedTexture = new Texture(Gdx.files.internal("img/itemSelected.png"));
        itemSelectedSprite = new Sprite(itemSelectedTexture);
        itemSelectedSprite.setColor(1,1,1,0.8f);
        itemSelectedSprite.setPosition(940,35);
        itemSelectedSprite.setScale(3);




            //create player ( for now, place before spawning everything else otherwise null error)
        player = new Player(null, this, 1200 / 2, 900 / 2, 200, camera, playerInventory);
        player.setController(controller);




        /*create entities*/


        generateLevel(level);

        updatableEntities.add(player);


        //ADD SIMPLE GUN IF PLAYER INVENTORY IS EMPTY
        if(playerInventory.isEmpty()) {
            Texture basicGunTexture = new Texture(Gdx.files.internal("img/basicGun.png"));
            SimpleGun basicGun = new SimpleGun(basicGunTexture, 0, 0, updatableEntities, null);
            weaponList.add(basicGun);
            playerInventory.addWeapon(basicGun);
            player.setWeapon(basicGun);
            basicGun.setOwner(player);
            updatableEntities.add(basicGun);
        }


        //replace mouse
        Pixmap pm = new Pixmap(Gdx.files.internal("img/crosshair.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 16, 16));
        pm.dispose();

        playerTexture = new Texture(Gdx.files.internal("img/player.png"));
        playerSprite = new Sprite(playerTexture);
        playerSprite.setPosition(32,64);
        playerSprite.setScale(3);

        //light
        //game.rayHandler.setAmbientLight(.55f);

        stage = new Stage();

        skin = new Skin(Gdx.files.internal("skins/neon-ui.json"));



    }


    /**
     * Uses switch case to generate level entities
     * @param level current level 2d array
     */

    public void generateLevel(int[][] level){
        List<UpdatableEntity> tiles = new ArrayList<>();
        List<UpdatableEntity> enemies = new ArrayList<>();
        List<UpdatableEntity> weapons = new ArrayList<>();
        for(int y = 0; y < level.length; y++){
            for (int x = 0; x < level[y].length; x++){

                //basic tile
                tiles.add(new Tile(null, x*64,y*64, 20));
                switch(level[y][x]){

                    //basic tile1
                    case 1:
                        tiles.add(new Tile(null,x*64,y*64, 21));
                        break;
                    //breakable tile
                    case 2:
                        tiles.add(new Tile(null,x*64,y*64, 22));
                        break;
                    //melee zombie
                    case 10:
                        Enemy zombieMelee = new Enemy(null,this, x*64, y*64, 250, 100, 10, updatableEntities, null);
                        enemies.add(zombieMelee);
                        enemyList.add(zombieMelee);
                        break;
                    //zombie with pistol
                    case 11:
                        Enemy zombieSimpleGun = new Enemy(null,this, x*64, y*64, 500, 50, 10, updatableEntities, null);
                        enemies.add(zombieSimpleGun);
                        enemyList.add(zombieSimpleGun);
                        Texture simpleGunTexture = new Texture(Gdx.files.internal("img/basicGun.png"));
                        SimpleGun simpleGun = new SimpleGun(simpleGunTexture, x*64, y*64, updatableEntities, null);
                        zombieSimpleGun.setWeapon(simpleGun);
                        simpleGun.setOwner(zombieSimpleGun);
                        weapons.add(simpleGun);
                        break;
                    //zombie with shotgun
                    case 12:
                        Enemy zombieShotGun = new Enemy(null,this, x*64, y*64, 500, 25, 10, updatableEntities, null);
                        enemies.add(zombieShotGun);
                        enemyList.add(zombieShotGun);
                        Texture shotGunTexture = new Texture(Gdx.files.internal("img/shotgun.png"));
                        Shotgun shotgun = new Shotgun(shotGunTexture, x*64, y*64, updatableEntities, null);
                        zombieShotGun.setWeapon(shotgun);
                        shotgun.setOwner(zombieShotGun);
                        weapons.add(shotgun);
                        break;
                    //melee alien
                    case 13:
                        Enemy alienMelee = new Enemy(null,this, x*64, y*64, 250, 100, 11, updatableEntities, null);
                        enemyList.add(alienMelee);
                        enemies.add(alienMelee);
                        break;
                    //torch
                    case 41:
                        addToUpdatableEntities.add(new Torch(null, x*64,y*64));
                        break;
                    //beacon
                    case 42:
                        int[] enemyValues = {0,0,1,1};
                        EnemyBeacon beacon = new EnemyBeacon(null,x*64,y*64, enemyValues, this, null);
                        addToUpdatableEntities.add(beacon);
                        enemyList.add(beacon);
                        break;
                    //boss
                    case 90:
                        Enemy zombieBoss = new Enemy(null,this, x*64, y*64, 250, 100, 90, updatableEntities, null);
                        enemies.add(zombieBoss);
                        break;
                    //player
                    case 100:
                        player.setPosX(x*64);
                        player.setPosY(y*64);
                        break;
                    case 30:
                        Texture gun1Tex = new Texture(Gdx.files.internal("img/basicGun.png"));
                        SimpleGun gun1 = new SimpleGun(gun1Tex, x*64, y*64, updatableEntities, null);
                        weaponList.add(gun1);
                        weapons.add(gun1);
                        break;
                    case 31:
                        Texture gun2Tex = new Texture(Gdx.files.internal("img/shotgun.png"));
                        Shotgun gun2 = new Shotgun(gun2Tex, x*64, y*64, updatableEntities, null);
                        weaponList.add(gun2);
                        weapons.add(gun2);
                        break;
                    case 32:
                        Texture gun3Tex = new Texture(Gdx.files.internal("img/ray-gun.png"));
                        RayGun gun3 = new RayGun(gun3Tex, x*64, y*64, updatableEntities, null);
                        weaponList.add(gun3);
                        weapons.add(gun3);
                        break;
                    case 33:
                        Texture gun4Tex = new Texture(Gdx.files.internal("img/rifle.png"));
                        AssaultRifle gun4 = new AssaultRifle(gun4Tex, x*64, y*64, updatableEntities, null);
                        weaponList.add(gun4);
                        weapons.add(gun4);
                        break;
                    case 34:
                        Texture knifeTex = new Texture(Gdx.files.internal("img/knife.png"));
                        Knife knife = new Knife(knifeTex, x*64, y*64, updatableEntities);
                        weaponList.add(knife);
                        weapons.add(knife);
                        break;

                }
            }
        }

        //place in order
        updatableEntities.addAll(tiles);
        updatableEntities.addAll(enemies);
        updatableEntities.addAll(weapons);

    }

    /**
     * Render method for the scene. Renders all the updatable entities. and user interface
     * @param delta change in time per frame
     */
    public void render(float delta) {

        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        game.batch.enableBlending();


        if(!this.isPaused()) {
            update(delta);
        }

        //start batch
        game.batch.begin();

        //draw entities
        for (Entity e : updatableEntities){
            e.draw(game.batch);
        }

        game.batch.end();


        game.batch.begin();

        //DRAWING HEALTH RELATED UI STUFF
        for (UpdatableEntity e : updatableEntities) {
            if (e.getObjectId() >= 10 && e.getObjectId() <= 19 && e.getCurrentHealth() > 0) {
                //game.bitmapFont.draw(game.batch, e.getName(), e.getPosX() - e.getWidth() / 2, e.getPosY());
                Sprite enemyBarSprite = new Sprite(enemyBarTexture);
                enemyBarSprite.setColor(1,1,1,0.8f);
                enemyBarSprite.setScale(0.6f,0.8f);
                enemyBarSprite.setPosition(e.getPosX()-e.getWidth()*1.6f,e.getPosY()+e.getHeight()*1.4f);
                enemyBarSprite.draw(game.batch);

                //health
            }
        }

        game.batch.end();



        //start shape render
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        for (UpdatableEntity e: updatableEntities){
            if((e.getObjectId() >= 10 && e.getObjectId() <= 19)  && e.getCurrentHealth() > 0){
                game.shapeRenderer.setColor(0.3f ,   0.7f , 0.25f, 0.7f);
                game.shapeRenderer.rect(e.getPosX() - e.getWidth()*0.75f, e.getPosY()+e.getHeight()*1.5f, (e.getCurrentHealth() / e.getTotalHealth()) * 72, 10);
            }
        }

        game.shapeRenderer.end();

        game.batch.begin();

        for (UpdatableEntity e : updatableEntities) {
            if ((e.getObjectId() >= 12 && e.getObjectId() <= 19)&& e.getCurrentHealth() > 0) {
                game.bitmapFont.getData().setScale(0.8f);
                //health
                game.bitmapFont.draw(game.batch, (int)(e.getCurrentHealth()) + "/" + (int)(e.getTotalHealth()), e.getPosX() - e.getWidth() / 2, e.getPosY() + e.getHeight() * 1.8f);
            }
        }
        game.batch.end();

        //game.rayHandler.render();





        game.hudSpriteBatch.begin();
        playerBarSprite.draw(game.hudSpriteBatch);
        playerSprite.draw(game.hudSpriteBatch);
        playerSprite.setRotation(player.getSprite().getRotation());
        inventorySprite.draw(game.hudSpriteBatch);
        itemSelectedSprite.draw(game.hudSpriteBatch);


        for(int i = 0; i < playerInventory.getWeapons().length; i++){
            if(playerInventory.getWeapons()[i] != null){
                Sprite item = new Sprite(playerInventory.getWeapons()[i].getSprite().getTexture());
                item.setScale(3);
                item.setPosition(856+(i*96), 40);
                item.setRotation(45);
                item.draw(game.hudSpriteBatch);
            }
        }
        game.hudSpriteBatch.end();

        //HUD UI
        game.hudShapeBatch.begin(ShapeRenderer.ShapeType.Filled);
//        game.hudShapeBatch.setColor(1 ,   0 , 0, 0.5f);
//        game.hudShapeBatch.circle(64,64,100);


        //player health
        if(player != null) {
            if(player.getCurrentHealth() > 0) {
                game.hudShapeBatch.setColor(1 - (player.getCurrentHealth() / player.getTotalHealth()), 0.5f*(player.getCurrentHealth() / player.getTotalHealth()), 0.1f, 0.5f);
                game.hudShapeBatch.rect(88, 36, (player.getCurrentHealth() / player.getTotalHealth()) * 700, 52);
            }
            game.hudShapeBatch.setColor(1, 0.8f, 0.3f, 0.5f);
            game.hudShapeBatch.rect(88, 4, (player.getCurrentEnergy() / player.getTotalEnergy()) * 400, 24);
            game.hudShapeBatch.setColor(0, 0.7f, 0.8f, 0.5f);
            game.hudShapeBatch.rect(508, 4, (player.getUltimateAbilityAmount() / player.getGetUltimateAbilityAmountTotal()) * 275, 24);
        }

        game.hudShapeBatch.end();

        game.hudSpriteBatch.begin();

                game.bitmapFont.getData().setScale(2f);
                //health
                game.bitmapFont.draw(game.hudSpriteBatch, (int)(player.getCurrentHealth()) + "/" + (int)(player.getTotalHealth()), 325, 74);

        game.hudSpriteBatch.end();

        if(isPlayerDead){

            game.hudShapeBatch.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.hudShapeBatch.setColor(0.2f, 0, 0, 1-(popUpTimer/3));
            game.hudShapeBatch.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            game.hudShapeBatch.end();

            game.hudSpriteBatch.begin();

            game.bitmapFont.getData().setScale(5f);
            //health
            game.bitmapFont.draw(game.hudSpriteBatch, "You have died.", Gdx.graphics.getWidth()/2-225, Gdx.graphics.getHeight()/2);
            game.hudSpriteBatch.end();

        }

        if(stageCompleted){
            game.hudSpriteBatch.begin();

            game.bitmapFont.getData().setScale(5f);
            //health
            game.bitmapFont.setColor(1f, 1, 1, 1-((timerToNextLevel-2)/3));
            game.bitmapFont.draw(game.hudSpriteBatch, "Stage Completed", Gdx.graphics.getWidth()/2-225, Gdx.graphics.getHeight()/2);
            game.hudSpriteBatch.end();
        }


        if(isPaused()){
            Gdx.gl.glEnable(GL20.GL_BLEND);
            game.hudShapeBatch.begin(ShapeRenderer.ShapeType.Filled);
            game.hudShapeBatch.setColor(0,0,0,0.5f);
            game.hudShapeBatch.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            game.hudShapeBatch.end();
            Gdx.input.setInputProcessor(stage);
            stage.draw();
        }


    }


    /**
     * Update method updates all the updateable entities
     * @param delta
     */

    public void update(float delta){

        //game.rayHandler.update();


        //update camera
        //let camera follow player position lerp meaning linear interpololation
        float lerp = 0.99f;
        Vector3 position = camera.position;
        position.x += (player.getPosX() - position.x) * lerp * delta;
        position.y += (player.getPosY() - position.y) * lerp * delta;
        camera.position.set(position.x, position.y, 0);
        camera.update();
        /////////////////////////////////////////////////////////////////////


        //set camera as render
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        //update entities
        List<UpdatableEntity> deadEntity = new ArrayList<UpdatableEntity>();
        for (UpdatableEntity e : updatableEntities){
            e.update(delta);

            if(!e.isAlive()){
                deadEntity.add(e);
            }
        }
        updatableEntities.removeAll(deadEntity); //removes all dead instances

        //inventory
        playerInventory.setItemSelected(controller.inventorySelect);

        for(int i = 0; i < 4; i++){
            if(i == controller.inventorySelect){
                itemSelectedSprite.setPosition(844 + i*96, 32);
            }
        }

        if(player != null && !isPlayerDead && player.getCurrentHealth() < 0){
            isPlayerDead = true;
            popUpTimer = 3f;
            timerToContemplateMistakes = 5f;
        }

        if(player != null && !stageCompleted &&  enemyList.size() == 0){
            stageCompleted = true;
            timerToNextLevel = 5f;
        }

        if(popUpTimer > 0){
            popUpTimer -= delta;
        }

        if(timerToNextLevel > 0){
            timerToNextLevel -= delta;
            if(timerToNextLevel <= 0){
                game.dispose();
                game.loadResources();
                int nextLevel = currentLevelValue+1;
                if(nextLevel < levels.length) {
                    game.setScreen(new GameArea(game, levels, currentLevelValue + 1, playerInventory));
                }
                else{
                    game.setScreen(new LevelSelect(game));
                }
            }
        }

        if(timerToContemplateMistakes > 0) {
            timerToContemplateMistakes -= delta;
            if (timerToContemplateMistakes <= 0) {
                updatableEntities.clear();
                game.dispose();
                game.loadResources();
                game.setScreen(new LevelSelect(game));
            }
        }



        updatableEntities.removeAll(removeFromUpdatableEntities);
        updatableEntities.addAll(addToUpdatableEntities);
        addToUpdatableEntities.clear();
        removeFromUpdatableEntities.clear();




        //game.rayHandler.setCombinedMatrix(camera.combined, camera.position.x, camera.position.y, camera.viewportWidth * game.PPM, camera.viewportHeight * game.PPM);
    }

    /* GETTERS and SETTERS */

    public int[][] getWalls() {
        return walls;
    }

    public List<Weapon> getWeaponList() {
        return weaponList;
    }

    /**
     * Generates pause menu
     */
    public void loadPause() {
        if (isPaused()) {
            Gdx.app.log("",""+isPaused());
            TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
            btnStyle.font = game.bitmapFont;

            //test button
            TextButton testAreaBtn = new TextButton("Resume", skin, "default");
            testAreaBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    pause();
                    Gdx.input.setInputProcessor(controller);
                }
            });

            //exit button
            TextButton exitButton = new TextButton("Leave Game", skin, "default");
            exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    game.dispose();
                    game.loadResources();
                    game.setScreen(new MainMenu(game));
                }
            });

            stage.addListener(new InputListener() {
                @Override
                public boolean keyDown(InputEvent event, int keycode) {
                    if (keycode == Input.Keys.ESCAPE) {
                        pause();
                        Gdx.input.setInputProcessor(controller);
                    }
                    return false;
                }
            });

            Table table = new Table();
            table.setFillParent(true);
            //table.setDebug(true);

            table.add(testAreaBtn).width(Gdx.graphics.getWidth() / 5);
            table.row();

            table.add(exitButton).width(Gdx.graphics.getWidth() / 5);

            stage.addActor(table);
        }
    }


}
