/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.cic.platform.scene.Sprite;
import com.cic.platform.map.ObstacleMap;
import com.cic.platform.map.obstacle.Obstacle;

/**
 *
 * @author Cic
 */
public class GameCharacter extends MovableObject {

    public float direction = 0; // +1 0 -1

    public float walkSpeed = 10;
    public float runSpeed = 20;
    public float jumpSpeed = 5;

    // intentionally
    private boolean walking = false;
    private boolean running = false;
    private boolean jumping = false;

    // physically
    private boolean falling = false;

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

        Obstacle collidedWith = obstacleMap.move(this, tpf);

        if (falling) {
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

    public void stop(){
        walking = false;
        running = false;
        if (!falling) {
            doStop();
        }
    }

    private void doStop(){
        depiction.setNextSequence("stop:"+(direction == 0 ? "" : ( direction > 0 ? "R" : "L")));
        xSpeed = 0;
    }

    public void lookLeft(){
        direction = -1;
    }

    public void lookRight(){
        direction = +1;
    }

    public void walk(){
        walking = true;
        running = false;
        if (!falling) {
            startWalking();
        }
    }

    private void startWalking(){
        depiction.setNextSequence("walk:"+(direction > 0 ? "R" : "L"));
        xSpeed = walkSpeed * direction;
    }

    public void run(){
        walking = true;
        running = true;
        if (!falling) {
            startRunning();
        }
    }

    private void startRunning(){
        depiction.setNextSequence("run:"+(direction > 0 ? "R" : "L"));
        xSpeed = runSpeed * direction;
    }

    public void jump(boolean isJumping){
        jumping = isJumping;
        if (jumping && !falling) {
            doJump();
        }
    }

    private void doJump() {
        depiction.setNextSequence("jump:"+(direction > 0 ? "R" : "L"));
        ySpeed = jumpSpeed;
        falling = true;
    }

    private void freeFall(float tpf){
        ySpeed -= 20 * tpf;
    }

    private void doLand(){
        //ySpeed = 0;
        falling = false;
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
