/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.pvmanager.integration;

import static org.epics.pvmanager.ExpressionLanguage.channel;
import org.epics.pvmanager.PVManager;
import org.epics.util.time.TimeDuration;

/**
 *
 * @author carcassi
 */
public class RestartTestPhase extends TestPhase {

    @Override
    public void run() throws Exception {
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

}
