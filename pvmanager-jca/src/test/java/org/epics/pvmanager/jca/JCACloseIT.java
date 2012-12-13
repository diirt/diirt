/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jca;

import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderListener;
import org.junit.Test;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import static org.epics.util.time.TimeDuration.*;

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
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        
        Thread.sleep(1000);
        
        assertJCAOn();
        
        pv.close();

        Thread.sleep(1500);
        
        assertJCAOff();
    }

    @Test
    public void testMultipleOpenAndClose() throws Exception{
        final PVReader<Object> pv1 = PVManager.read(channel("carcassi"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        final PVReader<Object> pv2 = PVManager.read(channel("carcassi"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        final PVReader<Object> pv3 = PVManager.read(channel("carcassi"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        final PVReader<Object> pv4 = PVManager.read(channel("carcassi"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        
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
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        final PVReader<Object> pv2 = PVManager.read(channel("carcassi2"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        final PVReader<Object> pv3 = PVManager.read(channel("carcassi"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        final PVReader<Object> pv4 = PVManager.read(channel("carcassi2"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        counter.incrementAndGet();
                    }
                })
                .maxRate(ofHertz(10));
        
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
