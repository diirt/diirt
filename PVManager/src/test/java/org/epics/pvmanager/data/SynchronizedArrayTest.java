/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.PrivateFactory;
import org.epics.pvmanager.ValueCache;
import static org.epics.pvmanager.data.ValueFactory.*;
import static org.epics.util.time.TimeDuration.*;
import org.epics.util.time.Timestamp;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class SynchronizedArrayTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        DataTypeSupport.install();
    }
//
//    @Test
//    public void missingValues() throws InterruptedException {
//        int nPvs = 5;
//        List<ValueCache<VDouble>> caches = new ArrayList<ValueCache<VDouble>>();
//        List<String> names = new ArrayList<String>();
//        List<Collector<VDouble>> collectors = new ArrayList<Collector<VDouble>>();
//        for (int i = 0; i < nPvs; i++) {
//            caches.add(new ValueCache<VDouble>(VDouble.class));
//            collectors.add(PrivateFactory.newTimeCacheCollector(caches.get(i), ofMillis(10)));
//            names.add("pv" + i);
//        }
//        @SuppressWarnings("unchecked")
//        SynchronizedVDoubleAggregator aggregator =
//                new SynchronizedVDoubleAggregator(names, (List<Function<List<VDouble>>>) (List) collectors, ofNanos(10));
//
//        Timestamp reference = Timestamp.now();
//        Timestamp secondPass = reference.plus(ofMillis(1));
//        Timestamp thirdPass = reference.plus(ofMillis(2));
//
//        // Set values
//        caches.get(0).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(0).collect();
//        caches.get(0).setValue(newVDouble(1.0, newTime(secondPass)));
//        collectors.get(0).collect();
//
//        // Set values
//        caches.get(1).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(1).collect();
//        caches.get(1).setValue(newVDouble(1.0, newTime(secondPass)));
//        collectors.get(1).collect();
//        caches.get(1).setValue(newVDouble(2.0, newTime(thirdPass)));
//        collectors.get(1).collect();
//
//        // Set values
//        caches.get(2).setValue(newVDouble(1.0, newTime(secondPass)));
//        collectors.get(2).collect();
//        caches.get(2).setValue(newVDouble(2.0, newTime(thirdPass)));
//        collectors.get(2).collect();
//
//        // Set values
//        caches.get(3).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(3).collect();
//        caches.get(3).setValue(newVDouble(2.0, newTime(thirdPass)));
//        collectors.get(3).collect();
//
//        // Set values
//        caches.get(4).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(4).collect();
//
//        VMultiDouble array = aggregator.getValue();
//        assertEquals(0.0, array.getValues().get(0).getValue(), 0.0);
//        assertEquals(0.0, array.getValues().get(1).getValue(), 0.0);
//        assertNull(array.getValues().get(2));
//        assertEquals(0.0, array.getValues().get(3).getValue(), 0.0);
//        assertEquals(0.0, array.getValues().get(4).getValue(), 0.0);
//
//    }
//
//    @Test
//    public void reconstructArray() throws InterruptedException {
//        int nPvs = 5;
//        List<ValueCache<VDouble>> caches = new ArrayList<ValueCache<VDouble>>();
//        List<String> names = new ArrayList<String>();
//        List<Collector<VDouble>> collectors = new ArrayList<Collector<VDouble>>();
//        for (int i = 0; i < nPvs; i++) {
//            caches.add(new ValueCache<VDouble>(VDouble.class));
//            collectors.add(PrivateFactory.newTimeCacheCollector(caches.get(i), ofMillis(10)));
//            names.add("pv" + i);
//        }
//        @SuppressWarnings("unchecked")
//        SynchronizedVDoubleAggregator aggregator =
//                new SynchronizedVDoubleAggregator(names, (List<Function<List<VDouble>>>) (List) collectors, ofMillis(5));
//
//        Timestamp reference = Timestamp.now();
//        Timestamp future1 = reference.plus(ofMillis(1));
//        Timestamp past1 = reference.minus(ofMillis(1));
//        Timestamp past2 = reference.minus(ofMillis(2));
//
//        // Set values
//        caches.get(0).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(0).collect();
//        caches.get(0).setValue(newVDouble(1.0, newTime(future1)));
//        collectors.get(0).collect();
//
//        // Set values
//        caches.get(1).setValue(newVDouble(1.0, newTime(past2)));
//        collectors.get(1).collect();
//        caches.get(1).setValue(newVDouble(1.0, newTime(past1)));
//        collectors.get(1).collect();
//        caches.get(1).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(1).collect();
//
//        // Set values
//        caches.get(2).setValue(newVDouble(1.0, newTime(past1)));
//        collectors.get(2).collect();
//        caches.get(2).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(2).collect();
//
//        // Set values
//        caches.get(3).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(3).collect();
//        caches.get(3).setValue(newVDouble(2.0, newTime(future1)));
//        collectors.get(3).collect();
//
//        // Set values
//        caches.get(4).setValue(newVDouble(0.0, newTime(reference)));
//        collectors.get(4).collect();
//
//        VMultiDouble array = aggregator.getValue();
//        assertEquals(0.0, array.getValues().get(0).getValue(), 0.0);
//        assertEquals(0.0, array.getValues().get(1).getValue(), 0.0);
//        assertEquals(0.0, array.getValues().get(2).getValue(), 0.0);
//        assertEquals(0.0, array.getValues().get(3).getValue(), 0.0);
//        assertEquals(0.0, array.getValues().get(4).getValue(), 0.0);
//
//    }

}