/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.test;

import gov.bnl.pvmanager.MockDataSource;
import gov.bnl.pvmanager.PV;
import gov.bnl.pvmanager.PVManager;
import gov.bnl.pvmanager.PVValueChangeListener;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static gov.bnl.pvmanager.types.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class ReadingPVTest {

    public ReadingPVTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        PVManager.setConnectionManager(MockDataSource.mockData());
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
    public void testFluentApi() throws Exception {
        long testTimeMs = 5000;
        final double scanFrequency = 25;
        final long scanPeriodMs = 40;
        final long notificationPeriodMs = 1;
        final int samplesPerNotification = 5;
        final int nNotifications = (int) (testTimeMs / notificationPeriodMs);
        int maxNotifications = (int) (testTimeMs / scanPeriodMs);
        int targetNotifications = Math.min(nNotifications, maxNotifications);


        counter = new AtomicInteger();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pv = PVManager.read(doublePv("" + samplesPerNotification + "samples_every" + notificationPeriodMs + "ms_for" + nNotifications + "times")).atHz(scanFrequency);
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        counter.incrementAndGet();
                    }
                });
            }
        });
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get() - 1;
        if (Math.abs(actualNotification - targetNotifications) > 1) {
            fail("Expected " + targetNotifications + " but got " + actualNotification);
        }
    }

}