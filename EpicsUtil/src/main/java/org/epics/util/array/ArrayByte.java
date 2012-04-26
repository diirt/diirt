/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public final class ArrayByte extends ListByte {
    
    private final byte[] array;

    public ArrayByte(byte[] array) {
        this.array = array;
    }

    public final IteratorByte iterator() {
        return new IteratorByte() {
            
            private int index;

            public boolean hasNext() {
                return index < array.length;
            }

            public byte nextByte() {
                return array[index++];
            }
        };
    }

    public final int size() {
        return array.length;
    }
    
    public final byte getByte(int index) {
        return array[index];
    }
    
}
