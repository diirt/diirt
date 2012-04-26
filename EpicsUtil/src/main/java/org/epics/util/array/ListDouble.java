/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class ListDouble implements ListNumber, CollectionDouble {

    @Override
    public float getFloat(int index) {
        return (float) getDouble(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getDouble(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getDouble(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getDouble(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getDouble(index);
    }
    
}
