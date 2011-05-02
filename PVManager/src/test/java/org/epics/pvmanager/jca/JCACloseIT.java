/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVValueChangeListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;

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
        final PV<Object> pv =PVManager.read(channel("carcassi"))
                .atHz(10);
        pv.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
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
        final PV<Object> pv1 = PVManager.read(channel("carcassi"))
                .atHz(10);
        pv1.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                counter.incrementAndGet();
            }
        });
        final PV<Object> pv2 = PVManager.read(channel("carcassi"))
                .atHz(10);
        pv2.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                counter.incrementAndGet();
            }
        });
        final PV<Object> pv3 = PVManager.read(channel("carcassi"))
                .atHz(10);
        pv3.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                counter.incrementAndGet();
            }
        });
        final PV<Object> pv4 = PVManager.read(channel("carcassi"))
                .atHz(10);
        pv4.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
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
        final PV<Object> pv1 = PVManager.read(channel("carcassi"))
                .atHz(10);
        pv1.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                counter.incrementAndGet();
            }
        });
        final PV<Object> pv2 = PVManager.read(channel("carcassi2"))
                .atHz(10);
        pv2.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                counter.incrementAndGet();
            }
        });
        final PV<Object> pv3 = PVManager.read(channel("carcassi"))
                .atHz(10);
        pv3.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
                counter.incrementAndGet();
            }
        });
        final PV<Object> pv4 = PVManager.read(channel("carcassi2"))
                .atHz(10);
        pv4.addPVValueChangeListener(new PVValueChangeListener() {

            @Override
            public void pvValueChanged() {
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
