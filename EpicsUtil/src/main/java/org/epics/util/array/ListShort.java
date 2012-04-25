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

    public double getDouble(int index) {
        return (float) getShort(index);
    }

    public float getFloat(int index) {
        return (float) getShort(index);
    }

    public long getLong(int index) {
        return (long) getShort(index);
    }

    public int getInt(int index) {
        return (int) getShort(index);
    }

    public byte getByte(int index) {
        return (byte) getShort(index);
    }
    
}
