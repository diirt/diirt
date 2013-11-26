/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.pvmanager.integration;

import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVManager;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import static org.epics.pvmanager.integration.VTypeMatchMask.*;
import static org.epics.vtype.ValueFactory.*;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class RestartTestPhase extends TestPhase {

    @Override
    public void run() throws Exception {
        // Open command writer
        addWriter("command", PVManager.write(channel("command")));
        Thread.sleep(1000);

        // Reset ioc to known state
        write("command", "start phase1 1");
        Thread.sleep(10000);
        
        // Add all constant fields
        // TODO: missing float, int, short, byte, string and all arrays
        addReader(PVManager.read(channel("const-double")), TimeDuration.ofHertz(50));

        // Send restart command and wait enough time
        write("command", "start phase1 1");
        Thread.sleep(10000);
    }

    @Override
    public void verify(Log log) {
        log.matchConnections("const-double", true, false, true);
        log.matchValues("const-double", ALL,
                newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                newVDouble(0.13, newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()));
        log.matchErrors("const-double");
    }

}
