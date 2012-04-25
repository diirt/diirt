/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class ListInt implements ListNumber, CollectionInt {

    public double getDouble(int index) {
        return (float) getInt(index);
    }

    public float getFloat(int index) {
        return (float) getInt(index);
    }

    public long getLong(int index) {
        return (long) getInt(index);
    }

    public short getShort(int index) {
        return (short) getInt(index);
    }

    public byte getByte(int index) {
        return (byte) getInt(index);
    }
    
}
