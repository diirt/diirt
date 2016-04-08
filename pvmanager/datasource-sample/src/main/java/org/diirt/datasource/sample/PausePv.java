/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;


import java.util.List;
import org.diirt.datasource.ExpressionLanguage;
import org.diirt.datasource.PVManager;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.sim.SimulationDataSource;
import java.time.Duration;
import org.diirt.vtype.VNumber;

/**
 * Shows the pausing and resuming of pvs.
 * <p>
 * Pv notifications can be paused and resumed. The connection and caching
 * is not halted but delayed. When the pv is resumed, the current cached
 * values for pending events are immediately processed.
 *
 * @author carcassi
 */
public class PausePv {
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new SimulationDataSource());
        System.out.println("Note that events cannot be stopped while they are in-flight.");
        System.out.println("Therefore you may see one event arriving after the pause or after the close.");

        System.out.println("Starting pv");
        PVReader<List<VNumber>> reader = PVManager.read(ExpressionLanguage.newValuesOf(vNumber("ramp()")))
                .readListener(new PVReaderListener<List<VNumber>>() {
                    @Override
                    public void pvChanged(PVReaderEvent<List<VNumber>> event) {
                        if (event.isValueChanged()) {
                            System.out.println("... values are " + event.getPvReader().getValue());
                        }
                    }
                })
                .maxRate(Duration.ofMillis(50));

    Thread.sleep(2000);
        System.out.println("Pausing for 3 seconds");
        reader.setPaused(true);
        Thread.sleep(3000);

        System.out.println("Unpausing...");
        reader.setPaused(false);
        Thread.sleep(3000);

        System.out.println("Closing...");
        reader.close();
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
