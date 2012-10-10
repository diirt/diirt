/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.tests;

import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.jca.JCADataSourceBuilder;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.pvmanager.util.Executors;

/**
 *
 * @author carcassi
 */
public class SetupUtil {
    public static void defaultCASetup() {
        CompositeDataSource dataSource = new CompositeDataSource();
        dataSource.putDataSource("sim", SimulationDataSource.simulatedData());
        dataSource.putDataSource("ca", new JCADataSourceBuilder().build());
        dataSource.setDefaultDataSource("ca");
        PVManager.setDefaultDataSource(dataSource);
    }
    public static void defaultCASetupForSwing() {
        PVManager.setDefaultNotificationExecutor(Executors.swingEDT());
        defaultCASetup();
    }
}
