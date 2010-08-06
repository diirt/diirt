/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.types;

import gov.bnl.pvmanager.types.SynchronizedArray;
import gov.bnl.pvmanager.types.SynchronizedArrayAggregator;
import gov.aps.jca.dbr.DBR_TIME_Double;
import gov.bnl.pvmanager.MockDataSource;
import gov.bnl.pvmanager.MonitorRecipe;
import gov.bnl.pvmanager.TimeDuration;
import gov.bnl.pvmanager.TimeStamp;
import gov.bnl.pvmanager.TimedCacheCollector;
import gov.bnl.pvmanager.ValueCache;
import gov.bnl.pvmanager.jca.JCASupport;
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
        // Installs JCA types
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
        int nPvs = 100;
        List<ValueCache<DBR_TIME_Double>> caches = new ArrayList<ValueCache<DBR_TIME_Double>>();
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<DBR_TIME_Double>> collectors = new ArrayList<TimedCacheCollector<DBR_TIME_Double>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<DBR_TIME_Double>(DBR_TIME_Double.class));
            collectors.add(new TimedCacheCollector<DBR_TIME_Double>(caches.get(i), TimeDuration.ms(1000)));
            MonitorRecipe monitorRecipe = new MonitorRecipe();
            monitorRecipe.collector = collectors.get(i);
            monitorRecipe.caches.put(MockDataSource.mockPVName(1, 100, 300) + "linear", caches.get(i));
            names.add("pv" + i);
            MockDataSource.mockData().monitor(monitorRecipe);
        }
        SynchronizedArrayAggregator<DBR_TIME_Double> aggregator =
                new SynchronizedArrayAggregator<DBR_TIME_Double>(names, collectors, TimeDuration.ms(100));

        // After 100 ms there should be one element
        Thread.sleep(100);
        aggregator.getValue();
        for (DBR_TIME_Double data : aggregator.getValue().getValues()) {
            Double value = null;
            if (data == null) {
                fail("Not all elements had a value");
            } else {
                if (value == null) {
                    value = ((double[]) data.getValue())[0];
                } else {
                    assertEquals("Value of elements in not the same (incorrect binning)", value, (Double) ((double[]) data.getValue())[0]);
                }
            }
        }

        Thread.sleep(500);
        aggregator.getValue();
        for (DBR_TIME_Double data : aggregator.getValue().getValues()) {
            Double value = null;
            if (data == null) {
                fail("Not all elements had a value");
            } else {
                if (value == null) {
                    value = ((double[]) data.getValue())[0];
                } else {
                    assertEquals("Value of elements in not the same (incorrect binning)", value, (Double) ((double[]) data.getValue())[0]);
                }
            }
        }
    }

    @Test
    public void missingValues() throws InterruptedException {
        int nPvs = 5;
        List<ValueCache<DBR_TIME_Double>> caches = new ArrayList<ValueCache<DBR_TIME_Double>>();
        List<String> names = new ArrayList<String>();
        List<TimedCacheCollector<DBR_TIME_Double>> collectors = new ArrayList<TimedCacheCollector<DBR_TIME_Double>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<DBR_TIME_Double>(DBR_TIME_Double.class));
            collectors.add(new TimedCacheCollector<DBR_TIME_Double>(caches.get(i), TimeDuration.ms(10)));
            names.add("pv" + i);
        }
        SynchronizedArrayAggregator<DBR_TIME_Double> aggregator =
                new SynchronizedArrayAggregator<DBR_TIME_Double>(names, collectors, TimeDuration.nanos(10));

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

        SynchronizedArray<DBR_TIME_Double> array = aggregator.getValue();
        assertEquals(0.0, array.getValues().get(0).getDoubleValue()[0], 0.0);
        assertEquals(0.0, array.getValues().get(1).getDoubleValue()[0], 0.0);
        assertNull(array.getValues().get(2));
        assertEquals(0.0, array.getValues().get(3).getDoubleValue()[0], 0.0);
        assertEquals(0.0, array.getValues().get(4).getDoubleValue()[0], 0.0);

    }

    public static DBR_TIME_Double createValue(TimeStamp time, double aValue) {
        DBR_TIME_Double value = new DBR_TIME_Double();
        value.setTimeStamp(new gov.aps.jca.dbr.TimeStamp(time.getEpicsSec(), time.getNanoSec()));
        value.getDoubleValue()[0] = aValue;
        return value;
    }

}