package com.scc210.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.scc210.game.scenes.MainMenu;


/**
 * Initial class that contains the rendering variables needed for the rest of the game
 */
public class Game extends com.badlogic.gdx.Game {

    public SpriteBatch batch;
    public SpriteBatch hudSpriteBatch;
    public ShapeRenderer hudShapeBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont bitmapFont;
//    public World world;
//    public RayHandler rayHandler;
    public final int PPM = 100;

    /**
     * LibGDX function that executes when code is ran
     */

    public void loadResources(){
        batch = new SpriteBatch();
        hudSpriteBatch = new SpriteBatch();
        hudShapeBatch = new ShapeRenderer();
        shapeRenderer = new ShapeRenderer();
        bitmapFont = new BitmapFont();
//        world = new World(new Vector2(0,0), true);
//        rayHandler = new RayHandler(world);
    }
    public void create(){

        loadResources();
        setScreen(new MainMenu(this));

    }


    /**
     * LibGDX method to dispose of resources
     */
    public void dispose(){
        batch.dispose();
        shapeRenderer.dispose();
        bitmapFont.dispose();
        hudSpriteBatch.dispose();
        hudShapeBatch.dispose();
//        rayHandler.dispose();
//        world.dispose();
    }

}
