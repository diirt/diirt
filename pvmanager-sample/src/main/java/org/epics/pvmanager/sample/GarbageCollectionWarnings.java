/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.sim.SimulationDataSource;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class GarbageCollectionWarnings {

    public static void main(String[] args) throws InterruptedException {
        PVManager.setDefaultDataSource(new SimulationDataSource());
        
        // Create pv without holding a reference
        PVManager.read(channel("sim://ramp(0,100,1,0.1)")).maxRate(ofMillis(100));

        System.gc();

        Thread.sleep(2000);
        
        PVManager.getDefaultDataSource().close();
    }
}
