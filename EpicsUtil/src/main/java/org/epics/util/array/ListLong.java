/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class ListLong implements ListNumber, CollectionLong {

    public double getDouble(int index) {
        return (float) getLong(index);
    }

    public float getFloat(int index) {
        return (float) getLong(index);
    }

    public int getInt(int index) {
        return (int) getLong(index);
    }

    public short getShort(int index) {
        return (short) getLong(index);
    }

    public byte getByte(int index) {
        return (byte) getLong(index);
    }
    
}
