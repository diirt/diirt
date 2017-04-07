/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;


import org.diirt.datasource.PVManager;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.sim.SimulationDataSource;
import java.time.Duration;

/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

/**
 *
 * @author carcassi
 */
public class VerifyCloseChannelPVManager {
    public static void main(String[] args) throws Exception {
        //System.setProperty("com.cosylab.epics.caj.CAJContext.max_array_bytes", "20000000");
        PVManager.setDefaultDataSource(new SimulationDataSource());
        final PVReader<Object> reader = PVManager.read(channel("gaussian()"))
                .readListener(new PVReaderListener<Object>() {
                    @Override
                    public void pvChanged(PVReaderEvent<Object> event) {
                        System.out.println("Monitor called");
                    }
                })
                .maxRate(Duration.ofMillis(10));

    Thread.sleep(10000);
        reader.close();
        System.out.println("After five seconds");

        Thread.sleep(10000);
        System.out.println("After another five seconds");
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
