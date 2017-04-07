/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class RrdToolDB {
    public static List<TimeSeries> fetchData(Collection<String> signals, Timestamp start, Timestamp end) {
        Map<String, TimeSeriesMulti> cachedFileCFData = new HashMap<>();
        List<TimeSeries> series = new ArrayList<>();
        List<Timestamp> timestamps = null;
        RrdToolReader reader = new RrdToolReader();
        for (String signal : signals) {
            String[] tokens = signal.split(":");
            String cacheKey = tokens[0]+":"+tokens[2];
            TimeSeriesMulti data = cachedFileCFData.get(cacheKey);
            if (data == null) {
                data = reader.readFile(tokens[0], tokens[2], start, end);
                cachedFileCFData.put(cacheKey, data);
            }
            ListDouble buffer = data.getValues().get(tokens[1]);
            if (buffer == null) {
                throw new IllegalArgumentException("Signal " + signal + " was not found");
            }
            series.add(new TimeSeries(data.getTime(), buffer));
        }

        return series;
    }
}
