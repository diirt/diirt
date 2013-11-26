/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.pvmanager.integration;

import org.epics.pvmanager.integration.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVManager;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import static org.epics.pvmanager.integration.VTypeMatchMask.*;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.vtype.ValueFactory.*;

/**
 * Tests reconnects caused by a network outage.
 *
 * @author carcassi
 */
public class OutageTestPhase extends DisconnectTestPhase {

    @Override
    public void disconnectCycle() {
        // Send restart command and wait enough time
        write("command", "netpause 35");
        pause(40000);
    }

    public static void main(String[] args) {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new OutageTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }
}
