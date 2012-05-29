/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

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
        assertThat(other, equalTo(coll));
        assertThat(coll, equalTo(other));
    }
    
    @Test
    public void equals3() {
        ListNumber doubles = new ArrayDouble(new double[] {Double.MIN_VALUE});
        ListNumber floats = new ArrayFloat(new float[] {(float) Double.MIN_VALUE});
        ListNumber longs = new ArrayLong(new long[] {(long) Double.MIN_VALUE});
        ListNumber ints = new ArrayInt(new int[] {(int) Double.MIN_VALUE});
        ListNumber shorts = new ArrayShort(new short[] {(short) Double.MIN_VALUE});
        ListNumber bytes = new ArrayByte(new byte[] {(byte) Double.MIN_VALUE});
        assertThat(doubles, not(equalTo(floats)));
        assertThat(floats, not(equalTo(doubles)));
        assertThat(doubles, not(equalTo(longs)));
        assertThat(longs, not(equalTo(doubles)));
        assertThat(doubles, not(equalTo(ints)));
        assertThat(ints, not(equalTo(doubles)));
        assertThat(doubles, not(equalTo(shorts)));
        assertThat(shorts, not(equalTo(doubles)));
        assertThat(doubles, not(equalTo(bytes)));
        assertThat(bytes, not(equalTo(doubles)));
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
    }
    
    @Test
    public void allEqualHashcode() {
        ListNumber doubles = new ArrayDouble(new double[] {0,1,2,3,4,5,6,7,8,9});
        ListNumber floats = new ArrayFloat(new float[] {0,1,2,3,4,5,6,7,8,9});
        ListNumber longs = new ArrayLong(new long[] {0,1,2,3,4,5,6,7,8,9});
        ListNumber ints = new ArrayInt(new int[] {0,1,2,3,4,5,6,7,8,9});
        ListNumber shorts = new ArrayShort(new short[] {0,1,2,3,4,5,6,7,8,9});
        ListNumber bytes = new ArrayByte(new byte[] {0,1,2,3,4,5,6,7,8,9});
        assertThat(ints.hashCode(), equalTo(doubles.hashCode()));
        assertThat(ints.hashCode(), equalTo(floats.hashCode()));
        assertThat(ints.hashCode(), equalTo(longs.hashCode()));
        assertThat(ints.hashCode(), equalTo(shorts.hashCode()));
        assertThat(ints.hashCode(), equalTo(bytes.hashCode()));
    }
    
}
