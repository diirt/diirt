/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.integration;

import static org.diirt.datasource.ExpressionLanguage.channel;
import static org.diirt.datasource.integration.Constants.alarm_string;
import static org.diirt.datasource.integration.Constants.alarm_string_value;
import static org.diirt.datasource.integration.Constants.const_double;
import static org.diirt.datasource.integration.Constants.const_double_value;
import static org.diirt.datasource.integration.Constants.const_enum;
import static org.diirt.datasource.integration.Constants.const_enum_value;
import static org.diirt.datasource.integration.Constants.const_int;
import static org.diirt.datasource.integration.Constants.const_int_value;
import static org.diirt.datasource.integration.Constants.const_string;
import static org.diirt.datasource.integration.Constants.const_string_value;
import static org.diirt.datasource.integration.Constants.counter_double_100Hz;
import static org.diirt.datasource.integration.Constants.counter_double_1Hz;
import static org.diirt.datasource.integration.VTypeMatchMask.ALL;
import static org.diirt.datasource.integration.VTypeMatchMask.ALL_EXCEPT_TIME;
import static org.diirt.datasource.integration.VTypeMatchMask.VALUE;

import java.time.Instant;
import java.util.Arrays;

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
public class UpdateTestPhase extends AbstractCATestPhase {

    @Override
    public final void run() throws Exception {
        init("phase1");

        addReader(PVManager.read(channel(const_double)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_int)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_string)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_enum)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(counter_double_1Hz)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(counter_double_100Hz)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(alarm_string)), TimeDuration.ofHertz(50));
        addWriter(const_double, PVManager.write(channel(const_double)));
        addWriter(const_int, PVManager.write(channel(const_int)));
        pause(1000);
        write(const_double, 3.0);
        write(const_int, 42);
        pause(4000);
        singleChannelConnection(const_double);
    }

    @Override
    public final void verify(Log log) {
        // Check double
        log.matchConnections(const_double, true);
        log.matchValues(const_double, ALL_EXCEPT_TIME,
                const_double_value,
                VDouble.of(3.0, Alarm.of(AlarmSeverity.NONE, null, "NO_ALARM"), Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none()));
        log.matchWriteConnections(const_double, true);
        log.matchWriteNotifications(const_double, true);

        // Check int
        log.matchConnections(const_int, true);
        log.matchValues(const_int, ALL_EXCEPT_TIME,
                const_int_value,
                VInt.of(42, Alarm.of(AlarmSeverity.NONE, null, "NO_ALARM"), Time.of(Instant.ofEpochSecond(631152000, 0), null, false), Display.none()));
        log.matchWriteConnections(const_double, true);
        log.matchWriteNotifications(const_double, true);

        log.matchConnections(const_string, true);
        log.matchValues(const_string, ALL, const_string_value);
        log.matchConnections(const_enum, true);
        log.matchValues(const_enum, ALL, const_enum_value);
        log.matchConnections(counter_double_1Hz, true);
        log.matchSequentialNumberValues(counter_double_1Hz, 0);
        log.matchValueEventRate(counter_double_1Hz, 0.95, 1.05);
        log.matchConnections(counter_double_100Hz, true);
        log.matchValueEventRate(counter_double_100Hz, 45, 50);
        log.matchAllValues(alarm_string, VALUE, alarm_string_value);
        log.validate(alarm_string, Validators.cycleValidator(VTypeMatchMask.ALARM, Arrays.<Object>asList(Alarm.of(AlarmSeverity.NONE, null, "NO_ALARM"),
                Alarm.of(AlarmSeverity.MINOR, null, "LINK_ALARM"), Alarm.of(AlarmSeverity.NONE, null, "NO_ALARM"),
                Alarm.of(AlarmSeverity.MAJOR, null, "LINK_ALARM"), Alarm.of(AlarmSeverity.NONE, null, "NO_ALARM"),
                Alarm.of(AlarmSeverity.INVALID, null, "LINK_ALARM"))));
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSourceConfiguration().dbePropertySupported(false).create());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new UpdateTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
