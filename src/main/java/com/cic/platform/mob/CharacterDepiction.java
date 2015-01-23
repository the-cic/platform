/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;
import java.util.HashMap;

/**
 *
 * @author Cic
 */
public class CharacterDepiction {

    public HashMap<String, FrameSequence> sequences = new HashMap<String, FrameSequence>();
    public FrameSequence frameSequence = null;
    public String currentSequenceKey = null;
    public String nextSequenceKey = null;
    public Character character = null;

    public Material material;

    public CharacterDepiction(AssetManager assetManager, String textureFile){
        Texture spritesTexture = assetManager.loadTexture(textureFile);
        material = new Material(assetManager, "Materials/ColoredTexturedSprite.j3md");
        //mat.setColor("Color", new ColorRGBA(255f / 255f, 0f / 255f, 0f / 255f, 1));
        material.setTexture("ColorMap", spritesTexture);
        material.setInt("Index", 1);
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
        material.setInt("Index", frameSequence.frameIndex);
    }

    public FrameSequence getFrameSequence(){
        return frameSequence;
    }

    public String getFrameSequenceKey(){
        return currentSequenceKey;
    }
}
