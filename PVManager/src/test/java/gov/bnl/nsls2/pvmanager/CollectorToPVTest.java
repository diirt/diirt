/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
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

    private volatile DoublePV pv;
    private Random rand = new Random();
    private AtomicInteger counter;

    @Test
    public void simpleTest() throws Exception {
        final Collector collector = new Collector();
        counter = new AtomicInteger();
        Aggregator aggregator = new SimpleAggregator(collector);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                pv = new DoublePV();
                pv.addPropertyChangeListener(new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        System.out.println("New value " + pv.getValue());
                        counter.incrementAndGet();
                    }
                });
            }
        });
        PullNotificator notificator = new PullNotificator(pv, aggregator);
        Scanner.scan(notificator, 40);
        new Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                collector.post(rand.nextGaussian());
                collector.post(rand.nextGaussian());
                collector.post(rand.nextGaussian());
                collector.post(rand.nextGaussian());
                collector.post(rand.nextGaussian());
            }
        }, 0, 200);
        Thread.sleep(5000);
        System.out.println(counter.get());
    }

}