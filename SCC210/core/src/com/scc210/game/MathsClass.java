package com.scc210.game;

import com.scc210.game.entities.Entity;

/**
 * Class used for repeatable calculation methods
 */
public class MathsClass {

    /**
     * Method to check if 2 entities are colliding
     * @param e1 entity
     * @param e2 another entity
     * @return boolean on the collision state
     */
    public static boolean isCollide(Entity e1, Entity e2){

        float e1x = e1.getPosX();
        float e1y = e1.getPosY();
        float e1w = e1.getWidth();
        float e1h = e1.getHeight();

        float e2x = e2.getPosX();
        float e2y = e2.getPosY();
        float e2w = e2.getWidth();
        float e2h = e2.getHeight();

        //player fixes thingy
        if (e1.getObjectId() == 0) {
            e1x += e1w / 2;
            e1y += e1h / 2;
        }

        if(e1x < e2x + e2w &&
                e1x + e1w > e2x &&
                e1y < e2y + e2h &&
                e1h + e1y > e2y){
            return true;
        }
        return false;
    }
}
