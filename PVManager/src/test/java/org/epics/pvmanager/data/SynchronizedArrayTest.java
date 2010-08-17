/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import java.util.Collections;
import org.epics.pvmanager.DataRecipe;
import gov.aps.jca.dbr.DBR_TIME_Double;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.TimedCacheCollector;
import org.epics.pvmanager.ValueCache;
import java.util.ArrayList;
import java.util.List;
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
public class SynchronizedArrayTest {

    public SynchronizedArrayTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        EpicsTypeSupport.install();
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
        int nPvs = 100;
        List<ValueCache<VDouble>> caches = new ArrayList<ValueCache<VDouble>>();
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<VDouble>> collectors = new ArrayList<TimedCacheCollector<VDouble>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<VDouble>(VDouble.class));
            collectors.add(new TimedCacheCollector<VDouble>(caches.get(i), TimeDuration.ms(1000)));
            DataRecipe connRecipe = new DataRecipe();
            connRecipe = connRecipe.includeCollector(collectors.get(i),
                    Collections.<String,ValueCache>singletonMap("gaussian(0.0, 1.0, 0.1)", caches.get(i)));
            names.add("pv" + i);
            SimulationDataSource.simulatedData().connect(connRecipe);
        }
        SynchronizedArrayAggregator aggregator =
                new SynchronizedArrayAggregator(names, collectors, TimeDuration.ms(100));

        // After 100 ms there should be one element
        Thread.sleep(100);
        aggregator.getValue();
        for (VDouble data : aggregator.getValue().getValues()) {
            Double value = null;
            if (data == null) {
                fail("Not all elements had a value");
            } else {
                if (value == null) {
                    value = data.getValue();
                } else {
                    assertEquals("Value of elements in not the same (incorrect binning)", value, data.getValue());
                }
            }
        }

        Thread.sleep(500);
        aggregator.getValue();
        for (VDouble data : aggregator.getValue().getValues()) {
            Double value = null;
            if (data == null) {
                fail("Not all elements had a value");
            } else {
                if (value == null) {
                    value = data.getValue();
                } else {
                    assertEquals("Value of elements in not the same (incorrect binning)", value, data.getValue());
                }
            }
        }
    }

    @Test
    public void missingValues() throws InterruptedException {
        int nPvs = 5;
        List<ValueCache<VDouble>> caches = new ArrayList<ValueCache<VDouble>>();
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<VDouble>> collectors = new ArrayList<TimedCacheCollector<VDouble>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<VDouble>(VDouble.class));
            collectors.add(new TimedCacheCollector<VDouble>(caches.get(i), TimeDuration.ms(10)));
            names.add("pv" + i);
        }
        SynchronizedArrayAggregator aggregator =
                new SynchronizedArrayAggregator(names, collectors, TimeDuration.nanos(10));

        TimeStamp reference = TimeStamp.now();
        TimeStamp secondPass = reference.plus(TimeDuration.ms(1));
        TimeStamp thirdPass = reference.plus(TimeDuration.ms(2));

        // Set values
        caches.get(0).setValue(createValue(reference, 0.0));
        collectors.get(0).collect();
        caches.get(0).setValue(createValue(secondPass, 1.0));
        collectors.get(0).collect();

        // Set values
        caches.get(1).setValue(createValue(reference, 0.0));
        collectors.get(1).collect();
        caches.get(1).setValue(createValue(secondPass, 1.0));
        collectors.get(1).collect();
        caches.get(1).setValue(createValue(thirdPass, 2.0));
        collectors.get(1).collect();

        // Set values
        caches.get(2).setValue(createValue(secondPass, 1.0));
        collectors.get(2).collect();
        caches.get(2).setValue(createValue(thirdPass, 2.0));
        collectors.get(2).collect();

        // Set values
        caches.get(3).setValue(createValue(reference, 0.0));
        collectors.get(3).collect();
        caches.get(3).setValue(createValue(thirdPass, 2.0));
        collectors.get(3).collect();

        // Set values
        caches.get(4).setValue(createValue(reference, 0.0));
        collectors.get(4).collect();

        VMultiDouble array = aggregator.getValue();
        assertEquals(0.0, array.getValues().get(0).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(1).getValue(), 0.0);
        assertNull(array.getValues().get(2));
        assertEquals(0.0, array.getValues().get(3).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(4).getValue(), 0.0);

    }

    public static VDouble createValue(TimeStamp time, double aValue) {
        DBR_TIME_Double value = new DBR_TIME_Double();
        value.setTimeStamp(new gov.aps.jca.dbr.TimeStamp(time.getEpicsSec(), time.getNanoSec()));
        value.getDoubleValue()[0] = aValue;
        return ValueFactory.newVDouble(aValue, AlarmSeverity.NONE, Collections.<String>emptySet(),
                Collections.<String>emptyList(), time, null, Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE, "", null, Double.MAX_VALUE,
                Double.MAX_VALUE, Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE);
    }

}