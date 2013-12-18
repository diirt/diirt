/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVManager;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import static org.epics.pvmanager.integration.VTypeMatchMask.*;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.vtype.ValueFactory.*;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class TypeChangeTestPhase extends TestPhase {

    @Override
    public final void run() throws Exception {
        // Open command writer
        addWriter("command", PVManager.write(channel("command")));
        Thread.sleep(1000);

        // Reset ioc to known state
        write("command", "start typeChange1 1");
        Thread.sleep(10000);
        
        addReader(PVManager.read(channel("double-to-i32")), TimeDuration.ofHertz(50));
        //addReader(PVManager.read(channel("i32-to-double")), TimeDuration.ofHertz(50));
        pause(1000);
        
        // Change ioc and types
        write("command", "start typeChange2 1");
        pause(10000);
    }

    @Override
    public final void verify(Log log) {
        // Check double
        log.matchConnections("double-to-i32", true, false, true);
        log.matchValues("double-to-i32", ALL_EXCEPT_TIME,
                newVDouble(0.0, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                newVDouble(0.0, newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                newVInt(0, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()));
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new TypeChangeTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
