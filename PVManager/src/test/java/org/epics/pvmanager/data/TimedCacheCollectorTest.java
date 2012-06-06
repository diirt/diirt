/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.epics.pvmanager.Collector;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.PrivateFactory;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.util.time.TimeDuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        DataTypeSupport.install();
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
        Collector<VDouble> collector =
                PrivateFactory.newTimeCacheCollector(cache, TimeDuration.ofMillis(100));
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