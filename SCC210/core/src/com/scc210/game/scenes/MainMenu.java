package com.scc210.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.scc210.game.Game;

/**
 * MainMenu class that uses the ScreenAdapter to render a initial menu
 */
public class MainMenu extends ScreenAdapter {

    private Game game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundImage;

    /**
     * Initialize menu
     * @param game game variable that contains the render variables
     */
    public MainMenu(Game game){
        this.game = game;
    }

    /**
     * LibGDX method that executes when class is created
     */
    public void show(){

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/neon-ui.json"));

        backgroundImage = new Texture("img/background.fw.png");

        createStage();

    }

    /**
     * Method to create Buttons outside the show() method to improve readability and structure
     */
    private void createStage(){

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = game.bitmapFont;

        //test button
        TextButton testAreaBtn = new TextButton("Level Select", skin, "default");
        testAreaBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                game.setScreen(new LevelSelect(game));
            }
        });

        //enemy area button
        TextButton enemyTestArea = new TextButton("Controls", skin, "default");
        enemyTestArea.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                game.setScreen(new Controls(game));
            }
        });

        //exit button
        TextButton exitButton = new TextButton("Exit game", skin, "default");
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);

        table.add(testAreaBtn).width(Gdx.graphics.getWidth()/5);
        table.row();

        table.add(enemyTestArea).width(Gdx.graphics.getWidth()/5);
        table.row();

        table.add(exitButton).width(Gdx.graphics.getWidth()/5);

        stage.addActor(table);
    }

    /**
     * LibGDX method that tries to run as many times as it can (frames)
     * @param delta time passed since last frame
     */
    public void render(float delta){

        ScreenUtils.clear(Color.BLACK);

        game.batch.begin();

        game.batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.end();

        stage.draw();

    }

}
