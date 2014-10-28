/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;


import org.diirt.datasource.PVManager;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.sim.SimulationDataSource;
import org.diirt.util.time.TimeDuration;
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
        PVManager.setDefaultDataSource(new SimulationDataSource());
        
        System.out.println("Starting pv");
        PVReader<VNumber> reader = PVManager.read(vNumber("gaussianNoise()"))
                .readListener(new PVReaderListener<VNumber>() {
                    @Override
                    public void pvChanged(PVReaderEvent<VNumber> event) {
                        if (event.isValueChanged()) {
                            System.out.println("... value is " + event.getPvReader().getValue().getValue());
                        }
                    }
                })
                .maxRate(TimeDuration.ofMillis(500));
	
	Thread.sleep(2000);
        System.out.println("Garbage collecting");
        reader = null;
        System.gc();
	Thread.sleep(3000);
	
        System.out.println("Closing...");
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
