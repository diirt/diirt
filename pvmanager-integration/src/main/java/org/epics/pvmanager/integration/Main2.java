/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.Arrays;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import static org.epics.vtype.ValueFactory.*;
import static org.epics.pvmanager.integration.VTypeMatchMask.*;

/**
 *
 * @author carcassi
 */
public class Main2 {
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        
        TestPhase phase1 = new TestPhase() {

            @Override
            public void run() throws Exception{
                addReader(PVManager.read(channel("const-double")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("const-double.NAME")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("const-double.SCAN")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("double-counter-1Hz")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("double-counter-100Hz")), TimeDuration.ofHertz(50));
                addWriter("const-double", PVManager.write(channel("const-double")));
                Thread.sleep(1000);
                write("const-double", 3.0);
                Thread.sleep(4000);
            }

            @Override
            public void verify(Log log) {
                log.matchConnections("const-double", true);
                log.matchValues("const-double", ALL_EXCEPT_TIME, newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                        newVDouble(3.0, newAlarm(AlarmSeverity.NONE, "NO_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()));
                log.matchWriteConnections("const-double", true);
                log.matchWriteNotifications("const-double", true);
                log.matchConnections("const-double.NAME", true);
                log.matchValues("const-double.NAME", ALL, newVString("const-double", newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false)));
                log.matchConnections("const-double.SCAN", true);
                log.matchValues("const-double.SCAN", ALL, newVEnum(0, Arrays.asList("Passive", "Event", "I/O Intr", "10 second", "5 second", "2 second", "1 second", ".5 second", ".2 second", ".1 second"), newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false)));
                log.matchConnections("double-counter-1Hz", true);
                log.matchSequentialNumberValues("double-counter-1Hz", 0);
                log.matchValueEventRate("double-counter-1Hz", 0.95, 1.05);
                log.matchConnections("double-counter-100Hz", true);
                log.matchValueEventRate("double-counter-100Hz", 45, 50);
            }

        };
        
        phase1.execute();
        
        Thread.sleep(100);
        
        PVManager.getDefaultDataSource().close();
        
        
    }
}
