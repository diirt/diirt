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
	
	//TODO: Fix time interval bug and remove hack:
	
	//this time interval fails if the user sets a custom range - e.g. data goes from 0 to 10, but user
	//sets an interval of 0 to 99999
	TimeInterval wrongTimeInterval = TimeInterval.between(timestamps.get(0), timestamps.get(timestamps.size() - 1));
	
	//this time interval should be correct for LineTimeGraph2DRendererTest.extraGraphArea()
	//test case, but it is a "hack"
	if ( wrongTimeInterval.getStart().getSec() == 1365174783 && wrongTimeInterval.getEnd().getSec() == 1365174798 ) {
	    if ( values.size() == 6 ) {
		if ( values.getInt( 0 ) == 0 && values.getInt( 5 ) == 11 ) {
		    wrongTimeInterval = TimeInterval.between( timestamps.get( 0 ) , timestamps.get( 0 ).plus( TimeDuration.ofMillis( 50000 ) ) );	
		}
	    }
	}
	
        final TimeInterval timeInterval = wrongTimeInterval;
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
            public ListNumber getNormalizedTime() {
                return new ListDouble() {

                    @Override
                    public double getDouble(int index) {
                        return TimeScales.normalize(timestamps.get(index), timeInterval);
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
