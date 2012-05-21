/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of numeric (primitive) elements.
 *
 * @author Gabriele Carcassi
 */
public interface ListNumber extends CollectionNumber {
    
    /**
     * Returns the element at the specified position in this list casted to a double.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    double getDouble(int index);
    
    /**
     * Returns the element at the specified position in this list casted to a float.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    float getFloat(int index);
    
    /**
     * Returns the element at the specified position in this list casted to a long.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    long getLong(int index);
    
    /**
     * Returns the element at the specified position in this list casted to an int.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    int getInt(int index);
    
    /**
     * Returns the element at the specified position in this list casted to a short.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    short getShort(int index);
    
    /**
     * Returns the element at the specified position in this list casted to a byte.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    byte getByte(int index);
    
    void setDouble(int index, double value);
    
    void setFloat(int index, float value);
    
    void setLong(int index, long value);
    
    void setInt(int index, int value);
    
    void setShort(int index, short value);
    
    void setByte(int index, byte value);
    
}
