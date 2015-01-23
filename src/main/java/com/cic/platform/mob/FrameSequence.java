package com.cic.platform.mob;

import java.util.ArrayList;

/**
 *
 * @author Cic
 */
public abstract class FrameSequence {

    private ArrayList<Integer> frameIndexList = new ArrayList<Integer>();;
    private ArrayList<Integer> frameNextSequenceIndexList = new ArrayList<Integer>();
    private ArrayList<Boolean> frameInterruptableList = new ArrayList<Boolean>();
    private ArrayList<Float> frameDurationList = new ArrayList<Float>();

    private int sequenceIndex = 0;
    private int maxSequenceIndex = -1;
    private float time = 0;
    private int nextSequenceIndex;

    public int frameIndex;
    public boolean canBeInterrupted;
    public float frameDuration;

    public FrameSequence addFrame(int frameIndex, boolean interruptable, float duration){
        frameIndexList.add(frameIndex);
        frameInterruptableList.add(interruptable);
        frameDurationList.add(duration);
        maxSequenceIndex = frameIndexList.size() - 1; // current last added frame
        frameNextSequenceIndexList.add(maxSequenceIndex + 1); // every last added frame points to next frame
        return this;
    }

    public FrameSequence setNextFrame(int nextIndex){
        frameNextSequenceIndexList.set(maxSequenceIndex, nextIndex);
        return this;
    }

    public void start() {
        nextSequenceIndex = 0;
        advanceFrame();
        time = 0;
    }

    public boolean advance(float tpf){
        time += tpf;
        boolean advanced = false;
        while (time > frameDuration) {
            time -= frameDuration;
            advanceFrame();
            advanced = true;
        }
        return advanced;
    }

    private void advanceFrame(){
        sequenceIndex = nextSequenceIndex;
        if (sequenceIndex > maxSequenceIndex) {
            sequenceIndex = 0;
        }
        frameIndex = frameIndexList.get(sequenceIndex);
        canBeInterrupted = frameInterruptableList.get(sequenceIndex);
        frameDuration = frameDurationList.get(sequenceIndex);
        nextSequenceIndex = frameNextSequenceIndexList.get(sequenceIndex);
    }
}
