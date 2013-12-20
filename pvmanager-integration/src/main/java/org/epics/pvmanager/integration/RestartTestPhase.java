/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.LogManager;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSourceBuilder;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class RestartTestPhase extends DisconnectTestPhase {

    @Override
    public void disconnectCycle() {
        // Send restart command
        restart("phase1");
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSourceBuilder().dbePropertySupported(true).build());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new RestartTestPhase();
        phase1.execute();
        Thread.sleep(100);
        PVManager.getDefaultDataSource().close();
    }

}
