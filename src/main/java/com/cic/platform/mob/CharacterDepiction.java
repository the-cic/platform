/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.cic.platform.util.CommonMaterials;
import com.cic.platform.scene.Sprite;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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

    public GameCharacter character = null;
    public Sprite sprite;
    public Node anchorBox = null;
    private Node boundBox = null;
    public float spriteXOfs;
    public float spriteYOfs;

    public CharacterDepiction(Sprite sprite, float spriteXOfs, float spriteYOfs){
        this.sprite = sprite;
        this.spriteXOfs = spriteXOfs;
        this.spriteYOfs = spriteYOfs;
    }

    public void addAnchorBox(AssetManager assetManager){
        this.anchorBox = new Node("Anchor");

        Material boxMat = new CommonMaterials.SolidTransparentMaterial(ColorRGBA.Green, 0.75f);

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

        boxMat = new CommonMaterials.SolidTransparentMaterial(ColorRGBA.Blue, 0.75f);

        quad = new Quad(1, 1);
        geom = new Geometry("Geom", quad);
        geom.setMaterial(boxMat);
        this.boundBox = new Node("Bound box");
        this.boundBox.attachChild(geom);
        updateBoundBox();

        this.anchorBox.attachChild(boundBox);
    }

    public void updateBoundBox(){
        if (boundBox != null) {
            boundBox.setLocalScale(character.boxWidth, character.boxHeight, 1);
            boundBox.setLocalTranslation(-character.boxWidth/2, 0, 0);
        }
    }

    public void addFrameSequence(String key, FrameSequence seq){
        sequences.put(key, seq);
    }

    public void startSequence(String key){
        currentSequenceKey = key;
        frameSequence = sequences.get(currentSequenceKey);
        frameSequence.start();
    }

    public void update(float tpf){
        boolean advancedFrame = frameSequence.advance(tpf);
        if (frameSequence.hasLooped()) {
            String newSequence = character.getSequenceFromState();
            if (!currentSequenceKey.equals(newSequence)) {
                startSequence(newSequence);
            }
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
