/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.texture.Texture;

/**
 *
 * @author cic
 */
public class SpriteMaterial extends Material {
    
    public SpriteMaterial(AssetManager assetManager, String textureFile){
        super(assetManager, "Materials/ColoredTexturedSprite.j3md");
        
        init(assetManager, textureFile, 1);
    }

    public SpriteMaterial(AssetManager assetManager, String textureFile, int numberOfFrames){
        super(assetManager, "Materials/ColoredTexturedSprite.j3md");
        
        init(assetManager, textureFile, numberOfFrames);
    }
    
    private void init(AssetManager assetManager, String textureFile, int numberOfFrames) {
        Texture texture = assetManager.loadTexture(textureFile);
        setTexture("ColorMap", texture);
        setInt("Index", 0);
        setInt("Frames", numberOfFrames);
    }
}
