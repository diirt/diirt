/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.array;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.diirt.util.array.CollectionTest.testCollection;

/**
 *
 * @author carcassi
 */
public class ListTest {

    public ListTest() {
    }

    @org.junit.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.junit.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testListFloat() {
        ListFloat coll = new ListFloat() {

            public int size() {
                return 10;
            }

            public float getFloat(int index) {
                return (float) 1.0;
            }
        };
        testCollection(coll);
    }

    @Test
    public void testListLong() {
        ListLong coll = new ListLong() {

            public int size() {
                return 10;
            }

            public long getLong(int index) {
                return 1L;
            }

        };
        testCollection(coll);
        testList(coll);
    }

    @Test
    public void testListInt() {
        ListInt coll = new ListInt() {

            public int size() {
                return 10;
            }

            public int getInt(int index) {
                return 1;
            }
        };
        testCollection(coll);
        testList(coll);
    }

    @Test
    public void testListShort() {
        ListShort coll = new ListShort() {

            public int size() {
                return 10;
            }

            public short getShort(int index) {
                return (short) 1;
            }
        };
        testCollection(coll);
        testList(coll);
    }

    @Test
    public void testListByte() {
        ListByte coll = new ListByte() {

            public int size() {
                return 10;
            }

            public byte getByte(int index) {
                return (byte) 1;
            }

        };
        testCollection(coll);
        testList(coll);
    }

    public static void testList(ListNumber coll) {
        assertEquals(10, coll.size());
        for (int i = 0; i < coll.size(); i++) {
            assertEquals(1.0, coll.getDouble(i), 0.00001);
            assertEquals((float) 1.0, coll.getFloat(i), 0.00001);
            assertEquals(1L, coll.getLong(i));
            assertEquals(1, coll.getInt(i));
            assertEquals((short) 1, coll.getShort(i));
            assertEquals((byte) 1, coll.getByte(i));
        }
    }

}
