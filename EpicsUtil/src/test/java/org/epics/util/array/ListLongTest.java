/*
 * Copyright 2011 Brookhaven National Laboratory
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
public class ListLongTest {
    
    public ListLongTest() {
    }
    
    @Test
    public void list1() {
        ListLong coll = new ListLong() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public long getLong(int index) {
                return 1L;
            }
        };
        testCollection(coll);
        testList(coll);
    }
    
    @Test
    public void equals1() {
        ListLong coll = new ListLong() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public long getLong(int index) {
                return index;
            }
        };
        ListLong other = new ArrayLong(new long[] {0,1,2,3,4,5,6,7,8,9});
        assertThat(coll, equalTo(other));
        assertThat(other, equalTo(coll));
    }
    
    @Test
    public void equals2() {
        ListNumber doubles = new ArrayDouble(new double[] {(float) Long.MIN_VALUE});
        ListNumber floats = new ArrayFloat(new float[] {(float) Long.MIN_VALUE});
        ListNumber longs = new ArrayLong(new long[] {(long) Long.MIN_VALUE});
        ListNumber ints = new ArrayInt(new int[] {(int) Long.MIN_VALUE});
        ListNumber shorts = new ArrayShort(new short[] {(short) Long.MIN_VALUE});
        ListNumber bytes = new ArrayByte(new byte[] {(byte) Long.MIN_VALUE});
        assertThat(longs, equalTo(doubles));
        assertThat(doubles, equalTo(longs));
        assertThat(longs, equalTo(floats));
        assertThat(floats, equalTo(longs));
        assertThat(longs, not(equalTo(ints)));
        assertThat(ints, not(equalTo(longs)));
        assertThat(longs, not(equalTo(shorts)));
        assertThat(shorts, not(equalTo(longs)));
        assertThat(longs, not(equalTo(bytes)));
        assertThat(bytes, not(equalTo(longs)));
    }
    
    @Test
    public void hashcode1() {
        ListLong coll = new ListLong() {

            @Override
            public int size() {
                return 10;
            }

            @Override
            public long getLong(int index) {
                return index;
            }
        };
        ListLong other = new ArrayLong(new long[] {0,1,2,3,4,5,6,7,8,9});
        assertThat(coll.hashCode(), equalTo(other.hashCode()));
    }
    
}
