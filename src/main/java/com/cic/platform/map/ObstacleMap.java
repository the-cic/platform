/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.map;

import com.cic.platform.mob.MovableObject;
import com.cic.platform.map.obstacle.Ground;
import com.cic.platform.map.obstacle.Obstacle;

/**
 *
 * Reinventint the wheel again
 *
 * @author Cic
 */
public abstract class ObstacleMap {

    public Obstacle move(MovableObject mob, float tpf){
        float nextXPos = mob.xPos + mob.xSpeed * tpf;
        float nextYPos = mob.yPos + mob.ySpeed * tpf;

        Obstacle collision = collisionCheck(mob.xPos, mob.yPos, nextXPos, nextYPos, mob);

        if (collision != null) {
            switch (collision.type) {
                case Obstacle.OT_GROUND :
                        //nextYPos = 0;
                        mob.ySpeed = 0;
                        mob.xSpeed = 0;
                    break;
                default :
                    //nextYPos = 0;
                    mob.ySpeed = 0;
                    mob.xSpeed = 0;
            }
        } else {
            mob.xPos = nextXPos;
            mob.yPos = nextYPos;
        }

        return collision;
    }
    
    
    
    

    public abstract float getWidth();
    public abstract float getHeight();

    public Obstacle collisionCheck(float x1, float y1, float x2, float y2, MovableObject mob){
        if (y2 < 0) {
            mob.yPos = 0;
            return Ground.anyGround;
        }
        return null;
    }
}
