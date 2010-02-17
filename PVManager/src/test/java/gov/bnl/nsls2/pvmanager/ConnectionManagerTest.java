/**
 * 
 */
package gov.bnl.nsls2.pvmanager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author shroffk
 * 
 */
public class ConnectionManagerTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ConnectionManager.useCAConnectionManager();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }
    private volatile PV<TypeDouble> pv;
    private AtomicInteger counter1;

    /**
     * checks writes to the collector
     * @throws Exception
     */
    @Test
    public void simpleTest() throws Exception {
        final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(TypeDouble.class);
        final Collector collector = new Collector(cache);
        counter1 = new AtomicInteger();

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
//				ConnectionManager.getInstance().connectToPV("pvk01", collector);
                pv = PV.createPv(TypeDouble.class);
                pv.setName("pvk01");
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        //System.out.println("New value " + pv.getValue());
                        counter1.incrementAndGet();
                    }
                });
                MonitorRecipe monRecipe = new MonitorRecipe();
                monRecipe.cache = cache;
                monRecipe.collector = collector;
                monRecipe.pvName = pv.getName();
                ConnectionManager.getInstance().monitor(monRecipe);
            }
        });
        Thread.sleep(5000);
        double[] doubleArray = collector.getData();
        for (double d : doubleArray) {
            System.out.println(d);
        }
        System.out.println(counter1.get());

    }

    /**
     * Checks writes and connection status updates
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
    @Test
    public void connectionStatusTest() throws InterruptedException, InvocationTargetException {
        final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(TypeDouble.class);
        final Collector collector = new Collector(cache);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
//				ConnectionManager.getInstance().connectToPV("pvk01", collector);
                pv = PV.createPv(TypeDouble.class);
                pv.setName("pvk01");
                pv.addPVValueChangeListener(new PVValueChangeListener() {

                    @Override
                    public void pvValueChanged() {
                        //
                        System.out.println("value changes " + pv.getName() + " " + pv.getState());
                    }
                });
                pv.addPropertyChangeListener(new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        // TODO Auto-generated method stub
                        // Testing this by starting and stopping the ioc.
                        System.out.println("detected a property change in PV.");
                    }
                });

                ConnectionRecipe connectionRecipe = new ConnectionRecipe();
                connectionRecipe.pv = pv;
                connectionRecipe.channelNames = Collections.singleton(pv.getName());
                ConnectionManager.getInstance().connect(connectionRecipe);
            }
        });
        Thread.sleep(50000);
        double[] doubleArray = collector.getData();
        for (double d : doubleArray) {
            System.out.println(d);
        }
    }
}
