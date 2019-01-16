/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import static org.diirt.datasource.ExpressionLanguage.channel;
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
import org.epics.vtype.VInt;

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class TypeChangeTestPhase extends AbstractCATestPhase {

    @Override
    public final void run() throws Exception {
        init("typeChange1");

        addReader(PVManager.read(channel("double-to-i32")), TimeDuration.ofHertz(50));
        //addReader(PVManager.read(channel("i32-to-double")), TimeDuration.ofHertz(50));
        pause(1000);

        restart("typeChange2");
        pause(2000);
    }

    @Override
    public final void verify(Log log) {
        // Check double
        log.matchConnections("double-to-i32", true, false, true);
        log.matchValues("double-to-i32", ALL_EXCEPT_TIME,
                VDouble.of(0.0, Alarm.of(AlarmSeverity.INVALID, null, "UDF_ALARM"), Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none()),
                VDouble.of(0.0, Alarm.of(AlarmSeverity.UNDEFINED, null, "Disconnected"), Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none()),
                VInt.of(0, Alarm.of(AlarmSeverity.INVALID, null, "UDF_ALARM"), Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none()));
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSourceConfiguration().create());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new TypeChangeTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
