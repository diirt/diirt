/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sim;

import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import org.epics.pvmanager.ThreadSwitch;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 *
 * @author carcassi
 */
public class SimulationDataSourceTest {

    @Test
    public void ramp1() throws InterruptedException {
        // Read data from a ramp PV
        final AtomicInteger sampleCounter = new AtomicInteger();
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());
        PVManager.setConnectionManager(SimulationDataSource.simulatedData());
        final PV<VDouble> pv = PVManager.read(vDouble("ramp(0,10,1,.125)"))
                .atHz(10);
        PVValueChangeListener listener = new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                // Check that the value is right
                assertTrue("Counter was " + sampleCounter.get() + " and value was " + pv.getValue().getValue().intValue(),
                        sampleCounter.get() == pv.getValue().getValue().intValue() ||
                        sampleCounter.get() == pv.getValue().getValue().intValue() + 11);
                sampleCounter.incrementAndGet();
            }
        };
        pv.addPVValueChangeListener(listener);
        Thread.sleep(2500);
        pv.removePVValueChangeListener(listener);
        // After 10s, expect about 20 samples
        assertTrue(sampleCounter.get() >= 19 && sampleCounter.get() <= 21);
        pv.removePVValueChangeListener(null);
    }

    @Test
    public void ramp2() throws InterruptedException {
        // Read data from a ramp PV
        final AtomicInteger sampleCounter = new AtomicInteger();
        PVManager.setDefaultThread(ThreadSwitch.onTimerThread());
        PVManager.setConnectionManager(SimulationDataSource.simulatedData());
        final PV<VDouble> pv = PVManager.read(vDouble("ramp(0,10,1,0.2)"))
                .atHz(10);
        PVValueChangeListener listener = new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                // Check that the value is right
                assertTrue("Counter was " + sampleCounter.get() + " and value was " + pv.getValue().getValue().intValue(),
                        sampleCounter.get() == pv.getValue().getValue().intValue() ||
                        sampleCounter.get() == pv.getValue().getValue().intValue() + 11);
                sampleCounter.incrementAndGet();
            }
        };
        pv.addPVValueChangeListener(listener);
        Thread.sleep(2000);
        pv.removePVValueChangeListener(listener);
        // After 10s, expect about 10 samples
        assertTrue("Less than 9 calls", sampleCounter.get() >= 9);
        assertTrue("More than 11 calls", sampleCounter.get() <= 11);
    }
}
