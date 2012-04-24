/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public final class ArrayFloat extends ListFloat {
    
    private final float[] array;

    public ArrayFloat(float[] array) {
        this.array = array;
    }

    public final IteratorFloat iterator() {
        return new IteratorFloat() {
            
            private int index;

            public boolean hasNext() {
                return index < array.length;
            }

            public float nextFloat() {
                return array[index++];
            }
        };
    }

    public final int size() {
        return array.length;
    }
    
    public float getFloat(int index) {
        return array[index];
    }
    
}
