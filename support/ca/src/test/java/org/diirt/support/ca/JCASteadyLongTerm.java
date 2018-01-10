/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import java.util.concurrent.atomic.AtomicInteger;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReaderListener;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PVReaderEvent;
import static org.diirt.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class JCASteadyLongTerm {
    public static void main(String[] args) throws Exception {
        JCADataSource jca = new JCADataSourceProvider().createInstance();
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
