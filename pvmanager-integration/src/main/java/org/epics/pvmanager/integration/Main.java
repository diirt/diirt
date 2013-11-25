package org.epics.pvmanager.integration;

/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

import org.epics.pvmanager.integration.*;
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
public class Main {
    public static void main(String[] args) throws Exception {
        PVManager.setDefaultDataSource(new JCADataSource());
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        
        TestPhase phase1 = new TestPhase() {

            @Override
            public void run() throws Exception{
                addReader(PVManager.read(channel("const-double")), TimeDuration.ofHertz(50));
                addWriter("command", PVManager.write(channel("command")));
                Thread.sleep(1000);
                write("command", "start phase1 1");
                Thread.sleep(10000);
            }

            @Override
            public void verify(Log log) {
                log.matchConnections("const-double", true, false, true);
            }

        };
        
        phase1.execute();
        
        Thread.sleep(100);
        
        PVManager.getDefaultDataSource().close();
        
        
    }
}
