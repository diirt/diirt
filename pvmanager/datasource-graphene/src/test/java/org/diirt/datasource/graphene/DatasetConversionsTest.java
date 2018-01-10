/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.graphene;

import java.util.Arrays;
import java.util.List;
import org.diirt.graphene.Point2DDataset;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;
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

    @Test
    public void point2DDatsetsFromVTable1() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class, double.class, double.class),
                Arrays.asList("x1", "y1", "x2", "y2"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6), new ArrayDouble(10,20,30), new ArrayDouble(50,40,60)));
        List<Point2DDataset> datasets = DatasetConversions.point2DDatasetsFromVTable(data, Arrays.asList("x1", "x2"), Arrays.asList("y1", "y2"));
        assertThat(datasets.size(), equalTo(2));
        Point2DDataset dataset = datasets.get(0);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
        dataset = datasets.get(1);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(10,20,30)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(50,40,60)));
    }

    @Test
    public void point2DDatsetsFromVTable2() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList(double.class, double.class, double.class, double.class),
                Arrays.asList("x", "y1", "y2", "y3"), Arrays.<Object>asList(new ArrayDouble(1,2,3), new ArrayDouble(5,4,6), new ArrayDouble(10,20,30), new ArrayDouble(50,40,60)));
        List<Point2DDataset> datasets = DatasetConversions.point2DDatasetsFromVTable(data, Arrays.asList("x"), Arrays.asList("y1", "y2", "y3"));
        assertThat(datasets.size(), equalTo(3));
        Point2DDataset dataset = datasets.get(0);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
        dataset = datasets.get(1);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(10,20,30)));
        dataset = datasets.get(2);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(50,40,60)));
    }

    @Test
    public void point2DDatsetsFromVTable3() {
        VTable data = ValueFactory.newVTable(Arrays.<Class<?>>asList( double.class, double.class, double.class),
                Arrays.asList("y1", "y2", "y3"), Arrays.<Object>asList(new ArrayDouble(5,4,6), new ArrayDouble(10,20,30), new ArrayDouble(50,40,60)));
        List<Point2DDataset> datasets = DatasetConversions.point2DDatasetsFromVTable(data, null, null);
        assertThat(datasets.size(), equalTo(3));
        Point2DDataset dataset = datasets.get(0);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(0,1,2)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(5,4,6)));
        dataset = datasets.get(1);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(0,1,2)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(10,20,30)));
        dataset = datasets.get(2);
        assertThat(dataset.getCount(), equalTo(3));
        assertThat(dataset.getXValues(), equalTo((ListNumber) new ArrayDouble(0,1,2)));
        assertThat(dataset.getYValues(), equalTo((ListNumber) new ArrayDouble(50,40,60)));
    }
}