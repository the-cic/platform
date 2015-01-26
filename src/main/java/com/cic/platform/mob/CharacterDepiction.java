/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.cic.platform.scene.Sprite;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
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
    public Node anchorBox = null;
    public float spriteXOfs;
    public float spriteYOfs;

    public CharacterDepiction(Sprite sprite, float spriteXOfs, float spriteYOfs){
        this.sprite = sprite;
        this.spriteXOfs = spriteXOfs;
        this.spriteYOfs = spriteYOfs;
    }

    public void addAnchorBox(AssetManager assetManager){
        this.anchorBox = new Node("Anchor");

        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        ColorRGBA col = ColorRGBA.Green.clone();
        col.a = 0.75f;
        boxMat.setColor("Color", col);
        boxMat.setColor("GlowColor", col);
        boxMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        Quad quad = new Quad(1, 0.2f);
        Geometry geom = new Geometry("Geom", quad);
        geom.setLocalTranslation(-0.5f, -0.1f, 0);
        geom.setMaterial(boxMat);
        this.anchorBox.attachChild(geom);

        quad = new Quad(0.2f, 1);
        geom = new Geometry("Geom", quad);
        geom.setLocalTranslation(-0.1f, -0.5f, 0);
        geom.setMaterial(boxMat);
        this.anchorBox.attachChild(geom);

        boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        col = ColorRGBA.Blue.clone();
        col.a = 0.75f;
        boxMat.setColor("Color", col);
        boxMat.setColor("GlowColor", col);
        boxMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);

        quad = new Quad(character.boxWidth, character.boxHeight);
        geom = new Geometry("Geom", quad);
        geom.setLocalTranslation(-character.boxWidth/2, 0, 0);
        geom.setMaterial(boxMat);
        this.anchorBox.attachChild(geom);
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
                startNextSequence();
            }
        //}
    }

    public void startNextSequence(){
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
            startNextSequence();
        }
        sprite.setFrame(frameSequence.frameIndex);
        sprite.setLocalTranslation(character.xPos + spriteXOfs, character.yPos + spriteYOfs, 0);

        if (anchorBox != null) {
            anchorBox.setLocalTranslation(character.xPos, character.yPos, 0.1f);
        }
    }

    public FrameSequence getFrameSequence(){
        return frameSequence;
    }

    public String getFrameSequenceKey(){
        return currentSequenceKey;
    }
}
