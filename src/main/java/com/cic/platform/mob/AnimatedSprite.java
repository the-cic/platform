/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.jme3.asset.AssetManager;

/**
 *
 * @author cic
 */
public class AnimatedSprite extends Sprite {
    
    private float frameDuration;
    private float time;
    private int frame;
    private int startFrame;
    private int endFrame;
    private boolean isRunning;
    private boolean mustLoop;

    public AnimatedSprite(AssetManager assetManager, String textureFile, int numberOfFrames, float width, float height) {
        super(assetManager, textureFile, numberOfFrames, width, height);
        isRunning = false;
        mustLoop = false;
    }
    
    public void start(float fps){
        startFrame = 0;
        endFrame = numberOfFrames - 1;
        frameDuration = 1f / fps;
        frame = startFrame;
        time = 0;
        isRunning = true;
        mustLoop = false;
    }
    
    public void loop(float fps){
        start(fps);
        mustLoop = true;
    }
    
    @Override
    public void update(float tpf){
        if (isRunning) {
            time += tpf;
            frame = Math.round(time / frameDuration);
            if (frame > endFrame) {
                if (mustLoop) {
                    time -= frame * frameDuration;
                    frame = Math.round(time / frameDuration);
                } else {
                    frame = endFrame;
                    isRunning = false;
                }
            }
        }
        setFrame(frame);
    }
}
