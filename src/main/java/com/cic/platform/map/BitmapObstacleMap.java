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
    private byte[][] blocks;
    private float blockWidth = 1;
    private float blockHeight = 1;

    public BitmapObstacleMap(Image textureImage) {
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

    public float getWidth() {
        return bitmapWidth * blockWidth;
    }

    public float getHeight() {
        return bitmapHeight * blockHeight;
    }

    @Override
    public Obstacle collisionCheck(float x1, float y1, float x2, float y2, MovableObject mob) {
        int u1 = (int) (x1 / blockWidth);
        int v1 = (int) (y1 / blockWidth);
        int u2 = (int) (x2 / blockWidth);
        int v2 = (int) (y2 / blockWidth);
        //if (y2 < 0) {
        if (blocks[v2][u2] != 0) {
            mob.yPos = (v2 + 1) * blockHeight;
            return Ground.anyGround;
        }
        return null;
    }

    private boolean isFreePassage(int i, int j) {
        return blocks[j][i] == 0;
    }

    @Override
    public Obstacle move(MovableObject mob, float tpf) {
        float nextXPos = mob.xPos + mob.xSpeed * tpf;
        float nextYPos = mob.isFalling ? mob.yPos + mob.ySpeed * tpf : mob.yPos;

        if (mob.isFalling) {
            
        } else {
            // Block Y index that mob is standing on (block under mob)
            int nextMapJ = Math.round(nextYPos / this.blockHeight) - 1;

            if (mob.xSpeed != 0) {
                // If mob is moving, check that the path is clear for mob box
                float nextBoxFarXPos = nextXPos + (mob.xSpeed > 0 ? mob.boxWidth / 2 : -mob.boxWidth / 2);
                int nextBoxFarMapI = (int) Math.floor(nextBoxFarXPos / this.blockWidth);
                int boxHeightInBlocks = (int) Math.floor(mob.boxHeight / this.blockHeight) + 1;
                boolean freePassage = true;
                for (int j = nextMapJ + 1; j <= nextMapJ + boxHeightInBlocks; j++) {
                    // All the blocks from bottom to top of box must be passable
                    freePassage &= isFreePassage(nextBoxFarMapI, j);
                }
                if (!freePassage) {
                    // Adjust position to clear mob box
                    nextXPos = mob.xSpeed > 0
                            ? nextBoxFarMapI * this.blockWidth - mob.boxWidth / 2
                            : (nextBoxFarMapI + 1) * this.blockWidth + mob.boxWidth / 2;
                }
            }

            int nextMapI = (int) Math.floor(nextXPos / this.blockWidth);

            // Block mob is standing on
            if (isFreePassage(nextMapI, nextMapJ)) {
                mob.isFalling = true;
                mob.onStartFalling();
            }
            
            // Later consider stairs / slopes

            //Obstacle standingOn;
            //Obstalce standingIn;
        }

        mob.xPos = nextXPos;
        mob.yPos = nextYPos;

        return null;
    }

}
