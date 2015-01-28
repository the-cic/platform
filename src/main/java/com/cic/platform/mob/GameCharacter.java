/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.cic.platform.map.ObstacleMap;
import com.cic.platform.scene.Sprite;

/**
 *
 * @author Cic
 */
public class GameCharacter extends MovableObject {

    public float direction = 0; // +1 0 -1

    public float walkSpeed = 5;
    public float runSpeed = 10;
    public float sneakSpeed = 1;
    public float jumpSpeed = 15;

    private float normalBoxHeight;
    private float crouchedBoxHeight;

    // intentionally
    private boolean walking = false;
    private boolean running = false;
    private boolean jumping = false;
    private boolean sneaking = false;
    private boolean crouching = false;
    private boolean mustJump = false;

    public CharacterDepiction depiction;

    public GameCharacter(float width, float height){
        this.boxWidth = width;
        this.boxHeight = height;
        normalBoxHeight = this.boxHeight;
        crouchedBoxHeight = this.boxHeight / 2;
    }

    public void setDepiction(CharacterDepiction depiction){
        this.depiction = depiction;
        this.depiction.character = this;
        this.depiction.startSequence(this.getSequenceFromState());
    }

    public Sprite getSprite(){
        return depiction.sprite;
    }

    public void setPosition(float x, float y){
        xPos = x;
        yPos = y;
    }

    public void update(ObstacleMap obstacleMap, float tpf){
        nextXPos = xPos + xSpeed * tpf;
        nextYPos = isFalling ? yPos + ySpeed * tpf : yPos;

        // ObstacleMap.move will set/unset isFalling and call onStartFalling or onStopFalling
        obstacleMap.checkMove(this);

        xPos = nextXPos;
        yPos = nextYPos;

        if (isFalling) {
            freeFall(tpf);
        } else {
        }

        depiction.update(tpf);
    }

    public void setLookLeft(){
        direction = -1;
    }

    public void setLookRight(){
        direction = +1;
    }

    public void setStop(){
        walking = false;
        running = false;
        sneaking = false;
    }

    public void setWalk(){
        walking = true;
        running = false;
        sneaking = false;
    }

    public void setRun(){
        walking = true;
        running = true;
        sneaking = false;
    }

    public void setSneak(){
        walking = true;
        running = false;
        sneaking = true;
    }

    public void setJump(boolean isJumping){
        jumping = isJumping;
        if (jumping) {
            crouching = false;
            mustJump = true;
        }
    }

    public void setCrouch(boolean isCrouching){
        crouching = isCrouching;
    }
    
    public String getSequenceFromState(){
        if (isFalling) {
            // no control while falling
            return "jump:"+(direction > 0 ? "R" : "L");
        }
        
        xSpeed = 0;
        String seq = crouching 
                ? "crouch:"+(direction > 0 ? "R" : "L")
                : "stop:"+(direction == 0 ? "" : ( direction > 0 ? "R" : "L"));
        
        if (crouching && !jumping) {
            boxHeight = crouchedBoxHeight;
            depiction.updateBoundBox();
        } else {
            boxHeight = normalBoxHeight;
            depiction.updateBoundBox();
        }
        
        if (walking) {
            if (running) {
                if (crouching) {
                    seq = "crouch:"+(direction > 0 ? "R" : "L");
                    xSpeed = runSpeed * direction * 0.75f;
                } else {
                    seq = "run:"+(direction > 0 ? "R" : "L");
                    xSpeed = runSpeed * direction;
                }
            } else
            if (sneaking) {
                if (crouching) {
                    xSpeed = sneakSpeed * direction * 0.75f;
                    seq = "crouch:"+(direction > 0 ? "R" : "L");
                } else {
                    xSpeed = sneakSpeed * direction;
                    seq = "sneak:"+(direction > 0 ? "R" : "L");
                }
            } else {
                if (crouching) {
                    xSpeed = walkSpeed * direction * 0.75f;
                    seq = "crouch:"+(direction > 0 ? "R" : "L");
                } else {
                    xSpeed = walkSpeed * direction;
                    seq = "walk:"+(direction > 0 ? "R" : "L");
                }
            }
        } else {
        }
        if (mustJump) {
            ySpeed = jumpSpeed;
            isFalling = true;
            mustJump = false;
            seq = "jump:"+(direction > 0 ? "R" : "L");
        }
        return seq;
    }

    private void freeFall(float tpf){
        // apply gravity or any modification during falling
        boolean wentUp = ySpeed > 0;
        ySpeed -= 50 * tpf;
        if (wentUp && ySpeed < 0) {
            onFallApex();
        }
    }

    public void onFallApex(){
        if (xSpeed == 0) {
            if (walking) {
                xSpeed = walkSpeed * direction * 0.1f;
            }
        }
    }

    @Override
    public void onStartFalling(){
        depiction.startSequence("jump:"+(direction > 0 ? "R" : "L"));
    }

    @Override
    public void onStopFalling(float impactSpeed){
        depiction.startSequence(this.getSequenceFromState());
        // do something with speed if needed
    }
}
