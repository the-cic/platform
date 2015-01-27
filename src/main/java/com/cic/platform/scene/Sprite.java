/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.scene;

import com.cic.platform.CommonMaterials;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author cic
 */
public class Sprite extends Node {

    public final float width, height;

    protected int numberOfFrames;

    private Material material;

    public Sprite(AssetManager assetManager, String textureFile, int numberOfFrames, float width, float height){
        super("Sprite");
        this.width = width;
        this.height = height;
        this.numberOfFrames = numberOfFrames;

        material = new SpriteMaterial(assetManager, textureFile, numberOfFrames);

        Quad shape = new Quad(width, height);
        Geometry geom = new Geometry("SpriteBox", shape);
        geom.setMaterial(material);
        this.attachChild(geom);

        /* frame */
        Material boxMat = new CommonMaterials.SolidMaterial(ColorRGBA.Red);

        shape = new Quad(0.2f, 1f);
        geom = new Geometry("ZeroBox", shape);
        geom.setMaterial(boxMat);
        this.attachChild(geom);

        shape = new Quad(1f, 0.2f);
        geom = new Geometry("ZeroBox", shape);
        geom.setMaterial(boxMat);
        this.attachChild(geom);

        boxMat = new CommonMaterials.SolidTransparentMaterial(ColorRGBA.Cyan, 0.15f);

        shape = new Quad(width, height);
        geom = new Geometry("ZeroBox", shape);
        this.attachChild(geom);

        geom.setMaterial(boxMat);
        /* */
    }

    public void setFrame(int index){
        material.setInt("Index", index);
    }

    /**
     * Default implementation does nothing
     * @param tpf
     */
    public void update(float tpf){}
}
