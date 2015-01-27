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
    public float jumpSpeed = 15;

    // intentionally
    private boolean walking = false;
    private boolean running = false;
    private boolean jumping = false;

    public CharacterDepiction depiction;

    public GameCharacter(float width, float height){
        this.boxWidth = width;
        this.boxHeight = height;
    }

    public void setDepiction(CharacterDepiction depiction){
        this.depiction = depiction;
        this.depiction.character = this;
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

    @Override
    public void onStartFalling(){
        depiction.setNextSequence("jump:"+(direction > 0 ? "R" : "L"));
    }

    @Override
    public void onStopFalling(float impactSpeed){
        if (walking) {
            if (running) {
                startRunning();
            } else {
                startWalking();
            }
        } else {
            doStop();
        }
        if (jumping) {
            doJump();
        }
        depiction.startNextSequence();
        // do something with speed if needed
    }

    public void setStop(){
        walking = false;
        running = false;
        if (!isFalling) {
            doStop();
        }
    }

    private void doStop(){
        depiction.setNextSequence("stop:"+(direction == 0 ? "" : ( direction > 0 ? "R" : "L")));
        xSpeed = 0;
    }

    public void setLookLeft(){
        direction = -1;
    }

    public void setLookRight(){
        direction = +1;
    }

    public void setWalk(){
        walking = true;
        running = false;
        if (!isFalling) {
            startWalking();
        }
    }

    private void startWalking(){
        depiction.setNextSequence("walk:"+(direction > 0 ? "R" : "L"));
        xSpeed = walkSpeed * direction;
    }

    public void setRun(){
        walking = true;
        running = true;
        if (!isFalling) {
            startRunning();
        }
    }

    private void startRunning(){
        depiction.setNextSequence("run:"+(direction > 0 ? "R" : "L"));
        xSpeed = runSpeed * direction;
    }

    public void setJump(boolean isJumping){
        jumping = isJumping;
        if (jumping && !isFalling) {
            doJump();
        }
    }

    private void doJump() {
        depiction.setNextSequence("jump:"+(direction > 0 ? "R" : "L"));
        ySpeed = jumpSpeed;
        isFalling = true;
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
        /*if (xSpeed == 0) {
            if (walking) {
                xSpeed = walkSpeed * direction;
            }
        }*/
    }

    /*public void onFrameSequenceChanged(String fullSeqName){
        //System.out.println(fullSeqName);
        String[] parts = fullSeqName.split(":");
        String seqName = parts[0];
        if (parts.length > 1) {
            if ("L".equals(parts[1])) {
                direction = -1;
            } else
            if ("R".equals(parts[1])) {
                direction = 1;
            } else {
                direction = 0;
            }
        }
        if ("walk".equals(seqName)) {
            doWalk();
        } else
        if ("run".equals(seqName)) {
            doRun();
        } else
        if ("stop".equals(seqName)) {
            doStop();
        }
        if ("jump".equals(seqName)) {
            doJump();
        }
    }*/
}
