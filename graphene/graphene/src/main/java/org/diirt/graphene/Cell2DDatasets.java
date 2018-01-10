/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.stats.Statistics;
import org.diirt.util.stats.Range;
import org.diirt.util.array.*;
import org.diirt.util.stats.Ranges;

/**
 * Factory methods for wrapper datasets.
 *
 * @author carcassi
 */
public class Cell2DDatasets {

    public static interface Function2D {

        public double getValue(double x, double y);

    }

    /**
     * returns a Cell2DDataset, which is a 1D list of values that is treated
     * like a 2D matrix.
     *
     * @param data 1D list of z-values. x and y coordinates are calculated by
     * partitioning the matrix into pieces of length xCount. # of rows = yCount
     * # of cols = xCount.
     * @param xRange the range of the x
     * @param xCount the number of x cells
     * @param yRange the range of the y
     * @param yCount the number of y cells
     * @return the new dataset
     */
    public static Cell2DDataset linearRange(final ListNumber data, final Range xRange, final int xCount, final Range yRange, final int yCount) {
        if (data.size() == 0) {
            throw new IllegalArgumentException("Empty Dataset. zData size = " + data.size());
        }
        if (xCount <= 0 || yCount <= 0) {
            throw new IllegalArgumentException("Number of X (or Y) values must be greater than 0. xCount = " + xCount + " yCount = " + yCount);
        }

        final int expectedSize = xCount * yCount;

        if (expectedSize != data.size()) {
            throw new IllegalArgumentException("Unexpected number of X (or Y) values. Array length = " + (data.size()) + ", Predicted size(given X and Y) = "
                    + expectedSize + ", xCount = " + xCount + ", yCount = " + yCount);
        }

        final ListNumber xBoundaries = ListNumbers.linearListFromRange(xRange.getMinimum(), xRange.getMaximum(), xCount + 1);
        final ListNumber yBoundaries = ListNumbers.linearListFromRange(yRange.getMinimum(), yRange.getMaximum(), yCount + 1);

        final Statistics stats = StatisticsUtil.statisticsOf(data);
        return new Cell2DDataset() {
            @Override
            public double getValue(int x, int y) {
                return data.getDouble(y * xCount + x);
            }

            @Override
            public Statistics getStatistics() {
                return stats;
            }

            @Override
            public Range getDisplayRange() {
                return stats.getRange();
            }

            @Override
            public ListNumber getXBoundaries() {
                return xBoundaries;
            }

            @Override
            public Range getXRange() {
                return xRange;
            }

            @Override
            public int getXCount() {
                return xCount;
            }

            @Override
            public ListNumber getYBoundaries() {
                return yBoundaries;
            }

            @Override
            public Range getYRange() {
                return yRange;
            }

            @Override
            public int getYCount() {
                return yCount;
            }
        };
    }

    public static Cell2DDataset linearRange(final Function2D function, final Range xRange, final int xCount, final Range yRange, final int yCount) {

        final ListNumber xBoundaries = ListNumbers.linearListFromRange(xRange.getMinimum(), xRange.getMaximum(), xCount + 1);
        final ListNumber yBoundaries = ListNumbers.linearListFromRange(yRange.getMinimum(), yRange.getMaximum(), yCount + 1);
        final double xHalfStep = (xBoundaries.getDouble(1) - xBoundaries.getDouble(0)) / 2.0;
        final double yHalfStep = (yBoundaries.getDouble(1) - yBoundaries.getDouble(0)) / 2.0;
        CollectionNumber data = new CollectionDouble() {

            @Override
            public IteratorDouble iterator() {
                return new IteratorDouble() {
                    int x;
                    int y;

                    @Override
                    public boolean hasNext() {
                        return y < yCount;
                    }

                    @Override
                    public double nextDouble() {
                        double value = function.getValue(xBoundaries.getDouble(x) + xHalfStep, yBoundaries.getDouble(y) + yHalfStep);
                        x++;
                        if (x == xCount) {
                            x = 0;
                            y++;
                        }
                        return value;
                    }
                };
            }

            @Override
            public int size() {
                return xCount * yCount;
            }
        };
        final Statistics stats = StatisticsUtil.statisticsOf(data);
        return new Cell2DDataset() {
            @Override
            public double getValue(int x, int y) {
                return function.getValue(xBoundaries.getDouble(x) + xHalfStep, yBoundaries.getDouble(y) + yHalfStep);
            }

            @Override
            public Statistics getStatistics() {
                return stats;
            }

            @Override
            public Range getDisplayRange() {
                return stats.getRange();
            }

            @Override
            public ListNumber getXBoundaries() {
                return xBoundaries;
            }

            @Override
            public Range getXRange() {
                return xRange;
            }

            @Override
            public int getXCount() {
                return xCount;
            }

            @Override
            public ListNumber getYBoundaries() {
                return yBoundaries;
            }

            @Override
            public Range getYRange() {
                return yRange;
            }

            @Override
            public int getYCount() {
                return yCount;
            }
        };
    }

    public static Cell2DDataset datasetFrom(final Function2D function, final ListNumber xBoundaries, final ListNumber yBoundaries) {
        int yCount = yBoundaries.size() - 1;
        int xCount = xBoundaries.size() - 1;
        double[] values = new double[yCount * xCount];
        for (int y = 0; y < yCount; y++) {
            double middleY = (yBoundaries.getDouble(y) + yBoundaries.getDouble(y + 1)) / 2;
            for (int x = 0; x < xCount; x++) {
                double middleX = (xBoundaries.getDouble(x) + xBoundaries.getDouble(x + 1)) / 2;
                values[y * xCount + x] = function.getValue(middleX, middleY);
            }
        }
        ListDouble data = new ArrayDouble(values);
        return datasetFrom(data, xBoundaries, yBoundaries);
    }

    public static Cell2DDataset datasetFrom(final ListNumber values, final ListNumber xBoundaries, final ListNumber yBoundaries) {
        final Statistics statistics = StatisticsUtil.statisticsOf(values);
        final Range xRange = Ranges.range(xBoundaries.getDouble(0), xBoundaries.getDouble(xBoundaries.size() - 1));
        final Range yRange = Ranges.range(yBoundaries.getDouble(0), yBoundaries.getDouble(yBoundaries.size() - 1));

        // Check boundary sizes correspond match the number of points.
        final int xCount = xBoundaries.size() - 1;
        final int yCount = yBoundaries.size() - 1;
        if (values.size() != xCount * yCount) {
            throw new IllegalArgumentException("Number of boundaries do not match number of cells (" + xCount + " * " + yCount + " !+ " + values.size() + ")");
        }
        return new Cell2DDataset() {

            @Override
            public double getValue(int x, int y) {
                return values.getDouble(y * xCount + x);
            }

            @Override
            public Statistics getStatistics() {
                return statistics;
            }

            @Override
            public Range getDisplayRange() {
                return statistics.getRange();
            }

            @Override
            public ListNumber getXBoundaries() {
                return xBoundaries;
            }

            @Override
            public Range getXRange() {
                return xRange;
            }

            @Override
            public int getXCount() {
                return xCount;
            }

            @Override
            public ListNumber getYBoundaries() {
                return yBoundaries;
            }

            @Override
            public Range getYRange() {
                return yRange;
            }

            @Override
            public int getYCount() {
                return yCount;
            }
        };
    }
}
