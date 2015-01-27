/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.scene;

import com.cic.platform.CommonMaterials;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 *
 * @author cic
 */
public class SceneView {

    // Dimensions (in units of 1) of scene to be fit in view
    private float sceneViewWidth = 44;
    // Scrol percent of scene visible in view 0..1
    private float sceneViewScrollPercent = 0.0f;

    private float frameWidth; // set to 1
    private float frameHeight; // set by aspect ratio
    private float sceneScale = 1; // scaling of scene
    private float frameFitScale = 1; // scaling to fit frame to frustrum

    private Node node; // the main scene view node
    private Node frame; // frame node contained in main node
    private Camera cam;
    private Scene scene;

    public SceneView(Camera cam, Scene scene, float aspect){
        this.cam = cam;
        this.scene = scene;
        frameWidth = 1;
        frameHeight = 1 / aspect;

        makeFrame();

        applyFrame();
    }

    public Node getNode(){
        return node;
    }

    public void setScene(Scene scene){
        this.scene = scene;
        applyFrame();
    }

    public void setSceneViewWidth(float w){
        this.sceneViewWidth = w;
        applyScale();
    }

    public void setSceneViewPercent(float p){
        this.sceneViewScrollPercent = p;
        applyScroll();
    }

    private void makeFrame(){
        frame = new Node("frame");
        Quad frameShape = new Quad(frameWidth, frameHeight);
        Geometry geom = new Geometry("frameGeom", frameShape);
        geom.setMaterial(CommonMaterials.solidGreen);
        geom.setLocalTranslation(-frameWidth / 2, -frameHeight / 2, -10);
        frame.attachChild(geom);

        node = new Node("SceneView");
        node.attachChild(frame);
    }

    private void applyFrame(){
        float frustrumWidth = cam.getFrustumRight() - cam.getFrustumLeft();
        float frustrumHeight = cam.getFrustumTop() - cam.getFrustumBottom();

        // scale to fit frame into frustrum
        frameFitScale = Math.min(frustrumWidth / frameWidth, frustrumHeight / frameHeight);
        frame.setLocalScale(0.99f * frameFitScale);

        applyScale();
    }

    private void applyScale(){
        // scale to show sceneWidth of scene in frame
        sceneScale = frameFitScale / sceneViewWidth;

        scene.getNode().setLocalScale(sceneScale);

        applyScroll();
    }

    private void applyScroll(){
        scene.getNode().setLocalTranslation(
                sceneScale * ((sceneViewWidth - scene.width) * sceneViewScrollPercent - sceneViewWidth / 2),
                -sceneScale * scene.height / 2,
                0);
    }
}
