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
import static org.epics.pvmanager.integration.Constants.*;
import org.epics.vtype.VDouble;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class RepeatedDisconnectTestPhase extends TestPhase {

    @Override
    public final void run() throws Exception {
        int msAfterRestart = 10000;
        
        // Open command writer
        addWriter("command", PVManager.write(channel("command")));
        pause(1000);

        // Reset ioc to known state
        write("command", "start phase1 1");
        pause(10000);
        
        addReader(PVManager.read(channel(const_double)), TimeDuration.ofHertz(50));
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
        write("command", "start phase1 0");
        pause(msAfterRestart);
        
    }

    @Override
    public final void verify(Log log) {
        // Check double
        VDouble disconnected = newVDouble(0.13, newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), newTime(Timestamp.of(631152000, 0), null, false), displayNone());
        log.matchConnections(const_double, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true);
        log.matchValues(const_double, ALL_EXCEPT_TIME,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value,
                disconnected,
                const_double_value);
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new RepeatedDisconnectTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
