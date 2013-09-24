/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.vtype.ExpressionLanguage;
import org.epics.util.time.TimeDuration;
import org.epics.vtype.VType;

/**
 *
 * @author carcassi
 */
public class MultiplePVLoadTest {

    private static List<PVReader<VType>> readers = new ArrayList<>();
    private static final AtomicInteger counter = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        int nPvs = 5000;

        SetupUtil.defaultCASetup();

        for (int i = 0; i < nPvs; i++) {
            PVReader<VType> reader = PVManager.read(ExpressionLanguage.vType("sim://noise"))
                    .readListener(new PVReaderListener<VType>() {
                @Override
                public void pvChanged(PVReaderEvent<VType> event) {
                    counter.incrementAndGet();
                }
            }).maxRate(TimeDuration.ofHertz(50));
            readers.add(reader);
        }

        Thread.sleep(60000);

        for (PVReader<VType> pVReader : readers) {
            pVReader.close();
        }
    }
}
