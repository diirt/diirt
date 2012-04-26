/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public final class ArrayDouble extends ListDouble {
    
    private final double[] array;

    public ArrayDouble(double[] array) {
        this.array = array;
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
    
}
