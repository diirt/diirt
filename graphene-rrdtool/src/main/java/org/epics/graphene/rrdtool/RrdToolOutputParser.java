/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.epics.util.array.CircularBufferDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class RrdToolOutputParser {
    
    public TimeSeries parse(BufferedReader reader) {
        try {
            // First line has the data name
            String name = reader.readLine().trim();
            
            // Second line is blank
            reader.readLine();
            
            String line;
            List<Timestamp> time = new ArrayList<>();
            CircularBufferDouble values = new CircularBufferDouble(1000000);
            while ((line = reader.readLine()) != null) {
                int colon = line.indexOf(':');
                Timestamp timestamp = Timestamp.of(Integer.parseInt(line.substring(0, colon)), 0);
                String valueText = line.substring(colon+1).trim();
                Double value;
                if ("nan".equals(valueText) || "-nan".equals(valueText)) {
                    value = Double.NaN;
                } else {
                    value = Double.parseDouble(valueText);
                }
                time.add(timestamp);
                values.addDouble(value);
            }
            
            return new TimeSeries(time, values);
        } catch(IOException ex) {
            throw new RuntimeException("Couldn't parse file", ex);
        }
    }
}
