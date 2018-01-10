/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import static org.diirt.datasource.ExpressionLanguage.*;

import java.time.Instant;

import org.diirt.datasource.PVManager;
import org.diirt.util.time.TimeDuration;
import org.diirt.vtype.AlarmSeverity;

import static org.diirt.datasource.integration.VTypeMatchMask.*;
import static org.diirt.vtype.ValueFactory.*;
import static org.diirt.datasource.integration.Constants.*;

import org.diirt.support.ca.JCADataSourceConfiguration;
import org.diirt.vtype.VDouble;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class RepeatedDisconnectTestPhase extends AbstractCATestPhase {

    @Override
    public final void run() throws Exception {
        init("phase1");

        addReader(PVManager.read(channel(const_double)), TimeDuration.ofHertz(50));

        // Perfom ten restarts
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
        restart("phase1");
    }

    @Override
    public final void verify(Log log) {
        // Check double
        VDouble disconnected = newVDouble(0.13, newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), newTime(Instant.ofEpochSecond(631152000, 0), null, false), displayNone());
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
        PVManager.setDefaultDataSource(new JCADataSourceConfiguration().create());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new RepeatedDisconnectTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
