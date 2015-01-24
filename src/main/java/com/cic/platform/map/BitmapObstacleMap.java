/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.map;

import com.cic.platform.mob.MovableObject;
import com.cic.platform.map.obstacle.Ground;
import com.cic.platform.map.obstacle.Obstacle;
import com.jme3.texture.Image;
import java.nio.ByteBuffer;

/**
 *
 * @author Cic
 */
public class BitmapObstacleMap extends ObstacleMap {

    private int bitmapWidth, bitmapHeight;
    private byte [][] blocks;
    private float blockWidth = 1;
    private float blockHeight = 1;

    public BitmapObstacleMap(Image textureImage){
        Image.Format format = textureImage.getFormat();
        if (format != Image.Format.BGR8) {
            throw new RuntimeException("Image format");
        }
        bitmapWidth = textureImage.getWidth();
        bitmapHeight = textureImage.getHeight();
        blocks = new byte[bitmapHeight][];
        int b, g, r;
        ByteBuffer data = textureImage.getData(0);
        data.rewind();
        // y starts at the bottom
        for (int y = 0; y < blocks.length; y++) {
            blocks[y] = new byte[bitmapWidth];
            for (int x = 0; x < blocks[y].length; x++) {
                b = data.get() & 0xFF;
                g = data.get() & 0xFF;
                r = data.get() & 0xFF;

                if (b > 0 || g > 0 || r > 0) {
                    blocks[y][x] = Obstacle.OT_GROUND;
                } else {
                    blocks[y][x] = 0;
                }
            }
        }
    }

    public float getWidth(){
        return bitmapWidth * blockWidth;
    }

    public float getHeight(){
        return bitmapHeight * blockHeight;
    }

    @Override
    public Obstacle collisionCheck(float x1, float y1, float x2, float y2, MovableObject mob){
        int u1 = (int)(x1 / blockWidth);
        int v1 = (int)(y1 / blockWidth);
        int u2 = (int)(x2 / blockWidth);
        int v2 = (int)(y2 / blockWidth);
        //if (y2 < 0) {
        if (blocks[v2][u2] != 0) {
            mob.yPos = (v2+1) * blockHeight;
            return Ground.anyGround;
        }
        return null;
    }

}
