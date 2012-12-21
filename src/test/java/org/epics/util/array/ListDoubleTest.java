/**
 * Copyright (C) 2012 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
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
    
    @Test
    public void equals1() {
        ListDouble coll = new ListDouble() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public double getDouble(int index) {
                return index;
            }
        };
        ListDouble other = new ArrayDouble(new double[] {0,1,2,3,4,5,6,7,8,9});
        assertThat(coll, equalTo(other));
        assertThat(other, equalTo(coll));
    }
    
    @Test
    public void equals2() {
        ListNumber coll = new ArrayDouble(new double[] {0,1,2,3,4,5,6,7,8,9});
        ListNumber other = new ArrayFloat(new float[] {0,1,2,3,4,5,6,7,8,9});
        assertThat(other, not(equalTo(coll)));
        assertThat(coll, not(equalTo(other)));
    }
    
    @Test
    public void hashcode1() {
        ListDouble coll = new ListDouble() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public double getDouble(int index) {
                return index;
            }
        };
        ListDouble other = new ArrayDouble(new double[] {0,1,2,3,4,5,6,7,8,9});
        assertThat(coll.hashCode(), equalTo(other.hashCode()));
        assertThat(coll.hashCode(), equalTo(Arrays.hashCode(new double[] {0,1,2,3,4,5,6,7,8,9})));
    }
    
    @Test
    public void toString1() {
        ListDouble coll = new ListDouble() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public double getDouble(int index) {
                return index;
            }
        };
        assertThat(coll.toString(), equalTo("[0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0]"));
    }
    
}
