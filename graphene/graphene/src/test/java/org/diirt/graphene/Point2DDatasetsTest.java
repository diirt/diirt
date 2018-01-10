/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
/**
 *
 * @author Jiakung
 */
public class Point2DDatasetsTest {
    public Point2DDatasetsTest(){

    }

//Not sure how assertEquals works
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void lineDataFromList() {
        ListNumber data = new ArrayDouble(1,2,3);
        Point2DDataset dataset = Point2DDatasets.lineData(data);
        assertEquals(3, dataset.getCount());
        assertEquals(0.0, dataset.getXValues().getDouble(0), 0.001);
        assertEquals(1.0, dataset.getXValues().getDouble(1), 0.001);
        assertEquals(2.0, dataset.getXValues().getDouble(2), 0.001);
        assertEquals(1.0, dataset.getYValues().getDouble(0), 0.001);
        assertEquals(2.0, dataset.getYValues().getDouble(1), 0.001);
        assertEquals(3.0, dataset.getYValues().getDouble(2), 0.001);
    }


    @Test
    public void lineDataFromListAndOffset(){


    }

    @Test
    public void lineDataFromRange(){

    }

    @Test
    public void lineDataFromFirstLastListNum(){

    }
// TODO: Are these supposed to be tested as well (methods that use arrays)?
//    @Test
//    public void testArrayLineData() {
//        Point2DDataset dataset = Point2DDatasets.lineData(new ArrayDouble(1, 2, 3));
//        assertEquals(3, dataset.getCount());
//        assertEquals(0.0, dataset.getXValues().getDouble(0), 0.001);
//        assertEquals(1.0, dataset.getXValues().getDouble(1), 0.001);
//        assertEquals(2.0, dataset.getXValues().getDouble(2), 0.001);
//        assertEquals(1.0, dataset.getYValues().getDouble(0), 0.001);
//        assertEquals(2.0, dataset.getYValues().getDouble(1), 0.001);
//        assertEquals(3.0, dataset.getYValues().getDouble(2), 0.001);
//    }
//
//    @Test
//    public void testArrayScaledLineData() {
//        Point2DDataset dataset = Point2DDatasets.lineData(new ArrayDouble(1, 2, 3), 10, 5);
//        assertEquals(3, dataset.getCount());
//        assertEquals(10.0, dataset.getXValues().getDouble(0), 0.001);
//        assertEquals(15.0, dataset.getXValues().getDouble(1), 0.001);
//        assertEquals(20.0, dataset.getXValues().getDouble(2), 0.001);
//        assertEquals(1.0, dataset.getYValues().getDouble(0), 0.001);
//        assertEquals(2.0, dataset.getYValues().getDouble(1), 0.001);
//        assertEquals(3.0, dataset.getYValues().getDouble(2), 0.001);
//    }
//
//    @Test
//    public void testDoubleArrayLineData() {
//        Point2DDataset dataset = Point2DDatasets.lineData(new ArrayDouble(1, 2, 3), new ArrayDouble(3,7,5));
//        assertEquals(3, dataset.getCount());
//        assertEquals(1.0, dataset.getXValues().getDouble(0), 0.001);
//        assertEquals(2.0, dataset.getXValues().getDouble(1), 0.001);
//        assertEquals(3.0, dataset.getXValues().getDouble(2), 0.001);
//        assertEquals(3.0, dataset.getYValues().getDouble(0), 0.001);
//        assertEquals(7.0, dataset.getYValues().getDouble(1), 0.001);
//        assertEquals(5.0, dataset.getYValues().getDouble(2), 0.001);
//    }
}
