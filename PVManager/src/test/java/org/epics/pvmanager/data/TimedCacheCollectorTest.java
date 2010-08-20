/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import org.epics.pvmanager.SourceRateExpression;
import java.util.Collections;
import org.epics.pvmanager.sim.SimulationDataSource;
import gov.aps.jca.dbr.DBR_TIME_Double;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimedCacheCollector;
import org.epics.pvmanager.ValueCache;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class TimedCacheCollectorTest {

    public TimedCacheCollectorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Force type support loading
        EpicsTypeSupport.install();
    }

    @Before
    public void setUp() {
        monitorRecipe = null;
    }

    @After
    public void tearDown() {
        // Always disconnect
        if (monitorRecipe != null)
            SimulationDataSource.simulatedData().disconnect(monitorRecipe);
    }

    DataRecipe monitorRecipe;

    @Test
    public void correctNumberOfValuesInCache() throws InterruptedException {
        ValueCache<VDouble> cache =
                new ValueCache<VDouble>(VDouble.class);
        TimedCacheCollector<VDouble> collector =
                new TimedCacheCollector<VDouble>(cache, TimeDuration.ms(100));
        monitorRecipe = new DataRecipe();
        monitorRecipe = monitorRecipe.includeCollector(collector, Collections.<String, ValueCache>singletonMap("gaussian(0.0, 1.0, 0.01)", cache));
        SimulationDataSource.simulatedData().connect(monitorRecipe);

        // After 100 ms there should be one element
        Thread.sleep(10);
        assertTrue(Math.abs(1 - collector.getValue().size()) < 2);

        // After another second there should be 10 or 11 samples
        Thread.sleep(100);
        assertTrue("Was " + collector.getValue().size(), Math.abs(10 - collector.getValue().size()) < 2);
        
        // After another second there should be 10 or 11 samples
        Thread.sleep(100);
        assertTrue("Was " + collector.getValue().size(), Math.abs(10 - collector.getValue().size()) < 2);

    }

}