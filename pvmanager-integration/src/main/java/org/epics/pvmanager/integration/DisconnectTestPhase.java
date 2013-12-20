/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVManager;
import org.epics.util.time.TimeDuration;
import org.epics.vtype.AlarmSeverity;
import static org.epics.pvmanager.integration.VTypeMatchMask.*;
import static org.epics.vtype.ValueFactory.*;
import static org.epics.pvmanager.integration.Constants.*;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public abstract class DisconnectTestPhase extends AbstractCATestPhase {

    @Override
    public final void run() throws Exception {
        init("phase1");
        
        // Add all constant fields
        // TODO: missing float, int, short, byte, string and all arrays
        addReader(PVManager.read(channel(const_double)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_string)), TimeDuration.ofHertz(50));

        disconnectCycle();
        
        channelConnections(const_double, 1);
    }
    
    public abstract void disconnectCycle();

    @Override
    public final void verify(Log log) {
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

}
