/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.vtype.ExpressionLanguage;
import static org.diirt.util.time.TimeDuration.*;
import org.diirt.vtype.VType;

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
            }).maxRate(ofHertz(50));
            readers.add(reader);
        }

        Thread.sleep(60000);

        for (PVReader<VType> pVReader : readers) {
            pVReader.close();
        }
    }
}
