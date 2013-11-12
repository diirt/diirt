/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import java.util.Arrays;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.jca.JCADataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVReaderEvent;
import org.epics.pvmanager.PVReaderListener;
import static org.epics.pvmanager.vtype.ExpressionLanguage.*;
import org.epics.util.time.TimeDuration;
import org.epics.util.time.Timestamp;
import org.epics.vtype.AlarmSeverity;
import org.epics.vtype.Time;
import org.epics.vtype.VEnum;
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        PVManager.setDefaultDataSource(new JCADataSource());
        
        TestPhase phase1 = new TestPhase() {

            @Override
            public void run() throws Exception{
                addReader(PVManager.read(channel("const-double")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("const-double.NAME")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("const-double.SCAN")), TimeDuration.ofHertz(50));
                addReader(PVManager.read(channel("double-counter-1Hz")), TimeDuration.ofHertz(50));
                Thread.sleep(5000);
            }

            @Override
            public void verify(Log log) {
                log.matchConnections("const-double", true);
                log.matchValues("const-double", newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()));
                log.matchConnections("const-double.NAME", true);
                log.matchValues("const-double.NAME", newVString("const-double", newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false)));
                log.matchConnections("const-double.SCAN", true);
                log.matchValues("const-double.SCAN", newVEnum(0, Arrays.asList("Passive", "Event", "I/O Intr", "10 second", "5 second", "2 second", "1 second", ".5 second", ".2 second", ".1 second"), newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false)));
                log.matchConnections("double-counter-1Hz", true);
            }
        };
        
        phase1.execute();
        
        Thread.sleep(100);
        
        PVManager.getDefaultDataSource().close();
        
        
    }
}
