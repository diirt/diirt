/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class IteratorLong implements IteratorNumber {

    public float nextFloat() {
        return (float) nextLong();
    }

    public double nextDouble() {
        return (double) nextLong();
    }

    public byte nextByte() {
        return (byte) nextLong();
    }

    public short nextShort() {
        return (short) nextLong();
    }

    public int nextInt() {
        return (int) nextLong();
    }
    
}
