/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code long}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListLong implements ListNumber, CollectionLong {

    @Override
    public IteratorLong iterator() {
        return new IteratorLong() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public long nextLong() {
                return getLong(index++);
            }
        };
    }

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
