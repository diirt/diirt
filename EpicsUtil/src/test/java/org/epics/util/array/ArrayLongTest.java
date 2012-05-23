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
public class ArrayLongTest {
    
    public ArrayLongTest() {
    }

    @Test
    public void wrap1() {
        ArrayLong array = new ArrayLong(new long[] {0, 1, 2, 3, 4, 5});
        assertThat(CollectionNumbers.toDoubleArray(array), equalTo(new double[] {0, 1, 2, 3, 4, 5}));
    }

    @Test(expected=UnsupportedOperationException.class)
    public void wrap2() {
        ArrayLong array = new ArrayLong(new long[] {0, 1, 2, 3, 4, 5});
        array.setDouble(0, 0);
    }

    @Test
    public void wrap3() {
        ArrayLong array = new ArrayLong(new long[] {0, 1, 2, 3, 4, 5}, false);
        array.setDouble(0, 5);
        array.setDouble(5, 0);
        assertThat(CollectionNumbers.toDoubleArray(array), equalTo(new double[] {5, 1, 2, 3, 4, 0}));
    }
}
