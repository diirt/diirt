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

    @Override
    public double getDouble(int index) {
        return (float) getLong(index);
    }

    @Override
    public float getFloat(int index) {
        return (float) getLong(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getLong(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getLong(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getLong(index);
    }
    
}
