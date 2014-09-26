/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;


import java.util.List;
import org.epics.pvmanager.ExpressionLanguage;
import org.epics.pvmanager.PVManager;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.util.time.TimeDuration;
import org.epics.vtype.VNumber;

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
                .maxRate(TimeDuration.ofMillis(50));
	
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
