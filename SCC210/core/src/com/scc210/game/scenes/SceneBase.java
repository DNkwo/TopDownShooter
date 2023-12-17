package com.scc210.game.scenes;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.scc210.game.Game;
import com.scc210.game.entities.Enemy;
import com.scc210.game.entities.Player;
import com.scc210.game.entities.UpdatableEntity;
import com.scc210.game.entities.Weapon;

import java.util.ArrayList;
import java.util.List;



/**
 * Base class for all the Scenes that contains variables common to every level
 */
public abstract class SceneBase extends ScreenAdapter {


    protected Game game;
    private boolean isPaused = false;

    //player
    protected Player player = null;
    protected int score;

    //enemies
    protected List<UpdatableEntity> enemyList = new ArrayList<>();

    //entities
    protected List<UpdatableEntity> updatableEntities = new ArrayList<>();
    protected List<UpdatableEntity> addToUpdatableEntities = new ArrayList<>();
    protected List<UpdatableEntity> removeFromUpdatableEntities = new ArrayList<>();

    //camera
    protected OrthographicCamera camera;

    //walls
    protected int[][] walls;

    //weapon list
    protected List<Weapon> weaponList = new ArrayList<>();

    /**
     *
     * @param game game variable that contains the render variables
     */
    public SceneBase(Game game){
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public int[][] getWalls() {
        return walls;
    }

    public List<Weapon> getWeaponList() {
        return weaponList;
    }

    public List<UpdatableEntity> getUpdatableEntities() {
        return updatableEntities;
    }

    public List<UpdatableEntity> getAddToUpdatableEntities() {
        return addToUpdatableEntities;
    }


    public void setAddToUpdatableEntities(List<UpdatableEntity> addToUpdatableEntities) {
        this.addToUpdatableEntities = addToUpdatableEntities;
    }

    public List<UpdatableEntity> getRemoveFromUpdatableEntities() {
        return removeFromUpdatableEntities;
    }

    public void setRemoveFromUpdatableEntities(List<UpdatableEntity> removeFromUpdatableEntities) {
        this.removeFromUpdatableEntities = removeFromUpdatableEntities;
    }

    public int getScore() {
        return score;
    }

    public void addToEnemyList(UpdatableEntity enemy){
        if(enemy != null) {
            enemyList.add(enemy);
        }
    }

    public void removeFromEnemyList(UpdatableEntity enemy){
        if(enemy != null) {
            enemyList.remove(enemy);
        }
    }

    public void addScore(int value){
        score += value;
    }

    public void pause(){
        if(isPaused) {
            this.isPaused = false;
        }
        else{
            this.isPaused = true;
            loadPause();
        }
    }

    public abstract void loadPause();

    public boolean isPaused() {
        return isPaused;
    }


}
