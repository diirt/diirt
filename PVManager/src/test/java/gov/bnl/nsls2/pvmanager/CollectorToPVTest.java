/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

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

    private volatile PV<TypeDouble> pv;
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

        final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(TypeDouble.class);
        final Collector collector = new Collector(cache);
        counter = new AtomicInteger();
        AverageAggregator aggregator = new AverageAggregator(collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pv = PV.createPv(TypeDouble.class);
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        //System.out.println("New value " + pv.getValue().getDouble());
                        counter.incrementAndGet();
                    }
                });
            }
        });
        PullNotificator<TypeDouble> notificator = new PullNotificator<TypeDouble>(pv, aggregator);
        Scanner.scan(notificator, scanPeriodMs);
        MonitorRecipe connRecipe = new MonitorRecipe();
        connRecipe.cache = cache;
        connRecipe.collector = collector;
        connRecipe.pvName = MockConnectionManager.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications);
        MockConnectionManager.instance.monitor(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get() - 1;
        if (Math.abs(actualNotification - targetNotifications) > 1) {
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

        final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(TypeDouble.class);
        final Collector collector = new Collector(cache);
        counter = new AtomicInteger();
        AverageAggregator aggregator = new AverageAggregator(collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pv = PV.createPv(TypeDouble.class);
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        //System.out.println("New value " + pv.getValue().getDouble());
                        counter.incrementAndGet();
                    }
                });
            }
        });
        PullNotificator<TypeDouble> notificator = new PullNotificator<TypeDouble>(pv, aggregator);
        Scanner.scan(notificator, scanPeriodMs);
        MonitorRecipe connRecipe = new MonitorRecipe();
        connRecipe.cache = cache;
        connRecipe.collector = collector;
        connRecipe.pvName = MockConnectionManager.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications);
        MockConnectionManager.instance.monitor(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get() - 1;
        if (Math.abs(actualNotification - targetNotifications) > 1) {
            fail("Expected " + targetNotifications + " but got " + actualNotification);
        }
    }

    private volatile PV<TypeStatistics> pvStat;

    @Test
    public void testStatistics() throws Exception {
        long testTimeMs = 5000;
        long scanPeriodMs = 40;
        long notificationPeriodMs = 1;
        int samplesPerNotification = 5;
        final int nNotifications = (int) (testTimeMs / notificationPeriodMs);
        int maxNotifications = (int) (testTimeMs / scanPeriodMs);
        int targetNotifications = Math.min(nNotifications, maxNotifications);

        final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(TypeDouble.class);
        final Collector collector = new Collector(cache);
        counter = new AtomicInteger();
        StatisticsAggregator aggregator = new StatisticsAggregator(collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pvStat = PV.createPv(TypeStatistics.class);
                pvStat.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        //System.out.println("New value " + pvStat.getValue().getAverage() + " +/- " + pvStat.getValue().getStdDev());
                        counter.incrementAndGet();
                    }
                });
            }
        });
        PullNotificator<TypeStatistics> notificator = new PullNotificator<TypeStatistics>(pvStat, aggregator);
        Scanner.scan(notificator, scanPeriodMs);
        MonitorRecipe connRecipe = new MonitorRecipe();
        connRecipe.cache = cache;
        connRecipe.collector = collector;
        connRecipe.pvName = MockConnectionManager.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications);
        MockConnectionManager.instance.monitor(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get() - 1;
        if (Math.abs(actualNotification - targetNotifications) > 1) {
            fail("Expected " + targetNotifications + " but got " + actualNotification);
        }
    }

}