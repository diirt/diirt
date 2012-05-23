/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * Wraps a {@code long[]} into a {@link ListLong}.
 *
 * @author Gabriele Carcassi
 */
public final class ArrayLong extends ListLong {
    
    private final long[] array;
    private final boolean readOnly;

    /**
     * A new {@code ArrayLong} that wraps around the given array.
     * 
     * @param array an array
     */
    public ArrayLong(long[] array) {
        this(array, true);
    }

    /**
     * A new {@code ArrayLong} that wraps around the given array.
     * 
     * @param array an array
     * @param readOnly if false the wrapper allows writes to the array
     */
    public ArrayLong(long[] array, boolean readOnly) {
        this.array = array;
        this.readOnly = readOnly;
    }

    @Override
    public final IteratorLong iterator() {
        return new IteratorLong() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public long nextLong() {
                return array[index++];
            }
        };
    }

    @Override
    public final int size() {
        return array.length;
    }
    
    @Override
    public long getLong(int index) {
        return array[index];
    }

    @Override
    public void setLong(int index, long value) {
        if (!readOnly) {
            array[index] = value;
        } else {
            throw new UnsupportedOperationException("Read only list.");
        }
    }
    
}
