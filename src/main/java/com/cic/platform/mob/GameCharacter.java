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
    public float jumpSpeed = 10;

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

        // ObstacleMap.move will set/unset isFalling and call onStartFalling or onStopFalling
        obstacleMap.move(this, tpf);

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

    /*
    public void update(ObstacleMap obstacleMap, float tpf){

        Obstacle collidedWith = obstacleMap.move(this, tpf);

        if (isFalling) {
            if (collidedWith == null) {
                freeFall(tpf);
            } else {
                doLand();
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
                depiction.setNextSequence();
            }
        } else {
            if (collidedWith != null) {
            }
        }

        depiction.update(tpf);
    }
    */

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
        ySpeed -= 50 * tpf;
    }

    /*
    private void doLand(){
        //ySpeed = 0;
        isFalling = false;
    }
    */

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
