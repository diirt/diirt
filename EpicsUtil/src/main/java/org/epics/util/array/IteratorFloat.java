/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class IteratorFloat implements IteratorNumber {

    public double nextDouble() {
        return (double) nextFloat();
    }

    public byte nextByte() {
        return (byte) nextFloat();
    }

    public short nextShort() {
        return (short) nextFloat();
    }

    public int nextInt() {
        return (int) nextFloat();
    }

    public long nextLong() {
        return (long) nextFloat();
    }
    
}
