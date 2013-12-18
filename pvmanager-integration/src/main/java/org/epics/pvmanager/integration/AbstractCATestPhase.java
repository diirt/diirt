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
import org.epics.vtype.VDouble;

/**
 * Superclass for Channel Access tests.
 *
 * @author carcassi
 */
public abstract class AbstractCATestPhase extends TestPhase {

    protected void init(String iocName) {
        // Open command writer
        addReader(PVManager.read(channel("command")), TimeDuration.ofHertz(50));
        addWriter("command", PVManager.write(channel("command")));
        pause(1000);
        
        // Reset ioc to known state
        restart(iocName);
    }
    
    protected void restart(String iocName) {
        pause(500);
        write("command", "start " + iocName + " 1");
        pause(500);
        waitFor("command", "ready", 20000);
    }

}
