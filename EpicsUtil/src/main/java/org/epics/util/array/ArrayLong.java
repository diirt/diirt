/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public final class ArrayLong extends ListLong {
    
    private final long[] array;

    public ArrayLong(long[] array) {
        this.array = array;
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
    
}
