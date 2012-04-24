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

    public float getFloat(int index) {
        return (float) getDouble(index);
    }

    public long getLong(int index) {
        return (long) getDouble(index);
    }

    public int getInt(int index) {
        return (int) getDouble(index);
    }

    public short getShort(int index) {
        return (short) getDouble(index);
    }

    public byte getByte(int index) {
        return (byte) getDouble(index);
    }
    
}
