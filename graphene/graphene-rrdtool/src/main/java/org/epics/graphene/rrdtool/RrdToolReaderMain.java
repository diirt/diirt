/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;
import org.epics.util.time.TimestampFormat;

/**
 *
 * @author carcassi
 */
public class RrdToolReaderMain {

    private static TimestampFormat format = new TimestampFormat("yyyyMMddHHmmss");

    public static void main(String[] args) throws Exception {
        List<String> signals = new ArrayList<>();
        Timestamp start = null;
        Timestamp end = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.equals("-s")) {
                i++;
                start = format.parse(args[i]);
            } else if (arg.equals("-e")) {
                i++;
                end = format.parse(args[i]);
            } else  {
                signals.add(arg);
            }
        }
        List<TimeSeries> data = RrdToolDB.fetchData(signals, start, end);
        for (TimeSeries timeSeries : data) {
            displayData(timeSeries);
        }
    }

    public static void displayData(TimeSeriesMulti data) {
        System.out.println("Data contains " + data.getValues().size() + " series");
        System.out.println("Time samples " + data.getTime().size() + " from " + format.format(data.getTime().get(0)) + " to " + format.format(data.getTime().get(data.getTime().size() - 1)));
        for (Map.Entry<String, ListDouble> entry : data.getValues().entrySet()) {
            String name = entry.getKey();
            ListDouble values = entry.getValue();
            System.out.println("Series " + name + " has " + values.size() + " entries");
        }
    }

    public static void displayData(TimeSeries data) {
        System.out.println(data.getTime().size() + " samples from " + format.format(data.getTime().get(0)) + " to " + format.format(data.getTime().get(data.getTime().size() - 1)));
    }
}
