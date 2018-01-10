/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.Statistics;
import org.diirt.util.array.ListNumber;
import org.diirt.util.stats.Range;

/**
 * A dataset consisting on a set of 1D points.
 * <p>
 * It represents a list of ordered values, and their statistical information.
 * The order may not be meaningful, but can be used to identify the points.
 *
 * @author carcassi
 */
public interface Point1DDataset {

    /**
     * The values of the points.
     * <p>
     * If the dataset is empty, it returns an empty list.
     *
     * @return the values; never null
     */
    public ListNumber getValues();

    /**
     * The statistical information of the values.
     * <p>
     * If the dataset is empty, or if it contains only NaN values, it returns null.
     *
     * @return statistical information; null if no actual values in the dataset
     */
    public Statistics getStatistics();

    /**
     * The suggested range to display the values.
     *
     * @return the suggested display range
     */
    public Range getDisplayRange();

    /**
     * The number of points in the dataset.
     * <p>
     * This number matches the size of the list returned by {@link #getValues() }.
     *
     * @return the number of values in this dataset
     */
    public int getCount();
}
