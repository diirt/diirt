/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class ListShort implements ListNumber, CollectionShort {

    @Override
    public double getDouble(int index) {
        return (float) getShort(index);
    }

    @Override
    public float getFloat(int index) {
        return (float) getShort(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getShort(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getShort(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getShort(index);
    }
    
}
