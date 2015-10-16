/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.epics.util.array.CircularBufferDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class TimeSeriesMulti {

    private List<Timestamp> time;
    private Map<String, ListDouble> values;

    public TimeSeriesMulti(List<Timestamp> time, Map<String, ListDouble>  values) {
        this.time = time;
        this.values = values;
    }

    public List<Timestamp> getTime() {
        return time;
    }

    public Map<String, ListDouble> getValues() {
        return values;
    }

    public static TimeSeriesMulti synchronizeSeries(Map<String, TimeSeries> seriesMap) {
        int[] offsets = new int[seriesMap.size()];
        Timestamp[] timestamps = new Timestamp[seriesMap.size()];
        boolean done = false;

        List<Timestamp> finalTimes = new ArrayList<>();
        Map<String, ListDouble> data = new HashMap<>();
        List<CircularBufferDouble> buffers = new ArrayList<>();
        List<TimeSeries> series = new ArrayList<>();
        for (Map.Entry<String, TimeSeries> entry : seriesMap.entrySet()) {
            String name = entry.getKey();
            TimeSeries timeSeries = entry.getValue();
            CircularBufferDouble buffer = new CircularBufferDouble(1000000);
            data.put(name, buffer);
            buffers.add(buffer);
            series.add(timeSeries);
        }

        while (!done) {
            // The next time is going to be the maximum between all the
            // next time, because we want all values to change
            for (int i = 0; i < timestamps.length; i++) {
                timestamps[i] = series.get(i).getTime().get(offsets[i]);
            }
            Timestamp nextTime = Collections.max(Arrays.asList(timestamps));

            // Advance all the offsets to a time that is nextTime or later
            for (int i = 0; i < offsets.length; i++) {
                List<Timestamp> times = series.get(i).getTime();
                while((offsets[i] != times.size() - 1) && (times.get(offsets[i]).compareTo(nextTime) < 0)) {
                    offsets[i]++;
                }
            }

            // Add next values and increment
            finalTimes.add(nextTime);
            boolean hasIncremented = false;
            for (int i = 0; i < offsets.length; i++) {
                buffers.get(i).addDouble(series.get(i).getValues().getDouble(offsets[i]));
                if (offsets[i] < series.get(i).getValues().size() - 1) {
                    hasIncremented = true;
                    offsets[i]++;
                }
            }

            // If no increment, we are done
            if (!hasIncremented) {
                done = true;
            }
        }
        return new TimeSeriesMulti(finalTimes, data);
    }
}
