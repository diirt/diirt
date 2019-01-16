/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.datasource.integration.Constants.const_double;
import static org.diirt.datasource.integration.Constants.const_double_value;
import static org.diirt.datasource.integration.VTypeMatchMask.ALL;

import org.diirt.datasource.PVManager;
import org.diirt.util.time.TimeDuration;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.VDouble;

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
        //addReader(PVManager.read(channel(const_string)), TimeDuration.ofHertz(50));

        disconnectCycle();

        pause(500);

        singleChannelConnection(const_double);
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
                VDouble.of(const_double_value.getValue(), Alarm.of(AlarmSeverity.UNDEFINED, null, "Disconnected"), const_double_value.getTime(), const_double_value.getDisplay()),
                const_double_value);
        // No errors
        log.matchErrors(const_double);

        // *** const_string ***
        // Connection should go up, down and back up
        //log.matchConnections(const_string, true, false, true);
        // Value should remain the same, but change alarm
//        log.matchValues(const_string, ALL,
//                const_string_value,
//                newVString(const_string_value.getValue(), Alarm.of(AlarmSeverity.UNDEFINED, "Disconnected"), const_string_value),
//                const_string_value);
//        // No errors
//        log.matchErrors(const_string);
    }

}
