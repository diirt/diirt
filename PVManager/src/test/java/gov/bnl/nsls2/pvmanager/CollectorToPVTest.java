/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import gov.bnl.nsls2.pvmanager.types.DoubleStatistics;
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
        ConnectionManager.useMockConnectionManager();
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
        AverageAggregator aggregator = new AverageAggregator(collector);
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
        PullNotificator<Double> notificator = new PullNotificator<Double>(pv, aggregator, ExpressionLanguage.onSwingEDT());
        Scanner.scan(notificator, scanPeriodMs);
        MonitorRecipe connRecipe = new MonitorRecipe();
        connRecipe.cache = cache;
        connRecipe.collector = collector;
        connRecipe.pvName = MockConnectionManager.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications);
        MockConnectionManager.instance.monitor(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get();
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

        final ValueCache<Double> cache = new ValueCache<Double>(Double.class);
        final Collector<Double> collector = new QueueCollector<Double>(cache);
        counter = new AtomicInteger();
        AverageAggregator aggregator = new AverageAggregator(collector);
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
        PullNotificator<Double> notificator = new PullNotificator<Double>(pv, aggregator, ExpressionLanguage.onSwingEDT());
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

    private volatile PV<DoubleStatistics> pvStat;

    @Test
    public void testStatistics() throws Exception {
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
        StatisticsAggregator aggregator = new StatisticsAggregator(collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pvStat = PV.createPv("My pv", DoubleStatistics.class);
                pvStat.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        counter.incrementAndGet();
                    }
                });
            }
        });
        PullNotificator<DoubleStatistics> notificator = new PullNotificator<DoubleStatistics>(pvStat, aggregator, ExpressionLanguage.onSwingEDT());
        Scanner.scan(notificator, scanPeriodMs);
        MonitorRecipe connRecipe = new MonitorRecipe();
        connRecipe.cache = cache;
        connRecipe.collector = collector;
        connRecipe.pvName = MockConnectionManager.mockPVName(samplesPerNotification, notificationPeriodMs, nNotifications);
        MockConnectionManager.instance.monitor(connRecipe);
        Thread.sleep(testTimeMs + 100);
        int actualNotification = counter.get();
        if (Math.abs(actualNotification - targetNotifications) > 1) {
            fail("Expected " + targetNotifications + " but got " + actualNotification);
        }
    }

}