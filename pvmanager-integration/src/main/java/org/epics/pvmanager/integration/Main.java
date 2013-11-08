/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

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
import static org.epics.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        PVManager.setDefaultDataSource(new JCADataSource());
        
        PVReaderTestCase<Object> test = PVReaderTestCase.newTest(PVManager.read(channel("passive_double")));
        test.addListener(PVReaderTestListener.matchConnections(true));
        test.addListener(PVReaderTestListener.matchValues(newVDouble(0.13, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone())));
        test.start(TimeDuration.ofSeconds(1));
        
        PVReaderTestCase<Object> test2 = PVReaderTestCase.newTest(PVManager.read(channel("passive_double.NAME")));
        test2.addListener(PVReaderTestListener.matchConnections(true));
        test2.addListener(PVReaderTestListener.matchValues(newVString("passive_double", newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false))));
        test2.start(TimeDuration.ofSeconds(1));
        
        test.await();
        test2.await();

        test.printErrors();
        test2.printErrors();
        
        Thread.sleep(100);
        
        PVManager.getDefaultDataSource().close();
        
        
    }
}
