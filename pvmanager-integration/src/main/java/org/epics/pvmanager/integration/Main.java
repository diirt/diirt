/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.integration;

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
        
        TestPhase phase1 = new RestartTestPhase();
        
        phase1.execute();
        
        Thread.sleep(100);
        
        PVManager.getDefaultDataSource().close();
        
        
    }
}
