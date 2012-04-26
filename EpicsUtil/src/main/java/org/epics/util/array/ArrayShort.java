/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public final class ArrayShort extends ListShort {
    
    private final short[] array;

    public ArrayShort(short[] array) {
        this.array = array;
    }

    @Override
    public final IteratorShort iterator() {
        return new IteratorShort() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public short nextShort() {
                return array[index++];
            }
        };
    }

    @Override
    public final int size() {
        return array.length;
    }
    
    @Override
    public short getShort(int index) {
        return array[index];
    }
    
}
