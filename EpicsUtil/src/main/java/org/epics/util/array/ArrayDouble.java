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

    public final IteratorDouble iterator() {
        return new IteratorDouble() {
            
            private int index;

            public boolean hasNext() {
                return index < array.length;
            }

            public double nextDouble() {
                return array[index++];
            }
        };
    }

    public final int size() {
        return array.length;
    }
    
    public double getDouble(int index) {
        return array[index];
    }
    
}
