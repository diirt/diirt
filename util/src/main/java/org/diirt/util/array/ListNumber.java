/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.array;

/**
 * An ordered collection of numeric (primitive) elements.
 *
 * @author Gabriele Carcassi
 */
public interface ListNumber extends CollectionNumber {

    /**
     * Returns the element at the specified position in this list casted to a double.
     *
     * @param index position of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    double getDouble(int index);

    /**
     * Returns the element at the specified position in this list casted to a float.
     *
     * @param index position of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    float getFloat(int index);

    /**
     * Returns the element at the specified position in this list casted to a long.
     *
     * @param index position of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    long getLong(int index);

    /**
     * Returns the element at the specified position in this list casted to an int.
     *
     * @param index position of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    int getInt(int index);

    /**
     * Returns the element at the specified position in this list casted to a short.
     *
     * @param index position of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    short getShort(int index);

    /**
     * Returns the element at the specified position in this list casted to a byte.
     *
     * @param index position of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    byte getByte(int index);

    /**
     * Changes the element at the specified position, casting to the internal
     * representation.
     *
     * @param index position of the element to change
     * @param value the new value
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    void setDouble(int index, double value);

    /**
     * Changes the element at the specified position, casting to the internal
     * representation.
     *
     * @param index position of the element to change
     * @param value the new value
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    void setFloat(int index, float value);

    /**
     * Changes the element at the specified position, casting to the internal
     * representation.
     *
     * @param index position of the element to change
     * @param value the new value
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    void setLong(int index, long value);

    /**
     * Changes the element at the specified position, casting to the internal
     * representation.
     *
     * @param index position of the element to change
     * @param value the new value
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    void setInt(int index, int value);

    /**
     * Changes the element at the specified position, casting to the internal
     * representation.
     *
     * @param index position of the element to change
     * @param value the new value
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    void setShort(int index, short value);

    /**
     * Changes the element at the specified position, casting to the internal
     * representation.
     *
     * @param index position of the element to change
     * @param value the new value
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    void setByte(int index, byte value);

}
