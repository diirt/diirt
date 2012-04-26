/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

import java.util.Iterator;

/**
 *
 * @author carcassi
 */
public interface IteratorNumber extends Iterator<Number> {
    
    boolean hasNext();
    
    float nextFloat();
    
    double nextDouble();
    
    byte nextByte();
    
    short nextShort();
    
    int nextInt();
    
    long nextLong();
}
