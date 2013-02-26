/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReaderListener;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 * @author msekoranja
 */
public class PVASteadyLongTerm {
    public static void main(String[] args) throws Exception {
        PVADataSource pva = new PVADataSource();
        PVManager.setDefaultDataSource(pva);
        final AtomicInteger count = new AtomicInteger();
        
        PVReader<?> pv = PVManager.read(channel("testCounter")).maxRate(ofHertz(50));
        pv.addPVReaderListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
            	System.out.println(event.getPvReader().getValue());
                count.incrementAndGet();
            }
        });
        /*
        pv = PVManager.read(channel("testCounter")).maxRate(ofHertz(50));
        pv.addPVReaderListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
                count.incrementAndGet();
            }
        });
        */
        while (true) {
            Thread.sleep(1000);
        }
    }
}
