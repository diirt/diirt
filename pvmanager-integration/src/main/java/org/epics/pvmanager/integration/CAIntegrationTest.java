/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.integration;

import java.util.Arrays;
import java.util.List;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.jca.JCADataSourceBuilder;

/**
 *
 * @author carcassi
 */
public class CAIntegrationTest {
    public static void main(String[] args) {
        //LogManager.getLogManager().readConfiguration(new FileInputStream(new File("logging.properties")));
        List<TestPhase> phases = Arrays.<TestPhase>asList(new UpdateTestPhase(),
                new RestartTestPhase(),
                new OutageTestPhase(),
                new TypeChangeTestPhase(),
                new RepeatedDisconnectTestPhase());
        int debugLevel = 1;
        
        PVManager.setDefaultDataSource(new JCADataSource());
        
        for (TestPhase phase : phases) {
            phase.setDebugLevel(debugLevel);
            phase.execute();
        }
        
        PVManager.getDefaultDataSource().close();
        
        PVManager.setDefaultDataSource(new JCADataSourceBuilder().dbePropertySupported(true).build());

        phases = Arrays.<TestPhase>asList(new UpdateTestPhase(),
                new RestartTestPhase(),
                new OutageTestPhase(),
                new TypeChangeTestPhase(),
                new RepeatedDisconnectTestPhase());
        
        for (TestPhase phase : phases) {
            phase.setDebugLevel(debugLevel);
            phase.execute();
        }
        
        PVManager.getDefaultDataSource().close();
    }
}
