package com.scc210.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.scc210.game.CharacterController;
import com.scc210.game.Game;
import com.scc210.game.entities.*;
import com.scc210.game.entities.weapons.AssaultRifle;
import com.scc210.game.entities.weapons.Knife;
import com.scc210.game.entities.weapons.SimpleGun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestArea extends SceneBase {

    public TestArea(Game game){
        super(game);
    }

    //camera
    //protected OrthographicCamera camera;

    //controller
    CharacterController controller;

    //Entity list
    private List<UpdatableEntity> updatableEntities = new ArrayList<UpdatableEntity>();
    //private List<Weapon> weaponList = new ArrayList<Weapon>();

    //player
    private Player player;

    //weapons
    private SimpleGun simpleGun;
    private Knife knife;
    private AssaultRifle rifle;

    //walls
    private int wallSize;
    //private int[][] walls;

    public void show() {

        /*create objects*/

        //create camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800);

        //create controller
        controller = new CharacterController(this);
        Gdx.input.setInputProcessor(controller);

        /*create entities*/

        //create player
        //player = new Player(game.world, this, 1200/2, 900/2, 300, camera);
        player.setController(controller);
        updatableEntities.add(player);

        //create weapons

//        Texture simpleGunTexture = new Texture(Gdx.files.internal("img/basicGun.png"));
//        simpleGun = new SimpleGun(simpleGunTexture, 300, 300, updatableEntities);
//        updatableEntities.add(simpleGun);
//        weaponList.add(simpleGun);
//
//        Texture shotgunTexture = new Texture(Gdx.files.internal("img/shotgun.png"));
//        Shotgun shotgun = new Shotgun(shotgunTexture, 100, 100, updatableEntities);
//        updatableEntities.add(shotgun);
//        weaponList.add(shotgun);
//
//        Texture knifeTexture = new Texture(Gdx.files.internal("img/knife.png"));
//        Knife knife = new Knife(knifeTexture, 150, 150, updatableEntities);
//        updatableEntities.add(knife);
//        weaponList.add(knife);
//
//        Texture rifleTexture = new Texture(Gdx.files.internal("img/rifle.png"));
//        AssaultRifle rifle = new AssaultRifle(rifleTexture, 200, 200, updatableEntities);
//        updatableEntities.add(rifle);
//        weaponList.add(rifle);
          Texture shieldTexture = new Texture(Gdx.files.internal("img/Shield.png"));
          Shield shield = new Shield(shieldTexture, 200, 200, updatableEntities);
          updatableEntities.add(shield);
          weaponList.add(shield);

        //replace mouse
        Pixmap pm = new Pixmap(Gdx.files.internal("img/crosshair.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 16, 16));
        pm.dispose();

        /* create walls */

        //wall side size (cube)
        wallSize = 50;

        //division of the free space in a grid
        int xSpaces = (int)Math.ceil(Gdx.graphics.getWidth() / wallSize);
        int ySpaces = (int)Math.ceil(Gdx.graphics.getHeight() / wallSize);

        //wall array
        walls = new int[xSpaces][ySpaces];

        //create wall matrix
        for (int i = 0; i < walls.length; i++){
            for (int j = 0; j < walls[0].length; j++){

                //clear center of walls
                if (i > 9 && i < 15 && j > 4 && j < 10){
                    walls[i][j] = 0;
                }
                else{
                    walls[i][j] = new Random().nextInt(2);
                }
            }
        }

        //can the player collide with walls
        player.canCollideWithWalls(false);

    }

    public void render(float delta) {

        ScreenUtils.clear(0.38f, 0.47f, 0.49f, 1);

        update(delta);

        //start shape render
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        //loop trough grid matrix
        for (int i = 0; i < walls.length; i++){
            for (int j = 0; j < walls[0].length; j++){

                //if grid value is 0 don't paint
                if (walls[i][j] != 0) {

                    //if grid value is 1 change to white
                    if (walls[i][j] == 1) {
                        game.shapeRenderer.setColor(Color.WHITE);
                    }

                    //draw wall
                    game.shapeRenderer.rect(wallSize * i, wallSize * j, wallSize, wallSize);

                }

            }
        }

        game.shapeRenderer.end();

        //start batch
        game.batch.begin();

        //draw entities
        for (Entity e : updatableEntities){
            e.draw(game.batch);
        }

        //end batch
        game.batch.end();

    }

    public void update(float delta){

        //update camera
        //let camera follow player position lerp meaning linear interpololation
        float lerp = 0.98f;
        Vector3 position = camera.position;
        position.x += (player.getPosX() - position.x) * lerp * delta;
        position.y += (player.getPosY() - position.y) * lerp * delta;
        camera.position.set(position.x, position.y, 0);
        camera.update();
        /////////////////////////////////////////////////////////////////////


        //set camera as render
        game.batch.setProjectionMatrix(camera.combined);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        //update player movement
        //player.update();

        //update entities
        for (UpdatableEntity e : updatableEntities){
            e.update(delta);
        }
    }

    /* GETTERS and SETTERS */

    public int[][] getWalls() {
        return walls;
    }

    public List<Weapon> getWeaponList() {
        return weaponList;
    }

    @Override
    public void loadPause() {

    }
}
