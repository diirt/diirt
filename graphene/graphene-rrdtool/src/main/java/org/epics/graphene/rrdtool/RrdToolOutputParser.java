/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.rrdtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.epics.util.array.CircularBufferDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListNumber;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class RrdToolOutputParser {

    public TimeSeriesMulti parse(BufferedReader reader) {
        try {
            // First line has the data name
            List<String> names = Arrays.asList(reader.readLine().trim().split("(\\s+)"));

            // Second line is blank
            reader.readLine();

            String line;
            List<Timestamp> time = new ArrayList<>();
            List<CircularBufferDouble> buffers = new ArrayList<>(names.size());
            for (int i = 0; i < names.size(); i++) {
                buffers.add(new CircularBufferDouble(1000000));
            }
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("(\\s|[:])+");
                Timestamp timestamp = Timestamp.of(Integer.parseInt(tokens[0]), 0);
                time.add(timestamp);
                for (int i = 0; i < buffers.size(); i++) {
                    String valueText = tokens[i + 1];
                    Double value;
                    if ("nan".equals(valueText) || "-nan".equals(valueText)) {
                        value = Double.NaN;
                    } else {
                        value = Double.parseDouble(valueText);
                    }
                    buffers.get(i).addDouble(value);
                }
            }
            Map<String, ListDouble> values = new HashMap<>();
            for (int i = 0; i < names.size(); i++) {
                values.put(names.get(i), buffers.get(i));
            }

            return new TimeSeriesMulti(time, values);
        } catch(IOException ex) {
            throw new RuntimeException("Couldn't parse file", ex);
        }
    }
}
