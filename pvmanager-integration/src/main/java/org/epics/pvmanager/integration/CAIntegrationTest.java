/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.integration;

import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;

/**
 *
 * @author carcassi
 */
public class CAIntegrationTest {
    public static void main(String[] args) {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        System.out.println("Starting update phase");
        TestPhase phase1 = new UpdateTestPhase();
        phase1.execute();
        
        System.out.println("Starting restart phase");
        TestPhase phase2 = new RestartTestPhase();
        phase2.execute();
        
        System.out.println("Starting outage phase");
        TestPhase phase3 = new OutageTestPhase();
        phase3.execute();
        
        PVManager.getDefaultDataSource().close();
    }
}
