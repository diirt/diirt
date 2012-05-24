/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code byte}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListByte implements ListNumber, CollectionByte {
    
    @Override
    public boolean deepEquals(ListNumber other) {
        if (other == null)
            return false;
        
        if (size() != other.size())
            return false;
        
        for (int i = 0; i < size(); i++) {
            if (getByte(i) != other.getByte(i))
                return false;
        }
        
        return true;
    }

    @Override
    public IteratorByte iterator() {
        return new IteratorByte() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public byte nextByte() {
                return getByte(index++);
            }
        };
    }
    
    @Override
    public double getDouble(int index) {
        return (float) getByte(index);
    }

    @Override
    public float getFloat(int index) {
        return (float) getByte(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getByte(index);
    }

    @Override
    public int getInt(int index) {
        return (int) getByte(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getByte(index);
    }

    @Override
    public void setDouble(int index, double value) {
        setByte(index, (byte) value);
    }

    @Override
    public void setFloat(int index, float value) {
        setByte(index, (byte) value);
    }

    @Override
    public void setLong(int index, long value) {
        setByte(index, (byte) value);
    }

    @Override
    public void setInt(int index, int value) {
        setByte(index, (byte) value);
    }

    @Override
    public void setShort(int index, short value) {
        setByte(index, (byte) value);
    }

    @Override
    public void setByte(int index, byte value) {
        throw new UnsupportedOperationException("Read only list.");
    }
    
}
