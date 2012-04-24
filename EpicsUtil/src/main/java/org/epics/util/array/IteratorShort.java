/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class IteratorShort implements IteratorNumber {

    public float nextFloat() {
        return (float) nextShort();
    }

    public double nextDouble() {
        return (double) nextShort();
    }

    public byte nextByte() {
        return (byte) nextShort();
    }

    public int nextInt() {
        return (int) nextShort();
    }

    public long nextLong() {
        return (long) nextShort();
    }
    
}
