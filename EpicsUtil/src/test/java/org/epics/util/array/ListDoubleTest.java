/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.util.array.CollectionTest.testCollection;
import static org.epics.util.array.ListTest.testList;

/**
 *
 * @author carcassi
 */
public class ListDoubleTest {
    
    public ListDoubleTest() {
    }
    
    @Test
    public void testListDouble() {
        ListDouble coll = new ListDouble() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public double getDouble(int index) {
                return 1.0;
            }
        };
        testCollection(coll);
        testList(coll);
    }
    
}
