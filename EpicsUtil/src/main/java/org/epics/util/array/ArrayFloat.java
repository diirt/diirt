/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * Wraps a {@code float[]} into a {@link ListFloat}.
 *
 * @author Gabriele Carcassi
 */
public final class ArrayFloat extends ListFloat {
    
    private final float[] array;

    /**
     * A new {@code ArrayFloat} that wraps around the given array.
     * 
     * @param array an array
     */
    public ArrayFloat(float[] array) {
        this.array = array;
    }

    @Override
    public final IteratorFloat iterator() {
        return new IteratorFloat() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public float nextFloat() {
                return array[index++];
            }
        };
    }

    @Override
    public final int size() {
        return array.length;
    }
    
    @Override
    public float getFloat(int index) {
        return array[index];
    }
    
}
