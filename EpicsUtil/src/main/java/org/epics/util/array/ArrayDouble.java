/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * Wraps a {@code double[]} into a {@link ListDouble}.
 *
 * @author Gabriele Carcassi
 */
public final class ArrayDouble extends ListDouble {
    
    private final double[] array;
    private final boolean writeable;

    /**
     * A new read-only {@code ArrayDouble} that wraps around the given array.
     * 
     * @param array an array
     */
    public ArrayDouble(double[] array) {
        this(array, false);
    }

    /**
     * A new {@code ArrayDouble} that wraps around the given array.
     * 
     * @param array an array
     */
    public ArrayDouble(double[] array, boolean writeable) {
        this.array = array;
        this.writeable = writeable;
    }

    @Override
    public final IteratorDouble iterator() {
        return new IteratorDouble() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public double nextDouble() {
                return array[index++];
            }
        };
    }

    @Override
    public final int size() {
        return array.length;
    }
    
    @Override
    public double getDouble(int index) {
        return array[index];
    }

    @Override
    public void setDouble(int index, double value) {
        if (writeable) {
            array[index] = value;
        } else {
            throw new UnsupportedOperationException("Read only list.");
        }
    }
    
}
