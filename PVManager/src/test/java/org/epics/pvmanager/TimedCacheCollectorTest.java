/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Collections;
import org.epics.pvmanager.sim.SimulationDataSource;
import gov.aps.jca.dbr.DBR_TIME_Double;
import org.epics.pvmanager.jca.JCASupport;
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
        JCASupport.jca();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void correctNumberOfValuesInCache() throws InterruptedException {
        ValueCache<DBR_TIME_Double> cache =
                new ValueCache<DBR_TIME_Double>(DBR_TIME_Double.class);
        TimedCacheCollector<DBR_TIME_Double> collector =
                new TimedCacheCollector<DBR_TIME_Double>(cache, TimeDuration.ms(1000));
        DataSourceRecipe monitorRecipe = new DataSourceRecipe();
        monitorRecipe = monitorRecipe.includeCollector(collector, Collections.<String, ValueCache>singletonMap(SimulationDataSource.mockPVName(1, 100, 300), cache));
        SimulationDataSource.simulatedData().monitor(monitorRecipe);

        // After 100 ms there should be one element
        Thread.sleep(100);
        assertTrue(Math.abs(1 - collector.getData().size()) < 2);

        // After another second there should be 10 or 11 samples
        Thread.sleep(1000);
        assertTrue(Math.abs(11 - collector.getData().size()) < 2);
        
        // After another second there should be 10 or 11 samples
        Thread.sleep(1000);
        assertTrue(Math.abs(11 - collector.getData().size()) < 2);
    }

}