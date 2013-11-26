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
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.vtype.VDouble;
import org.epics.vtype.VString;
import static org.epics.vtype.ValueFactory.*;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class RestartTestPhase extends TestPhase {
    
    private static final String const_double = "const-double";
    private static final VDouble const_double_value = newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone());
    private static final String const_string = "const-double.NAME";
    private static final VString const_string_value = newVString("const-double", newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false));

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
        addReader(PVManager.read(channel(const_double)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_string)), TimeDuration.ofHertz(50));

        // Send restart command and wait enough time
        write("command", "start phase1 1");
        Thread.sleep(10000);
    }

    @Override
    public void verify(Log log) {
        // *** const_double ***
        // Connection should go up, down and back up 
        log.matchConnections(const_double, true, false, true);
        // Value should remain the same, but change alarm
        log.matchValues(const_double, ALL,
                const_double_value,
                newVDouble(const_double_value.getValue(), newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), const_double_value, const_double_value),
                const_double_value);
        // No errors
        log.matchErrors(const_double);
        
        // *** const_string ***
        // Connection should go up, down and back up 
        log.matchConnections(const_string, true, false, true);
        // Value should remain the same, but change alarm
        log.matchValues(const_string, ALL,
                const_string_value,
                newVString(const_string_value.getValue(), newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), const_string_value),
                const_string_value);
        // No errors
        log.matchErrors(const_string);
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        TestPhase phase1 = new RestartTestPhase();
        phase1.execute();
        Thread.sleep(100);
        PVManager.getDefaultDataSource().close();
    }

}
