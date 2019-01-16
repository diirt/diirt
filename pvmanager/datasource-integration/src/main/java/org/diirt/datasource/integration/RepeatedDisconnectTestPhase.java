/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.datasource.integration.Constants.const_double;
import static org.diirt.datasource.integration.Constants.const_double_value;
import static org.diirt.datasource.integration.VTypeMatchMask.ALL_EXCEPT_TIME;

import java.time.Instant;

import org.diirt.datasource.PVManager;
import org.diirt.support.ca.JCADataSourceConfiguration;
import org.diirt.util.time.TimeDuration;
import org.epics.vtype.Alarm;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;

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
        VDouble disconnected = VDouble.of(0.13, Alarm.of(AlarmSeverity.UNDEFINED, null, "Disconnected"), Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none());
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
