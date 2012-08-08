/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * Utilities to work with number collections.
 *
 * @author carcassi
 */
public class CollectionNumbers {
    
    public static double[] wrappedDoubleArray(CollectionNumber coll) {
        if (coll instanceof ArrayDouble) {
            return ((ArrayDouble) coll).wrappedArray();
        }
        
        return null;
    }
    
    /**
     * Copies the content of the collection to an array.
     * 
     * @param coll the collection
     * @return the array
     */
    public static double[] doubleArrayCopyOf(CollectionNumber coll) {
        double[] data = new double[coll.size()];
        IteratorNumber iter = coll.iterator();
        int index = 0;
        while (iter.hasNext()) {
            data[index] = iter.nextDouble();
            index++;
        }
        return data;
    }
}
