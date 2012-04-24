/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 *
 * @author carcassi
 */
public interface IteratorNumber {
    
    boolean hasNext();
    
    float nextFloat();
    
    double nextDouble();
    
    byte nextByte();
    
    short nextShort();
    
    int nextInt();
    
    long nextLong();
}
