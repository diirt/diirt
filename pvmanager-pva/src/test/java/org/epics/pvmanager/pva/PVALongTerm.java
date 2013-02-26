/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.pva;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class PVALongTerm {
    public static void main(String[] args) throws Exception {
        PVADataSource pva = new PVADataSource();
        PVManager.setDefaultDataSource(pva);
        
        List<String> names = new ArrayList<String>();
        names.add("testCounter");
        
        List<PVReader<?>> pvs = new ArrayList<PVReader<?>>(); 
        for (String name : names) {
            pvs.add(null);
        }
        Random rand = new Random(1);
        final AtomicInteger count = new AtomicInteger(-1);
        
        while (true) {
            int index = rand.nextInt(names.size());
            PVReader<?> pv = pvs.get(index);
            if (pv == null) {
                pv = PVManager.read(channel(names.get(index)))
                        .readListener(new PVReaderListener<Object>() {
                            @Override
                            public void pvChanged(PVReaderEvent<Object> event) {
                                int value = count.incrementAndGet();
                                //if (value % 1000 == 0) {
                                    System.out.println(System.currentTimeMillis());
                                //}
                            }
                        })
                        .maxRate(ofHertz(rand.nextInt(20) + 1));
                pvs.set(index, pv);
            } else {
                pv.close();
                pvs.set(index, null);
            }
            
            Thread.sleep(1000);
        }
    }
}
