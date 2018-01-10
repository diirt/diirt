/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.stats.Statistics;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;
import org.diirt.util.stats.Range;

/**
 *
 * @author carcassi
 */
public class MockOrderedDataset2D implements Point2DDataset {

    private ListNumber xValues;
    private ListNumber yValues;
    private Statistics xStatistics;
    private Statistics yStatistics;

    public MockOrderedDataset2D(double[] xValues, double[] yValues) {
        this.xValues = new ArrayDouble(xValues);
        this.yValues = new ArrayDouble(yValues);
        xStatistics = StatisticsUtil.statisticsOf(this.xValues);
        yStatistics = StatisticsUtil.statisticsOf(this.yValues);
    }

    @Override
    public ListNumber getXValues() {
        return xValues;
    }

    @Override
    public ListNumber getYValues() {
        return yValues;
    }

    @Override
    public Statistics getXStatistics() {
        return xStatistics;
    }

    @Override
    public Statistics getYStatistics() {
        return yStatistics;
    }

    @Override
    public Range getXDisplayRange() {
        return null;
    }

    @Override
    public Range getYDisplayRange() {
        return null;
    }

    @Override
    public int getCount() {
        return xValues.size();
    }

}
