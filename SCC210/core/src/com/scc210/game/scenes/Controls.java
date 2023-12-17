package com.scc210.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.scc210.game.Game;

public class Controls extends ScreenAdapter {

    private Game game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundImage;

    private final int levelAmount = 10;
    private TextButton[] levelButtons = new TextButton[levelAmount];
    private int[][][] LevelList = new int[levelAmount][][];

    /**
     * Initialize menu
     *
     * @param game game variable that contains the render variables
     */
    public Controls(Game game) {
        this.game = game;
    }


    /**
     * LibGDX method that executes when class is created
     */
    public void show() {

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("skins/neon-ui.json"));

        backgroundImage = new Texture("img/tutorialPage.png");

        createStage();

    }

    /**
     * Method to create Buttons outside the show() method to improve readability and structure
     */
    private void createStage() {

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = game.bitmapFont;

        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MainMenu(game));
                }
                return false;
            }
        });


        stage.addActor(table);
    }

    /**
     * LibGDX method that tries to run as many times as it can (frames)
     *
     * @param delta time passed since last frame
     */
    public void render(float delta) {

        ScreenUtils.clear(Color.BLACK);

        game.batch.begin();

        game.batch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        game.batch.end();

        stage.draw();

    }
}