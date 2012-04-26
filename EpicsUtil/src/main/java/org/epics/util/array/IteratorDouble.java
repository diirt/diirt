/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class IteratorDouble implements IteratorNumber {

    public void remove() {
        throw new UnsupportedOperationException("Read only iterator.");
    }

    public Number next() {
        return nextDouble();
    }

    public float nextFloat() {
        return (float) nextDouble();
    }

    public byte nextByte() {
        return (byte) nextDouble();
    }

    public short nextShort() {
        return (short) nextDouble();
    }

    public int nextInt() {
        return (int) nextDouble();
    }

    public long nextLong() {
        return (long) nextDouble();
    }
    
}
