/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.util.Executors.*;
import static org.epics.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class PVManagerTest {

    public PVManagerTest() {
    }
    
    @Before @After
    public void restoreDefaults() {
        PVManager.setDefaultDataSource(null);
        PVManager.setDefaultNotificationExecutor(localThread());
    }

    @Test(expected=IllegalStateException.class)
    public void lackDataSource() {
        PVManager.setDefaultDataSource(null);

        PVManager.read(channel("test")).maxRate(ofHertz(10));
    }

    @Test
    public void overrideDataSource() {
        // XXX: this should not use the JCA data source
        PVManager.setDefaultDataSource(new JCADataSource());

        PVManager.read(channel("test")).from(SimulationDataSource.simulatedData()).maxRate(ofHertz(10));
    }

    @Test(expected=IllegalStateException.class)
    public void lackThreadSwitch() {
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());
        PVManager.setDefaultNotificationExecutor(null);

        PVManager.read(channel("test")).maxRate(ofHertz(10));
    }

    @Test
    public void overrideThreadSwitch() {
        PVManager.setDefaultDataSource(SimulationDataSource.simulatedData());

        PVManager.read(channel("test")).notifyOn(swingEDT()).maxRate(ofHertz(10));
    }

}