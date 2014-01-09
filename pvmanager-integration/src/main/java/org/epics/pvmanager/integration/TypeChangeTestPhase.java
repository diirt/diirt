/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
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
                newVDouble(0.0, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                newVDouble(0.0, newAlarm(AlarmSeverity.UNDEFINED, "Disconnected"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()),
                newVInt(0, newAlarm(AlarmSeverity.INVALID, "UDF_ALARM"), newTime(Timestamp.of(631152000, 0), null, false), displayNone()));
    }

    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        TestPhase phase1 = new TypeChangeTestPhase();
        phase1.execute();
        PVManager.getDefaultDataSource().close();
    }

}
