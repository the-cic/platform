/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import java.util.HashMap;

/**
 * Connection between character and sprite
 * 
 * @author Cic
 */
public class CharacterDepiction {

    public HashMap<String, FrameSequence> sequences = new HashMap<String, FrameSequence>();
    public FrameSequence frameSequence = null;
    public String currentSequenceKey = null;
    public String nextSequenceKey = null;
    
    public GameCharacter character = null;
    public Sprite sprite;

    public CharacterDepiction(Sprite sprite){
        this.sprite = sprite;
    }

    public void addFrameSequence(String key, FrameSequence seq){
        sequences.put(key, seq);
    }

    public void startSequence(String key){
        currentSequenceKey = key;
        nextSequenceKey = null;
        frameSequence = sequences.get(currentSequenceKey);
        frameSequence.start();
    }

    public void setNextSequence(String key){
        //if (!key.equals(currentSequenceKey)) {
            nextSequenceKey = key;
            if (currentSequenceKey == null) {
                setNextSequence();
            }
        //}
    }

    public void setNextSequence(){
        if (nextSequenceKey == null) {
            return;
        }
        if (sequences.containsKey(nextSequenceKey)) {
            currentSequenceKey = nextSequenceKey;
            frameSequence = sequences.get(currentSequenceKey);
            frameSequence.start();
            nextSequenceKey = null;
            // notify character
            //character.onFrameSequenceChanged(currentSequenceKey);
        }
    }

    public void update(float tpf){
        boolean advancedFrame = frameSequence.advance(tpf);
        if (nextSequenceKey != null && frameSequence.canBeInterrupted) {
            setNextSequence();
        }
        sprite.setFrame(frameSequence.frameIndex);
    }

    public FrameSequence getFrameSequence(){
        return frameSequence;
    }

    public String getFrameSequenceKey(){
        return currentSequenceKey;
    }
}
