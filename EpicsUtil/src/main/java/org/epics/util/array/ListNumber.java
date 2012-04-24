/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public interface ListNumber extends CollectionNumber {
    
    double getDouble(int index);
    float getFloat(int index);
    long getLong(int index);
    int getInt(int index);
    short getShort(int index);
    byte getByte(int index);
    
}
