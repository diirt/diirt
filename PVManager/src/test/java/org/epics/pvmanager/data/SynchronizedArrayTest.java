/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import org.epics.pvmanager.Function;
import org.epics.pvmanager.util.TimeDuration;
import org.epics.pvmanager.util.TimeStamp;
import org.epics.pvmanager.ValueCache;
import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.Collector;
import org.epics.pvmanager.PrivateFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.epics.pvmanager.data.DataUtils.*;
import static org.epics.pvmanager.util.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class SynchronizedArrayTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        TypeSupport.install();
    }

    @Test
    public void missingValues() throws InterruptedException {
        int nPvs = 5;
        List<ValueCache<VDouble>> caches = new ArrayList<ValueCache<VDouble>>();
        List<String> names = new ArrayList<String>();
        List<Collector<VDouble>> collectors = new ArrayList<Collector<VDouble>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<VDouble>(VDouble.class));
            collectors.add(PrivateFactory.newTimeCacheCollector(caches.get(i), TimeDuration.ms(10)));
            names.add("pv" + i);
        }
        @SuppressWarnings("unchecked")
        SynchronizedVDoubleAggregator aggregator =
                new SynchronizedVDoubleAggregator(names, (List<Function<List<VDouble>>>) (List) collectors, TimeDuration.nanos(10));

        TimeStamp reference = TimeStamp.now();
        TimeStamp secondPass = reference.plus(ms(1));
        TimeStamp thirdPass = reference.plus(ms(2));

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

    @Test
    public void reconstructArray() throws InterruptedException {
        int nPvs = 5;
        List<ValueCache<VDouble>> caches = new ArrayList<ValueCache<VDouble>>();
        List<String> names = new ArrayList<String>();
        List<Collector<VDouble>> collectors = new ArrayList<Collector<VDouble>>();
        for (int i = 0; i < nPvs; i++) {
            caches.add(new ValueCache<VDouble>(VDouble.class));
            collectors.add(PrivateFactory.newTimeCacheCollector(caches.get(i), TimeDuration.ms(10)));
            names.add("pv" + i);
        }
        @SuppressWarnings("unchecked")
        SynchronizedVDoubleAggregator aggregator =
                new SynchronizedVDoubleAggregator(names, (List<Function<List<VDouble>>>) (List) collectors, TimeDuration.ms(5));

        TimeStamp reference = TimeStamp.now();
        TimeStamp future1 = reference.plus(TimeDuration.ms(1));
        TimeStamp past1 = reference.minus(TimeDuration.ms(1));
        TimeStamp past2 = reference.minus(TimeDuration.ms(2));

        // Set values
        caches.get(0).setValue(createValue(reference, 0.0));
        collectors.get(0).collect();
        caches.get(0).setValue(createValue(future1, 1.0));
        collectors.get(0).collect();

        // Set values
        caches.get(1).setValue(createValue(past2, 1.0));
        collectors.get(1).collect();
        caches.get(1).setValue(createValue(past1, 1.0));
        collectors.get(1).collect();
        caches.get(1).setValue(createValue(reference, 0.0));
        collectors.get(1).collect();

        // Set values
        caches.get(2).setValue(createValue(past1, 1.0));
        collectors.get(2).collect();
        caches.get(2).setValue(createValue(reference, 0.0));
        collectors.get(2).collect();

        // Set values
        caches.get(3).setValue(createValue(reference, 0.0));
        collectors.get(3).collect();
        caches.get(3).setValue(createValue(future1, 2.0));
        collectors.get(3).collect();

        // Set values
        caches.get(4).setValue(createValue(reference, 0.0));
        collectors.get(4).collect();

        VMultiDouble array = aggregator.getValue();
        assertEquals(0.0, array.getValues().get(0).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(1).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(2).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(3).getValue(), 0.0);
        assertEquals(0.0, array.getValues().get(4).getValue(), 0.0);

    }

}