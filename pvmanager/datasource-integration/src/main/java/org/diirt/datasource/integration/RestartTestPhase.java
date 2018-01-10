/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import org.diirt.datasource.PVManager;
import org.diirt.support.ca.JCADataSourceConfiguration;

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
        PVManager.setDefaultDataSource(new JCADataSourceConfiguration().dbePropertySupported(true).create());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new RestartTestPhase();
        phase1.execute();
        Thread.sleep(100);
        PVManager.getDefaultDataSource().close();
    }

}
