/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class ListByte implements ListNumber, CollectionByte {

    @Override
    public double getDouble(int index) {
        return (float) getByte(index);
    }

    @Override
    public float getFloat(int index) {
        return (float) getByte(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getByte(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getByte(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getByte(index);
    }
    
}
