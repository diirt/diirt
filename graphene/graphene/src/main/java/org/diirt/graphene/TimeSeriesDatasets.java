/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.stats.StatisticsUtil;
import org.diirt.util.stats.Statistics;
import java.util.List;
import org.diirt.util.array.ListDouble;
import org.diirt.util.array.ListNumber;
import org.diirt.util.time.TimeDuration;
import org.diirt.util.time.TimeInterval;
import org.diirt.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class TimeSeriesDatasets {
    /**
     *Returns a TimeSeriesDataset with the <code>Statistics</code> and time interval.
     * the time interval is the difference in time between the first and second time intervals
     * @param values - List of values associated with timestamps
     * @param timestamps - list of <code>Timestamp</code> 
     * @return TimeSeriesDataset
     */
    public static TimeSeriesDataset timeSeriesOf(final ListNumber values, final List<Timestamp> timestamps) {
        // TODO: make sure timestamps are monotinic
	final TimeInterval timeInterval = TimeInterval.between(timestamps.get(0), timestamps.get(timestamps.size() - 1));
	
        final Statistics stats = StatisticsUtil.statisticsOf(values);
        return new TimeSeriesDataset() {

            @Override
            public ListNumber getValues() {
                return values;
            }

            @Override
            public List<Timestamp> getTimestamps() {
                return timestamps;
            }

            @Override
            public ListNumber getNormalizedTime( TimeInterval normalizationRange ) {
		
		//the data is normalized over the time interval of the data
		//but then it needs to be normalized again over the time interval
		//of the plot.
		//thus, we use this scale to convert values normalized over the
		//data's time interval to the plot's time interval
		final double scale = (double)(timeInterval.getEnd().durationFrom( timeInterval.getStart() ).toNanosLong())/(normalizationRange.getEnd().durationFrom( normalizationRange.getStart() ).toNanosLong() );
                return new ListDouble() {

                    @Override
                    public double getDouble(int index) {
                        return TimeScales.normalize(timestamps.get(index), timeInterval) * scale;
                    }

                    @Override
                    public int size() {
                        return timestamps.size();
                    }
                };
            }

            @Override
            public Statistics getStatistics() {
                return stats;
            }

            @Override
            public TimeInterval getTimeInterval() {
                return timeInterval;
            }

            @Override
            public int getCount() {
                return values.size();
            }
        };
    }
}
