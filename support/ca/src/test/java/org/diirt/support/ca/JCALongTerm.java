/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
public class JCALongTerm {
    public static void main(String[] args) throws Exception {
        JCADataSource jca = new JCADataSourceProvider().createInstance();
        PVManager.setDefaultDataSource(jca);

        List<String> names = new ArrayList<String>();
        for (int i = 0; i <= 20; i++) {
            names.add("counter" + i);
            names.add("counter" + i);
        }
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
                                if (value % 1000 == 0) {
                                    System.out.println(System.currentTimeMillis());
                                }
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
