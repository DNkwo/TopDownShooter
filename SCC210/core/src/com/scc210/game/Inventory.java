package com.scc210.game;

import com.badlogic.gdx.Gdx;
import com.scc210.game.entities.ControllableEntity;
import com.scc210.game.entities.Player;
import com.scc210.game.entities.UpdatableEntity;
import com.scc210.game.entities.Weapon;
import com.scc210.game.scenes.SceneBase;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<UpdatableEntity> updatableEntities;

    public Inventory(List<UpdatableEntity> updatableEntities){
        this.updatableEntities = updatableEntities;
    }


    private int itemSelected;

    private Player player;


    private Weapon[] weapons = new Weapon[4];

    private int healthPotions = 0;

    private int energyAmmo = 0;




    public void addWeapon(Weapon weapon){
        if(weapons[itemSelected] == null){
            weapons[itemSelected] = weapon;
        }
    }

    public void removeWeapon(){
        weapons[itemSelected] = null;
    }



    public int getHealthPotions() {
        return healthPotions;
    }

    public void setHealthPotions(int healthPotions) {
        this.healthPotions = healthPotions;
    }

    public int getEnergyAmmo() {
        return energyAmmo;
    }

    public void setEnergyAmmo(int energyAmmo) {
        this.energyAmmo = energyAmmo;
    }

    public Weapon getWeapon(int weaponPlacement) {
        return weapons[weaponPlacement];
    }

    public void setItemSelected(int newItemSelect){
        if(newItemSelect != this.itemSelected){ // new item selected


            //check if current select has an item, if so, remove from updateable list
            if(weapons[this.itemSelected] != null){
                weapons[this.itemSelected].setOwner(null);
                updatableEntities.remove(weapons[this.itemSelected]);
            }

            //check if new item has item, if so, add to updateable list
            if(weapons[newItemSelect] != null){
                weapons[newItemSelect].setOwner(player);
                weapons[newItemSelect].getSprite().setPosition(player.getPosX(), player.getPosY());
                player.setWeapon(weapons[newItemSelect]);
                updatableEntities.add(weapons[newItemSelect]);
            }


            this.itemSelected = newItemSelect;
        }
    }

    public boolean isEmpty(){
        for(int i = 0; i < weapons.length; i++){
            if(weapons[i] != null){
                return false;
            }
        }
        return true;
    }

    public int getItemSelected() {
        return itemSelected;
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setUpdatableEntities(List<UpdatableEntity> updatableEntities) {
        this.updatableEntities = updatableEntities;
    }

    public void reload() {


        for(int i = 0; i < weapons.length; i++){
            if(weapons[i] != null){
                player.setWeapon(weapons[i]);
                weapons[i].setOwner(player);
                updatableEntities.add(weapons[i]);
                weapons[i].setUpdatableEntities(updatableEntities);
            }
        }
    }
}
