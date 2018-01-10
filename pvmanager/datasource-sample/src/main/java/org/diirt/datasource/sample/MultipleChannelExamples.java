/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.util.HashMap;
import java.util.Map;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriter;
import static java.time.Duration.*;

/**
 * This is the code from the examples in the docs, to make sure it
 * actually compiles
 *
 * @author carcassi
 */
public class MultipleChannelExamples {

    public void m1_readMultipleChannels() {
        // Read a map with the channels named "one", "two" and "three"
        PVReader<Map<String, Object>> pvReader = PVManager
            .read(mapOf(latestValueOf(channels("one", "two", "three"))))
            .readListener(new PVReaderListener<Map<String, Object>>() {
                @Override
                public void pvChanged(PVReaderEvent<Map<String, Object>> event) {
                    // Print the values if any
                    Map<String, Object> map = event.getPvReader().getValue();
                    if (map != null) {
                        System.out.println("one: " + map.get("one") +
                                " - two: " + map.get("two") +
                                " - three: " + map.get("three"));
                    }
                }
            })
            .maxRate(ofMillis(100));

        // Remember to close
        pvReader.close();

        // Note that when using a composite datasource, the channels can be
        //from different sources (e.g. "sim://noise" and "ca://mypv").
    }

    public void m2_readMultipleChannels() {
        // Write a map to the channels named "one", "two" and "three"
        PVWriter<Map<String, Object>> pvWriter = PVManager
                .write(mapOf(channels("one", "two", "three")))
                .async();

        // Prepare the 3 values
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("one", 1.0);
        values.put("two", 2.0);
        values.put("three", "run");

        // Write
        pvWriter.write(values);

        // Remember to close
        pvWriter.close();

        // Note that when using a composite datasource, the channels can be
        //from different sources (e.g. "sim://noise" and "ca://mypv").
    }

    public void m3_readWriteMultipleChannels() {
        // Read and write a map to the channels named "one", "two" and "three"
        PV<Map<String, Object>, Map<String, Object>> pv = PVManager
            .readAndWrite(mapOf(latestValueOf(channels("one", "two", "three"))))
            .asynchWriteAndMaxReadRate(ofMillis(100));

        // Do something
        // ...

        // Remember to close
        pv.close();
    }

    public void m4_renameChannels() {
        // Read a map with the channels "one", "two" and "three"
        // reffered in the map as "setpoint", "readback" and "difference"
        PVReader<Map<String, Object>> pvReader = PVManager
            .read(mapOf(latestValueOf(channel("one").as("setpoint")
                         .and(channel("two").as("readback"))
                         .and(channel("three").as("difference")))))
            .readListener(new PVReaderListener<Map<String, Object>>() {
                @Override
                public void pvChanged(PVReaderEvent<Map<String, Object>> event) {
                    // Print the values if any
                    Map<String, Object> map = event.getPvReader().getValue();
                    if (map != null) {
                        System.out.println("setpoint: " + map.get("setpoint") +
                                " - readback: " + map.get("readback") +
                                " - difference: " + map.get("difference"));
                    }
                }
            })
            .maxRate(ofMillis(100));

        // Remember to close
        pvReader.close();

        // Any expression however created can be renamed.
    }

    public void m5_writeOrdering() {
        // Write a map to the channels named "one", "two" and "three"
        // Write "two" after "one" and write "three" after "two"
        PVWriter<Map<String, Object>> pvWriter = PVManager.write(
                mapOf(channel("one")
                      .and(channel("two").after("one"))
                      .and(channel("three").after("two")))).async();

        // Do something
        // ...

        // Remember to close
        pvWriter.close();
    }
}
