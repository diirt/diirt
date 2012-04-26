/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public abstract class ListFloat implements ListNumber, CollectionFloat {

    @Override
    public double getDouble(int index) {
        return (double) getFloat(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getFloat(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getFloat(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getFloat(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getFloat(index);
    }
    
}
