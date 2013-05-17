/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.graphene;

import java.util.Arrays;
import org.epics.graphene.Point2DDataset;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class DatasetConversionsTest {
    
    public DatasetConversionsTest() {
    }

    @Test
    public void point2DDatsetFromVTable1() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, "x", "y");
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test
    public void point2DDatsetFromVTable2() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, "x", null);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test
    public void point2DDatsetFromVTable3() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, "y");
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void point2DDatsetFromVTable4() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, "z");
    }

    @Test
    public void point2DDatsetFromVTable5() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, null);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void point2DDatsetFromVTable6() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, String.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), Arrays.asList("a", "b", "c")));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void point2DDatsetFromVTable7() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class),
                Arrays.asList("x"), Arrays.<Object>asList(new ArrayDouble(1,2,3)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, null);
    }

    @Test
    public void point2DDatsetFromVTable8() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, String.class, double.class),
                Arrays.asList("x", "y", "z"), Arrays.<Object>asList(new ArrayDouble(1,2,3), Arrays.asList("a", "b", "c"), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, null);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test
    public void point2DDatsetFromVTable9() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(String.class, double.class, double.class),
                Arrays.asList("x", "y", "z"), Arrays.<Object>asList(Arrays.asList("a", "b", "c"), new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, null);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
    }

    @Test
    public void point2DDatsetFromVTable10() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class),
                Arrays.asList("x", "y"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6)));
        Point2DDataset dataset = DatasetConversions.point2DDatasetFromVTable(data, null, "x");
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
    }
}