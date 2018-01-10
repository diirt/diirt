/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.stats.Statistics;
import org.diirt.util.array.ListNumber;
import org.diirt.util.stats.Range;

/**
 * Factory methods for wrapper datasets.
 *
 * @author carcassi
 */
public class Point1DDatasets {

    /**
     * Wraps a {@link ListNumber} into a {@link Point1DDataset}.
     * <p>
     * It assumes the argument is either immutable or mutable but
     * will not be changed in the future.
     *
     * @param values the values for the dataset
     * @return the dataset from the values; never null
     */
    public static Point1DDataset of(final ListNumber values) {
        final Statistics statistics = StatisticsUtil.statisticsOf(values);
        return new Point1DDataset() {

            @Override
            public ListNumber getValues() {
                return values;
            }

            @Override
            public Statistics getStatistics() {
                return statistics;
            }

            @Override
            public Range getDisplayRange() {
                return null;
            }

            @Override
            public int getCount() {
                return values.size();
            }
        };
    }
}
