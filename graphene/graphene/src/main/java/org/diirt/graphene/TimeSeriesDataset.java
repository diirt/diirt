/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Statistics;
import java.util.List;
import org.diirt.util.array.ListNumber;
import org.diirt.util.time.TimeInterval;
import java.time.Instant;

/**
 *
 * @author carcassi
 */
public interface TimeSeriesDataset {

    /**
     * The values.
     * <p>
     * If the dataset is empty, it returns an empty list.
     *
     * @return the values; never null
     */
    public ListNumber getValues();

    /**
     * The time for the values.
     * <p>
     * If the dataset is empty, it returns an empty list.
     *
     * @return time; never null
     */
    public List<Instant> getTimestamps();

    /**
     * Returns the time normalized within the range of the dataset.
     * <p>
     * If the dataset is empty, it returns an empty list.
     *
     * @param normalizationRange the interval over which the data is going to be displayed
     * @return normalized time; never null
     */
    public ListNumber getNormalizedTime( TimeInterval normalizationRange );

    /**
     * The statistical information of the values.
     * <p>
     * If the dataset is empty, or if it contains only NaN values, it returns null.
     *
     * @return statistical information; null if no actual values in the dataset
     */
    public Statistics getStatistics();

    /**
     * The interval of time where the data is defined.
     * <p>
     * If the dataset is empty, or if it contains only NaN values, it returns null.
     *
     * @return x statistical information; null if no actual values in the dataset
     */
    public TimeInterval getTimeInterval();

    /**
     * The number of values in the dataset.
     * <p>
     * This number matches the size of the list returned by {@link #getValues() }.
     *
     * @return the number of values in this dataset
     */
    public int getCount();
}
