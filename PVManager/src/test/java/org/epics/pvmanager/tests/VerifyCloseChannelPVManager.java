package org.epics.pvmanager.tests;


import gov.aps.jca.*;
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.PVReaderListener;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.pvmanager.util.TimeDuration;

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
        final PVReader<Object> reader = PVManager.read(channel("gaussian()")).every(TimeDuration.ms(10));
        reader.addPVReaderListener(new PVReaderListener() {

            @Override
            public void pvChanged() {
                System.out.println("Monitor called");
            }
        });
	
	Thread.sleep(10000);
        reader.close();
        System.out.println("After five seconds");
	
	Thread.sleep(10000);
        System.out.println("After another five seconds");
        PVManager.getDefaultDataSource().close();
        System.out.println("Done");
    }
}
