/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.diirt.util.stats.Ranges;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * TODO: add tests to cover remaining methods
 *
 * @author carcassi
 */
public class Cell2DDatasetsTest {

    @Test
    public void linearRangeFromFunction2D() {
        Cell2DDataset dataset = Cell2DDatasets.linearRange(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x + y;
            }
        }, Ranges.range(0, 1), 10, Ranges.range(0, 1), 20);

        assertThat(dataset.getXCount(), equalTo(10));
        assertThat(dataset.getXRange().getMinimum(), equalTo(0.0));
        assertThat(dataset.getXRange().getMaximum(), equalTo(1.0));
        assertThat(dataset.getYCount(), equalTo(20));
        assertThat(dataset.getYRange().getMinimum(), equalTo(0.0));
        assertThat(dataset.getYRange().getMaximum(), equalTo(1.0));
        assertThat(dataset.getStatistics().getAverage(), closeTo(1.0, 0.0001));
        assertThat(dataset.getStatistics().getStdDev(), closeTo(0.40697, 0.0001));
        assertThat(dataset.getStatistics().getCount(), equalTo(200));
        assertThat(dataset.getStatistics().getRange().getMinimum(), closeTo(0.075, 0.0001));
        assertThat(dataset.getStatistics().getRange().getMaximum(), closeTo(1.925, 0.0001));

        // Check values
        assertThat(dataset.getXBoundaries().getDouble(0), equalTo(0.0));
        assertThat(dataset.getYBoundaries().getDouble(0), equalTo(0.0));
        assertThat(dataset.getValue(0, 0), closeTo(0.075, 0.0001));
        assertThat(dataset.getValue(9, 19), closeTo(1.925, 0.0001));
        assertThat(dataset.getXBoundaries().getDouble(10), equalTo(1.0));
        assertThat(dataset.getYBoundaries().getDouble(20), equalTo(1.0));
    }

//    @Test
//    //Not sure how to test this method
//    public void linearRangeFromListNumber(){
//        ListNumber values = new ArrayDouble(5, 3, 7, -1, 2, 2);
//        Range xRange = Ranges.range(0.0, 10.0);
//        Range yRange = Ranges.range(0.0,20.0);
//        int xCount = 5;
//        int yCount = 10;
//        Cell2DDataset dataset = Cell2DDatasets.linearRange(values, xRange, xCount, yRange, yCount);
//    }

    @Test
    public void datasetFromListNumber() {
        ListNumber values = new ArrayDouble(5, 3, 7, -1, 2, 2);
        ListNumber xBoundaries = new ArrayDouble(-1, 0, 1);
        ListNumber yBoundaries = new ArrayDouble(0, 1, 2, 3);
        Cell2DDataset dataset = Cell2DDatasets.datasetFrom(values, xBoundaries, yBoundaries);

        assertThat(dataset.getXCount(), equalTo(2));
        assertThat(dataset.getXRange().getMinimum(), equalTo(-1.0));
        assertThat(dataset.getXRange().getMaximum(), equalTo(1.0));
        assertThat(dataset.getYCount(), equalTo(3));
        assertThat(dataset.getYRange().getMinimum(), equalTo(0.0));
        assertThat(dataset.getYRange().getMaximum(), equalTo(3.0));
        assertThat(dataset.getStatistics().getAverage(), equalTo(3.0));
        assertThat(dataset.getStatistics().getStdDev(), closeTo(2.51661, 0.0001));
        assertThat(dataset.getStatistics().getRange().getMinimum(), equalTo((Number) (-1.0)));
        assertThat(dataset.getStatistics().getRange().getMaximum(), equalTo((Number) 7.0));
        assertThat(dataset.getStatistics().getCount(), equalTo(6));

        // Check values
        assertThat(dataset.getXBoundaries().getDouble(0), equalTo(-1.0));
        assertThat(dataset.getXBoundaries().getDouble(1), equalTo(0.0));
        assertThat(dataset.getXBoundaries().getDouble(2), equalTo(1.0));
        assertThat(dataset.getYBoundaries().getDouble(0), equalTo(0.0));
        assertThat(dataset.getYBoundaries().getDouble(1), equalTo(1.0));
        assertThat(dataset.getYBoundaries().getDouble(2), equalTo(2.0));
        assertThat(dataset.getYBoundaries().getDouble(3), equalTo(3.0));
        assertThat(dataset.getValue(0, 0), equalTo(5.0));
        assertThat(dataset.getValue(1, 0), equalTo(3.0));
        assertThat(dataset.getValue(0, 1), equalTo(7.0));
        assertThat(dataset.getValue(1, 1), equalTo(-1.0));
        assertThat(dataset.getValue(0, 2), equalTo(2.0));
        assertThat(dataset.getValue(1, 2), equalTo(2.0));
    }

    @Test
    public void datasetFromFunction2D() {
        Cell2DDataset dataset = Cell2DDatasets.datasetFrom(new Cell2DDatasets.Function2D() {
            @Override
            public double getValue(double x, double y) {
                return x + y;
            }
        }, new ArrayDouble(0, 2, 3, 5), new ArrayDouble(0, 1, 3));

        assertThat(dataset.getXCount(), equalTo(3));
        assertThat(dataset.getXRange().getMinimum(), equalTo(0.0));
        assertThat(dataset.getXRange().getMaximum(), equalTo(5.0));
        assertThat(dataset.getYCount(), equalTo(2));
        assertThat(dataset.getYRange().getMinimum(), equalTo(0.0));
        assertThat(dataset.getYRange().getMaximum(), equalTo(3.0));
        assertThat(dataset.getStatistics().getAverage(), equalTo(3.75));
        assertThat(dataset.getStatistics().getStdDev(), closeTo(1.43614, 0.0001));
        assertThat(dataset.getStatistics().getRange().getMinimum(), equalTo((Number) (1.5)));
        assertThat(dataset.getStatistics().getRange().getMaximum(), equalTo((Number) 6.0));
        assertThat(dataset.getStatistics().getCount(), equalTo(6));

        // Check values
        assertThat(dataset.getXBoundaries().getDouble(0), equalTo(0.0));
        assertThat(dataset.getXBoundaries().getDouble(1), equalTo(2.0));
        assertThat(dataset.getXBoundaries().getDouble(2), equalTo(3.0));
        assertThat(dataset.getXBoundaries().getDouble(3), equalTo(5.0));
        assertThat(dataset.getYBoundaries().getDouble(0), equalTo(0.0));
        assertThat(dataset.getYBoundaries().getDouble(1), equalTo(1.0));
        assertThat(dataset.getYBoundaries().getDouble(2), equalTo(3.0));
        assertThat(dataset.getValue(0, 0), equalTo(1.5));
        assertThat(dataset.getValue(1, 0), equalTo(3.0));
        assertThat(dataset.getValue(2, 0), equalTo(4.5));
        assertThat(dataset.getValue(0, 1), equalTo(3.0));
        assertThat(dataset.getValue(1, 1), equalTo(4.5));
        assertThat(dataset.getValue(2, 1), equalTo(6.0));
    }
}
