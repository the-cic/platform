/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

/**
 *
 * @author Cic
 */
public class MovableObject {

    public float xPos = 0;
    public float yPos = 0;
    public float xSpeed = 0;
    public float ySpeed = 0;
    public float boxWidth = 1f;
    public float boxHeight = 1f;

    public float nextXPos = 0;
    public float nextYPos = 0;

    // object can be on a surface, or be falling
    // (simplify logic when on a surface)
    public boolean isFalling = false;

    public void onStartFalling(){}

    public void onStopFalling(float impactSpeed){}

}
