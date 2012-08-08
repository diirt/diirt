/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class CollectionNumbersTest {
    
    public CollectionNumbersTest() {
    }
    
    @Test
    public void toDoubleArray1() {
        double[] data = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ArrayDouble coll = new ArrayDouble(data);
        assertThat(CollectionNumbers.doubleArrayCopyOf(coll), equalTo(new double[] {0,1,2,3,4,5,6,7,8,9}));
    }
    
}
