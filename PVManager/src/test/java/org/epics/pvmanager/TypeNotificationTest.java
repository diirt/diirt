/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import java.util.concurrent.atomic.AtomicInteger;

import org.epics.pvmanager.data.DataTypeSupport;
import org.epics.pvmanager.data.VDouble;
import org.epics.pvmanager.data.VInt;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class TypeNotificationTest {

    public TypeNotificationTest() {
    }

    @BeforeClass
    public static void modifyDefaultSource() {
        DataTypeSupport.install();
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
    }

    @Test
    public void exceptionInFunction() throws Exception {
        final PVReader<VDouble> pv = PVManager.read(vDouble("gaussian()")).every(hz(10));
        final AtomicInteger noTypeCounter = new AtomicInteger();
        final AtomicInteger doubleCounter = new AtomicInteger();
        final AtomicInteger intCounter = new AtomicInteger();

        PVReaderListener noTypeListener = new PVReaderListener() {

            @Override
            public void pvChanged() {
                noTypeCounter.incrementAndGet();
            }
        };

        PVReaderListener doubleListener = new PVReaderListener() {

            @Override
            public void pvChanged() {
                doubleCounter.incrementAndGet();
            }
        };

        PVReaderListener intListener = new PVReaderListener() {

            @Override
            public void pvChanged() {
                intCounter.incrementAndGet();
            }
        };

        pv.addPVReaderListener(noTypeListener);
        pv.addPVReaderListener(VDouble.class, doubleListener);
        pv.addPVReaderListener(VInt.class, intListener);

        Thread.sleep(2000);

        assertThat(intCounter.get(), equalTo(0));
        assertThat(doubleCounter.get(), not(equalTo(0)));
        assertThat(noTypeCounter.get(), not(equalTo(0)));

        // Removing the listener to check whether the notifications stop
        pv.removePVReaderListener(noTypeListener);
        pv.removePVReaderListener(doubleListener);
        int tempCount = noTypeCounter.get();
        int tempCount2 = doubleCounter.get();

        Thread.sleep(2000);

        assertThat(noTypeCounter.get(), equalTo(tempCount));
        assertThat(doubleCounter.get(), equalTo(tempCount2));

        pv.close();
    }

}