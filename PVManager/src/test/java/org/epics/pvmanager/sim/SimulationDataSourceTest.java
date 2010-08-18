/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import org.epics.pvmanager.ThreadSwitch;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.data.DataUtils;
import org.epics.pvmanager.data.VMultiDouble;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 *
 * @author carcassi
 */
public class SimulationDataSourceTest {

    //@Test
    public void ramp1() throws InterruptedException {
        // Read data from a ramp PV
        final AtomicInteger sampleCounter = new AtomicInteger();
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());
        PVManager.setConnectionManager(SimulationDataSource.simulatedData());
        final PV<VDouble> pv = PVManager.read(vDouble("ramp(0,10,1,.05)"))
                .atHz(200);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
//                // Check that the value is right
                assertTrue("Counter was " + sampleCounter.get() + " and value was " + pv.getValue().getValue().intValue(),
                        sampleCounter.get() == pv.getValue().getValue().intValue() ||
                        sampleCounter.get() == pv.getValue().getValue().intValue() + 11 ||
                        sampleCounter.get() == pv.getValue().getValue().intValue() + 22);
                sampleCounter.incrementAndGet();
            }
        });
        Thread.sleep(1000);
        pv.close();
        // After 10s, expect about 20 samples
        assertTrue("Less than 19 calls", sampleCounter.get() >= 19);
        assertTrue("More than 21 calls", sampleCounter.get() <= 21);
        pv.removePVValueChangeListener(null);
    }

    //@Test
    public void ramp2() throws InterruptedException {
        // Read data from a ramp PV
        final AtomicInteger sampleCounter = new AtomicInteger();
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());
        PVManager.setConnectionManager(SimulationDataSource.simulatedData());
        final PV<VDouble> pv = PVManager.read(vDouble("ramp(0,10,1,0.2)"))
                .atHz(50);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                // Check that the value is right
                assertTrue("Counter was " + sampleCounter.get() + " and value was " + pv.getValue().getValue().intValue(),
                        sampleCounter.get() == pv.getValue().getValue().intValue() ||
                        sampleCounter.get() == pv.getValue().getValue().intValue() + 11);
                sampleCounter.incrementAndGet();
            }
        });
        Thread.sleep(2000);
        pv.close();
        // After 10s, expect about 10 samples
        assertTrue("Less than 9 calls", sampleCounter.get() >= 9);
        assertTrue("More than 11 calls", sampleCounter.get() <= 11);
    }

    @Test
    public void synchRamp() throws InterruptedException {
        // Read data from a ramp PV
        final AtomicInteger sampleCounter = new AtomicInteger();
        final AtomicInteger failedComparisons = new AtomicInteger();
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());
        PVManager.setConnectionManager(SimulationDataSource.simulatedData());
        // Data generation every 100 ms
        // Tolerance 200 ms
        // Cache last 5 samples
        final PV<VMultiDouble> pv = PVManager.read(synchronizedArrayOf(TimeDuration.ms(10), TimeDuration.ms(250), vDoubles(Collections.nCopies(100, "ramp(0,10,1,0.05)"))))
                .atHz(10);
        Thread.sleep(300);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                VMultiDouble array = pv.getValue();
                if (array == null)
                    return;
                // Check that all values are the same
                Double refValue = array.getValues().get(0).getValue();
                for (VDouble value : array.getValues()) {
                    if (value == null || !refValue.equals(value.getValue())) {
                        if (value != null) {
                            System.out.println(value.getValue() + " " + refValue);
                            System.out.println(value.getTimeStamp() + " " + array.getValues().get(0).getTimeStamp());
                        }
                        failedComparisons.incrementAndGet();
                    }
                }
                sampleCounter.incrementAndGet();
            }
        });
        Thread.sleep(1000);
        pv.close();
        // After 10s, expect about 10 samples
        assertTrue("There were " + failedComparisons.get() + " failed comparisons", failedComparisons.get() < 20);
        assertTrue("Less than 9 calls", sampleCounter.get() >= 9);
        assertTrue("More than 11 calls", sampleCounter.get() <= 11);
    }
}
