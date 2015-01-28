/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.mob;

/**
 *
 * @author Cic
 */
public class FrameSequences {

    public static final FrameSequence stand = new FrameSequence() {
        {
            addFrame(0, true, 0.1f);
        }
    };

    public static final FrameSequence crouch = new FrameSequence() {
        {
            addFrame(7, true, 0.1f);
        }
    };

    public static final FrameSequence walkLeft = new FrameSequence() {
        {
            addFrame(1, true, 0.2f);
            addFrame(2, true, 0.3f);
            //addFrame(7, true, 0.2f);
            //addFrame(8, true, 0.2f);
        }
    };

    public static final FrameSequence walkRight = new FrameSequence() {
        {
            addFrame(1, true, 0.2f);
            addFrame(2, true, 0.1f);
            addFrame(0, true, 0.1f);
            addFrame(2, true, 0.2f);
            addFrame(1, true, 0.1f);
            addFrame(0, true, 0.1f);
            //addFrame(3, true, 0.2f);
            //addFrame(4, true, 0.2f);
        }
    };

    public static final FrameSequence runRight = new FrameSequence() {
        {
            addFrame(3, true, 0.1f);
            addFrame(1, true, 0.1f);
            addFrame(4, true, 0.1f);
            addFrame(2, true, 0.1f);
            //addFrame(3, true, 0.2f);
            //addFrame(4, true, 0.2f);
        }
    };

    public static final FrameSequence jumpRight = new FrameSequence() {
        {
            addFrame(5, false, 0.2f);
            addFrame(6, false, 0.2f);
            //addFrame(13, false, 0.2f);
            //addFrame(14, false, 0.2f);
            //setNextFrame(2);
        }
    };
}
