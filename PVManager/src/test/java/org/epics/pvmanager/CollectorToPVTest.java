/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Collections;
import org.epics.pvmanager.sim.SimulationDataSource;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;
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
public class CollectorToPVTest {

    public CollectorToPVTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Force type support loading
        Expression<Double> exp = org.epics.pvmanager.types.ExpressionLanguage.doublePv("test");
        if (exp.hashCode() == 0)
            System.out.println("Loaded");
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

    private volatile PV<Double> pv;
    private AtomicInteger counter;

    @Test
    public void testFasterRate() throws Exception {
        long testTimeMs = 5000;
        long scanPeriodMs = 40;
        long notificationPeriodMs = 1;
        int samplesPerNotification = 5;
        final int nNotifications = (int) (testTimeMs / notificationPeriodMs);
        int maxNotifications = (int) (testTimeMs / scanPeriodMs);
        int targetNotifications = Math.min(nNotifications, maxNotifications);

        final ValueCache<Double> cache = new ValueCache<Double>(Double.class);
        final Collector<Double> collector = new QueueCollector<Double>(cache);
        counter = new AtomicInteger();
        LastValueAggregator<Double> aggregator = new LastValueAggregator<Double>(Double.class, collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pv = PV.createPv("My pv", Double.class);
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        counter.incrementAndGet();
                    }
                });
            }
        });
        Notifier<Double> notifier = new Notifier<Double>(pv, aggregator, ThreadSwitch.onSwingEDT());
        Scanner.scan(notifier, scanPeriodMs);
        DataRecipe connRecipe = new DataRecipe();
        connRecipe = connRecipe.includeCollector(collector, Collections.<String,ValueCache>singletonMap(SimulationDataSource.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications), cache));
        SimulationDataSource.simulatedData().connect(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get();
        if (Math.abs(actualNotification - targetNotifications) > 2) {
            fail("Expected " + targetNotifications + " but got " + actualNotification);
        }
    }

    @Test
    public void testSlowerRate() throws Exception {
        long testTimeMs = 5000;
        long scanPeriodMs = 40;
        long notificationPeriodMs = 100;
        int samplesPerNotification = 1;
        final int nNotifications = (int) (testTimeMs / notificationPeriodMs);
        int maxNotifications = (int) (testTimeMs / scanPeriodMs);
        int targetNotifications = Math.min(nNotifications, maxNotifications);

        final ValueCache<Double> cache = new ValueCache<Double>(Double.class);
        final Collector<Double> collector = new QueueCollector<Double>(cache);
        counter = new AtomicInteger();
        LastValueAggregator<Double> aggregator = new LastValueAggregator<Double>(Double.class, collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pv = PV.createPv("My pv", Double.class);
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        counter.incrementAndGet();
                    }
                });
            }
        });
        Notifier<Double> notifier = new Notifier<Double>(pv, aggregator, ThreadSwitch.onSwingEDT());
        Scanner.scan(notifier, scanPeriodMs);
        DataRecipe connRecipe = new DataRecipe();
        connRecipe = connRecipe.includeCollector(collector, Collections.<String,ValueCache>singletonMap(SimulationDataSource.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications), cache));
        SimulationDataSource.simulatedData().connect(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get() - 1;
        if (Math.abs(actualNotification - targetNotifications) > 1) {
            fail("Expected " + targetNotifications + " but got " + actualNotification);
        }
    }


//    @Test
//    public void testStatistics() throws Exception {
//        long testTimeMs = 5000;
//        long scanPeriodMs = 40;
//        long notificationPeriodMs = 1;
//        int samplesPerNotification = 5;
//        final int nNotifications = (int) (testTimeMs / notificationPeriodMs);
//        int maxNotifications = (int) (testTimeMs / scanPeriodMs);
//        int targetNotifications = Math.min(nNotifications, maxNotifications);
//
//        final ValueCache<Double> cache = new ValueCache<Double>(Double.class);
//        final Collector<Double> collector = new QueueCollector<Double>(cache);
//        counter = new AtomicInteger();
//        StatisticsAggregator aggregator = new StatisticsAggregator(collector);
//        SwingUtilities.invokeAndWait(new Runnable() {
//            @Override
//            public void run() {
//                pvStat = PV.createPv("My pv", DoubleStatistics.class);
//                pvStat.addPVValueChangeListener(new PVValueChangeListener() {
//
//                    @Override
//                    public void pvValueChanged() {
//                        counter.incrementAndGet();
//                    }
//                });
//            }
//        });
//        Notifier<DoubleStatistics> notifier = new Notifier<DoubleStatistics>(pvStat, aggregator, ExpressionLanguage.onSwingEDT());
//        Scanner.scan(notifier, scanPeriodMs);
//        PVRecipe connRecipe = new PVRecipe();
//        connRecipe.cache = cache;
//        connRecipe.collector = collector;
//        connRecipe.pvName = SimulationDataSource.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications);
//        SimulationDataSource.instance.connect(connRecipe);
//        Thread.sleep(testTimeMs + 100);
//        int actualNotification = counter.get();
//        if (Math.abs(actualNotification - targetNotifications) > 1) {
//            fail("Expected " + targetNotifications + " but got " + actualNotification);
//        }
//    }

}