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

/**
 * Tests reconnects caused by a server restart.
 *
 * @author carcassi
 */
public class UpdateTestPhase extends TestPhase {

    @Override
    public final void run() throws Exception {
        // Open command writer
        addWriter("command", PVManager.write(channel("command")));
        Thread.sleep(1000);

        // Reset ioc to known state
        write("command", "start phase1 1");
        Thread.sleep(10000);
        
        addReader(PVManager.read(channel(const_double)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_string)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(const_enum)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(counter_double_1Hz)), TimeDuration.ofHertz(50));
        addReader(PVManager.read(channel(counter_double_100Hz)), TimeDuration.ofHertz(50));
        addWriter(const_double, PVManager.write(channel(const_double)));
        pause(1000);
        write(const_double, 3.0);
        pause(4000);
    }

    @Override
    public final void verify(Log log) {
        log.matchConnections(const_double, true);
        log.matchValues(const_double, ALL_EXCEPT_TIME,
                const_double_value,
                newVDouble(3.0, newAlarm(AlarmSeverity.NONE, "NO_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()));
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
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new UpdateTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
