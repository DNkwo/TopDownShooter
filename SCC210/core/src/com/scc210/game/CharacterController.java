package com.scc210.game;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.scc210.game.scenes.SceneBase;

/**
 * Method used to record key states related to gameplay actions
 */
public class CharacterController extends InputAdapter implements InputProcessor {

    private SceneBase scene;

    //Movement directions
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;

    //other controls
    public boolean sprinting;
    public boolean shooting;
    public boolean interacting;
    public boolean castingUltimate;

    //mouse positions
    public int mouseX;
    public int mouseY;

    //inventory control
    public int inventorySelect = 0;


    /**
     * Create a CharacterController
     */
    public CharacterController(SceneBase scene) {
        this.scene = scene;
    }

    /**
     * Method called when a key is pressed and then uses a switch case to identify the action being done
     * @param keyCode code of the pressed key
     * @return
     */
    public boolean keyDown(int keyCode) {

        switch (keyCode) {
            case Keys.DOWN:
            case Keys.S:
                down = true;
                break;
            case Keys.UP:
            case Keys.W:
                up = true;
                break;
            case Keys.LEFT:
            case Keys.A:
                left = true;
                break;
            case Keys.RIGHT:
            case Keys.D:
                right = true;
                break;
            case Keys.SHIFT_LEFT:
                sprinting = true;
                break;
            case Keys.SPACE:
                shooting = true;
                break;
            case Keys.E:
                interacting = true;
                break;
            case Keys.NUM_1:
                inventorySelect = 0;
                break;
            case Keys.NUM_2:
                inventorySelect = 1;
                break;
            case Keys.NUM_3:
                inventorySelect = 2;
                break;
            case Keys.NUM_4:
                inventorySelect = 3;
                break;
            case Keys.ESCAPE:
                this.scene.pause();
                break;
            case Keys.R:
                this.castingUltimate = true;
                break;



        }

        return false;
    }

    /**
     * Method called when a key is released and then uses a switch case to identify the action being done
     * @param keyCode code of the released key
     * @return
     */
    public boolean keyUp(int keyCode) {

        switch (keyCode) {
            case Keys.DOWN:
            case Keys.S:
                down = false;
                break;
            case Keys.UP:
            case Keys.W:
                up = false;
                break;
            case Keys.LEFT:
            case Keys.A:
                left = false;
                break;
            case Keys.RIGHT:
            case Keys.D:
                right = false;
                break;
            case Keys.SHIFT_LEFT:
                sprinting = false;
                break;
            case Keys.SPACE:
                shooting = false;
                break;
            case Keys.E:
                interacting = false;
                break;
            case Keys.R:
                this.castingUltimate = false;
                break;

        }

        return false;
    }

    /**
     * method to identify the coordinates of the mouse on the screen
     * @param screenX x coordinate
     * @param screenY y coordinate
     * @return
     */
    public boolean mouseMoved(int screenX, int screenY) {

        mouseX = screenX;
        mouseY = screenY;

        return false;
    }

}
