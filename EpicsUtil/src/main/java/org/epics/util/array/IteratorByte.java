/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class IteratorByte implements IteratorNumber {

    public float nextFloat() {
        return (float) nextByte();
    }

    public double nextDouble() {
        return (double) nextByte();
    }

    public short nextShort() {
        return (short) nextByte();
    }

    public int nextInt() {
        return (int) nextByte();
    }

    public long nextLong() {
        return (long) nextByte();
    }
    
}
