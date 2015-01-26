/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.scene;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author cic
 */
public class SceneView {
    
    // dimensions (in units of 1) of scene to be fit in view
    float sceneWidth = 40;
    float sceneHeight;
    public Node node;

    public SceneView(Camera cam, Scene scene, AssetManager assetManager){
        Node frame = new Node("frame");
        float frustW = cam.getFrustumRight() - cam.getFrustumLeft();
        float frustH = cam.getFrustumTop() - cam.getFrustumBottom();
        System.out.println(frustW + " x " + frustH);
        Quad fq = new Quad(1, 1);
        Geometry fg = new Geometry("fgeom", fq);
        Material frMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        frMat.setColor("Color", ColorRGBA.Green.clone());
        fg.setMaterial(frMat);
        fg.setLocalTranslation(-0.5f, -0.5f, -10);
        frame.attachChild(fg);
        frame.scale(0.999f * Math.min(frustW, frustH));
        
        node = new Node("SceneView");
        node.attachChild(frame);
        
        float scale = Math.min(frustW, frustH) / scene.width;
        scale *= scene.width / sceneWidth;
        System.out.println(scale);
        
        scene.getNode().setLocalScale(scale);
        scene.getNode().setLocalTranslation(-scale * scene.width / 2, -scale * scene.height / 2, 0);
    }
    
}
