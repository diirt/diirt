/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * Wraps a {@code byte[]} into a {@link ListByte}.
 *
 * @author Gabriele Carcassi
 */
public final class ArrayByte extends ListByte {
    
    private final byte[] array;

    /**
     * A new {@code ArrayByte} that wraps around the given array.
     * 
     * @param array an array
     */
    public ArrayByte(byte[] array) {
        this.array = array;
    }

    @Override
    public final IteratorByte iterator() {
        return new IteratorByte() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public byte nextByte() {
                return array[index++];
            }
        };
    }

    @Override
    public final int size() {
        return array.length;
    }
    
    @Override
    public final byte getByte(int index) {
        return array[index];
    }
    
}
