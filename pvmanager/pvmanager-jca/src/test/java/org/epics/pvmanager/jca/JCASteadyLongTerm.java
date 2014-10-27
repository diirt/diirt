/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.jca;

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
 */
public class JCASteadyLongTerm {
    public static void main(String[] args) throws Exception {
        JCADataSource jca = new JCADataSource();
        PVManager.setDefaultDataSource(jca);
        final AtomicInteger count = new AtomicInteger();
        
        PVReader<?> pv = PVManager.read(channel("counter1")).maxRate(ofHertz(50));
        pv.addPVReaderListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
                count.incrementAndGet();
            }
        });
        
        pv = PVManager.read(channel("counter1")).maxRate(ofHertz(50));
        pv.addPVReaderListener(new PVReaderListener<Object>() {

            @Override
            public void pvChanged(PVReaderEvent<Object> event) {
                count.incrementAndGet();
            }
        });
        
        while (true) {
            Thread.sleep(1000);
        }
    }
}
