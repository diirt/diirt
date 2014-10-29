/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import org.diirt.datasource.PVManager;
import org.diirt.datasource.ca.JCADataSourceBuilder;

/**
 * Tests reconnects caused by a network outage.
 *
 * @author carcassi
 */
public class OutageTestPhase extends DisconnectTestPhase {

    @Override
    public void disconnectCycle() {
        // Pause network
        pauseNetwork(35);
    }

    public static void main(String[] args) {
        PVManager.setDefaultDataSource(new JCADataSourceBuilder().dbePropertySupported(true).build());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new OutageTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }
}
