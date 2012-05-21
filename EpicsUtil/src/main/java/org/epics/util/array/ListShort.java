/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code short}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListShort implements ListNumber, CollectionShort {

    @Override
    public IteratorShort iterator() {
        return new IteratorShort() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public short nextShort() {
                return getShort(index++);
            }
        };
    }

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
