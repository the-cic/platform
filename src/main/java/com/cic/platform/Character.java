/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform;

/**
 *
 * @author Cic
 */
public class Character {

    /*public static final int S_STANDING = 0; // doing nothing
    public static final int S_WALKING =  1;
    public static final int S_RUNNING =  2;
    public static final int S_JUMPING =  4; // going up*/

    public float xPos = 0;
    public float yPos = 0;
    public float direction = 0; // +1 0 -1
    //public float prevDirection = 0;
    public float xSpeed = 0;
    public float ySpeed = 0;

    public float walkSpeed = 1;
    public float runSpeed = 2;
    public float jumpSpeed = 3;

    //private int state = S_STANDING;
    // intentionally
    private boolean walking = false;
    private boolean running = false;
    private boolean jumping = false;

    // physically
    private boolean falling = false;

    public CharacterDepiction depiction;

    public void setPosition(float x, float y){
        xPos = x;
        yPos = y;
    }

    public void update(float tpf){
        xPos += xSpeed * tpf;
        yPos += ySpeed * tpf;

        /*if (yPos > 0) {
            ySpeed -= 10 * tpf;
        } else {
            if ((state & S_JUMPING) != 0) {
                if (depiction.nextSequenceKey == null) {
                    int stateWithoutJumping = state - S_JUMPING;
                    switch (stateWithoutJumping) {
                        case S_WALKING :
                            walk();
                            break;
                        case S_RUNNING :
                            run();
                            break;
                        default :
                            stop();
                    }
                }
                depiction.setNextSequence();
            }
            ySpeed = 0;
            yPos = 0;
        }*/

        if (falling) {
            if (yPos > 0) { // ground collision check
                doFall(tpf);
            } else { // landing
                doLand();
                yPos = 0; // ground level
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
        }
    }

    /*public int getState(){
        return state;
    }*/

    public void stop(){
        walking = false;
        running = false;
        //depiction.setNextSequence("stop:"+(direction == 0 ? "" : ( direction > 0 ? "R" : "L")));
        //doStop();
        if (!falling) {
            doStop();
        }
    }

    private void doStop(){
        depiction.setNextSequence("stop:"+(direction == 0 ? "" : ( direction > 0 ? "R" : "L")));
        xSpeed = 0;
    }

    /*public void walkLeft(){
        depiction.setNextSequence("walk:L");
    }

    public void walkRight(){
        depiction.setNextSequence("walk:R");
    }

    public void runLeft(){
        depiction.setNextSequence("run:L");
    }

    public void runRight(){
        depiction.setNextSequence("run:R");
    }*/

    public void lookLeft(){
        direction = -1;
    }

    public void lookRight(){
        direction = +1;
    }

    public void walk(){
        walking = true;
        running = false;
        //depiction.setNextSequence("walk:"+(direction > 0 ? "R" : "L"));
        //xSpeed = walkSpeed * direction;
        //doWalk();
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
        //depiction.setNextSequence("run:"+(direction > 0 ? "R" : "L"));
        //xSpeed = runSpeed * direction;
        //doRun();
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
        //falling = true;
        //depiction.setNextSequence("jump:"+(direction > 0 ? "R" : "L"));
        //doJump();
        if (jumping && !falling) {
            doJump();
        }
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

    /*
    private void doWalk() {
        //state = S_WALKING;
        xSpeed = walkSpeed * direction;
    }

    private void doRun() {
        //state = S_RUNNING;
        xSpeed = runSpeed * direction;
    }*/

    /*private void doStop() {
        //state = S_STANDING;
        xSpeed = 0;
    }*/
    private void doJump() {
        depiction.setNextSequence("jump:"+(direction > 0 ? "R" : "L"));

        /*if ((state & S_JUMPING) == 0) {
            state |= S_JUMPING;
            ySpeed = jumpSpeed;
        }*/
        ySpeed = jumpSpeed;
        falling = true;
    }

    private void doFall(float tpf){
        ySpeed -= 10 * tpf;
    }

    private void doLand(){
        ySpeed = 0;
        falling = false;
    }
}
