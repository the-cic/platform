/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cic.platform.util;

/**
 *
 * @author Cic
 */
public class Transition {
    private float value1;
    private float value2;
    private float value;
    private float delta;
    private boolean finished;
    private float easeIn;
    private float easeInAcceleration;

    public Transition(float fromValue, float toValue, float speed){
        this(fromValue, toValue, speed, 1f, 1f);
    }

    public Transition(float fromValue, float toValue, float speed, float easeIn){
        this(fromValue, toValue, speed, easeIn, 1f);
    }

    public Transition(float fromValue, float toValue, float speed, float easeIn, float easeInAcceleration){
        value1 = fromValue;
        value2 = toValue;
        value = value1;
        delta = Math.abs(speed);
        if (value2 < value1) {
            delta = -delta;
        }
        finished = false;
        this.easeIn = easeIn;
        this.easeInAcceleration = easeInAcceleration;
    }

    public void update(float tpf){
        value += delta * tpf * easeIn;
        easeIn += (1 - easeIn) * easeInAcceleration * tpf;
        if ((delta > 0 && value > value2) || (delta < 0 && value < value2)) {
            finished = true;
            value = value2;
        }
    }

    public float getValue(){
        return value;
    }

    public boolean isFinished(){
        return finished;
    }
}
