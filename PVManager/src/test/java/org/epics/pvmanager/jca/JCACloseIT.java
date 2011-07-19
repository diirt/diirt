/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class JCACloseIT extends JCABase {
    
    AtomicInteger counter = new AtomicInteger();
    
    public JCACloseIT() {
    }

    @Test
    public void testSingleOpenAndClose() throws Exception{
        final PVReader<Object> pv =PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        
        Thread.sleep(1000);
        
        assertJCAOn();
        
        pv.close();

        Thread.sleep(1500);
        
        assertJCAOff();
    }

    @Test
    public void testMultipleOpenAndClose() throws Exception{
        final PVReader<Object> pv1 = PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv1.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        final PVReader<Object> pv2 = PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv2.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        final PVReader<Object> pv3 = PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv3.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        final PVReader<Object> pv4 = PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv4.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        
        Thread.sleep(1000);
        
        assertJCAOn();
        
        pv1.close();
        pv2.close();
        pv3.close();
        pv4.close();

        Thread.sleep(1500);
        
        assertJCAOff();
    }

    @Test
    public void testMultipleDifferentOpenAndClose() throws Exception{
        final PVReader<Object> pv1 = PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv1.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        final PVReader<Object> pv2 = PVManager.read(channel("carcassi2"))
                .every(hz(10));
        pv2.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        final PVReader<Object> pv3 = PVManager.read(channel("carcassi"))
                .every(hz(10));
        pv3.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        final PVReader<Object> pv4 = PVManager.read(channel("carcassi2"))
                .every(hz(10));
        pv4.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                counter.incrementAndGet();
            }
        });
        
        Thread.sleep(1000);
        
        assertJCAOn();
        
        pv1.close();
        pv2.close();
        pv3.close();
        pv4.close();

        Thread.sleep(1500);
        
        assertJCAOff();
    }
}
