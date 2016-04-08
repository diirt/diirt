/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.log;


import org.diirt.datasource.PVManager;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import java.time.Duration;
import org.diirt.vtype.VNumber;

/**
 * Shows the automatic closing of garbage collected pvs.
 * <p>
 * Pvs must be properly closed. As a safety mechanics, pvmanager will automatically
 * close garbage collected pvs, and log a warning. This example shows
 * the mechanism in action.
 *
 * @author carcassi
 */
public class UnclosedPvs {
    public static void main(String[] args) throws Exception {
        System.out.println("Starting pv");
        PVReader<VNumber> reader = PVManager.read(vNumber("sim://gaussianNoise()"))
                .readListener(new PVReaderListener<VNumber>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VNumber> event) {
                        if (event.isValueChanged()) {
                            System.out.println("... value is " + event.getPvReader().getValue().getValue());
                        }
                    }
                })
                .maxRate(Duration.ofMillis(500));

    Thread.sleep(2000);
        System.out.println("Voiding reference");
        reader = null;
        Thread.sleep(100);
        System.out.println("Garbage collecting");
        System.gc();
        Thread.sleep(3000);

        System.out.println("Closing...");
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
