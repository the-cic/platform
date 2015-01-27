/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Cic
 */
public class CommonMaterials {

    public static Material solidBlack;
    public static Material solidWhite;
    public static Material solidRed;
    public static Material solidGreen;
    public static Material solidBlue;

    private static AssetManager assetManager;

    public static class SolidMaterial extends Material {
        public SolidMaterial(ColorRGBA color){
            super(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            setColor("Color", color.clone());
        }
    }

    public static class SolidTransparentMaterial extends Material {
        public SolidTransparentMaterial(ColorRGBA color, float alpha){
            super(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            ColorRGBA col = color.clone();
            col.a = alpha;
            setColor("Color", col);
            //setColor("GlowColor", col);
            getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
    }

    public static void create(AssetManager am){
        assetManager = am;

        solidBlack = new SolidMaterial(ColorRGBA.Black);
        solidWhite = new SolidMaterial(ColorRGBA.White);
        solidRed = new SolidMaterial(ColorRGBA.Red);
        solidGreen = new SolidMaterial(ColorRGBA.Green);
        solidBlue = new SolidMaterial(ColorRGBA.Blue);
    }
}
