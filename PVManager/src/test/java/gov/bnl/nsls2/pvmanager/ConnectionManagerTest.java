/**
 * 
 */
package gov.bnl.nsls2.pvmanager;

import static gov.bnl.nsls2.pvmanager.PVExpressionLanguage.doublePv;

import gov.aps.jca.Channel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

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

    private static Logger logger = Logger.getLogger(ConnectionManager.class
	    .getName());

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

    private volatile PV<Double> pv, pv1;
    private AtomicInteger counter1;

    /**
     * checks writes to the collector
     * 
     * @throws Exception
     */
    @Test
    public void simpleTest() throws Exception {
	final ValueCache<Double> cache = new ValueCache<Double>(
		Double.class);
	final Collector<Double> collector = new Collector<Double>(cache);
	counter1 = new AtomicInteger();

	SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
//				ConnectionManager.getInstance().connectToPV("pvk01", collector);
                pv = PV.createPv("pvk01", Double.class);
                pv.addPVValueChangeListener(new PVValueChangeListener() {

		    @Override
		    public void pvValueChanged() {
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

    }

    /**
     * Checks writes and connection status updates
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
//    @Test
//    public void connectionStatusTest() throws InterruptedException,
//	    InvocationTargetException {
//	final ValueCache<Double> cache = new ValueCache<Double>(
//		Double.class);
//	final Collector<Double> collector = new Collector<Double>(cache);
//	final Channel channel;
//	SwingUtilities.invokeAndWait(new Runnable() {
//
//	    @Override
//	    public void run() {
//		// ConnectionManager.getInstance().connectToPV("pvk01",
//		// collector);
//		pv = PV.createPv("pvk01", Double.class);
//		// Connection
//		ConnectionRecipe connectionRecipe = new ConnectionRecipe();
//		connectionRecipe.pv = pv;
//		connectionRecipe.channelNames = Collections.singleton(pv
//			.getName());
//		ConnectionManager.getInstance().connect(connectionRecipe);
//	    }
//	});
//	Thread.sleep(5000);
//    }

    /**
     * Test the working from start to finish.
     * 
     * @throws InterruptedException
     */
    @Test
    public void PVtest() throws InterruptedException {
	pv = PVManager.read(doublePv("pvk01")).atHz(10);
	Thread.sleep(5000);
    }

}
