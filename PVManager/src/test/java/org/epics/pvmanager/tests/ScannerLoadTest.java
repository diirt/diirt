/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.tests;

import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.loc.LocalDataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.util.time.TimeDuration;

/**
 * Load test to see the effect on the performance of the scanning.
 * <p>
 * The test opens a number of local pvs. Since there is no load associated
 * on keeping the local pv open, the total cpu load can be assumed to be
 * the scanning. If can also be used to optimize memory consumption.
 * <p>
 * 2012/11/12 - 1,000 pvs, 1.8%; 10,000 pvs, 18.9%; 100,000 pvs, 34%. GC time ~0%
 *
 * @author carcassi
 */
public class ScannerLoadTest {
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new LocalDataSource());
        
        List<PV<Object, Object>> pvs = new ArrayList<>();
        int nPvs = 100000;
        for (int i = 0; i < nPvs; i++) {
            PV<Object, Object> pv = PVManager.readAndWrite(channel("channel " + i)).asynchWriteAndMaxReadRate(TimeDuration.ofHertz(50));
            pvs.add(pv);
            
        }
        System.out.println("Started");
        
        Thread.sleep(60000);
        
        for (PV<Object, Object> pv : pvs) {
            pv.close();
        }
        
    }
}
