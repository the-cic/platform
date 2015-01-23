/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.obstacle;

/**
 *
 * @author Cic
 */
public class Ground extends Obstacle {

    public static final Ground anyGround = new Ground();

    public Ground(){
        type = OT_GROUND;
    }
}
