/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public final class ArrayInt extends ListInt {
    
    private final int[] array;

    public ArrayInt(int[] array) {
        this.array = array;
    }

    @Override
    public final IteratorInt iterator() {
        return new IteratorInt() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public int nextInt() {
                return array[index++];
            }
        };
    }

    @Override
    public final int size() {
        return array.length;
    }
    
    @Override
    public int getInt(int index) {
        return array[index];
    }
    
}
