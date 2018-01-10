/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.formula;

import org.diirt.datasource.ExpressionLanguage;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVWriter;
import java.time.Duration;

/**
 * Indirect channel use through name passed as String.
 * <p>
 * This examples shows the opening and closing of channels based on the name
 * contained in another channel. For C programmers: it's a pointer.
 *
 * @author carcassi
 */
public class PvFunction {

    public static void main(String[] args) throws Exception {
        String indirectPv = "=pv('loc://name')";
        String pvNamePv = "loc://name";
        System.out.println("Starting channel " + indirectPv);
        PVReader<?> reader = PVManager.read(org.diirt.datasource.formula.ExpressionLanguage.formula(indirectPv))
                .readListener((PVReaderEvent<Object> event) -> {
                    System.out.println(event + " - connected(" + event.getPvReader().isConnected() + ")");
                })
                .maxRate(Duration.ofMillis(50));

        Thread.sleep(3000);

        PVWriter<Object> namePv = PVManager.write(ExpressionLanguage.channel(pvNamePv))
                .async();
        System.out.println("Write sim://noise to " + pvNamePv);
        namePv.write("sim://noise");

        Thread.sleep(3000);
        System.out.println("Write sim://asdf to " + pvNamePv);
        namePv.write("sim://asdf");

        Thread.sleep(3000);
        System.out.println("Write sim://noise to " + pvNamePv);
        namePv.write("sim://noise");

        Thread.sleep(3000);
        System.out.println("Closing...");
        reader.close();
        namePv.close();
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
