/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class IteratorInt implements IteratorNumber {

    public void remove() {
        throw new UnsupportedOperationException("Read only iterator.");
    }

    public Number next() {
        return nextInt();
    }

    public float nextFloat() {
        return (float) nextInt();
    }

    public double nextDouble() {
        return (double) nextInt();
    }

    public byte nextByte() {
        return (byte) nextInt();
    }

    public short nextShort() {
        return (short) nextInt();
    }

    public long nextLong() {
        return (long) nextInt();
    }
    
}
