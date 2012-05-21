/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code int}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListInt implements ListNumber, CollectionInt {

    @Override
    public IteratorInt iterator() {
        return new IteratorInt() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public int nextInt() {
                return getInt(index++);
            }
        };
    }

    @Override
    public double getDouble(int index) {
        return (float) getInt(index);
    }

    @Override
    public float getFloat(int index) {
        return (float) getInt(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getInt(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getInt(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getInt(index);
    }
    
}
