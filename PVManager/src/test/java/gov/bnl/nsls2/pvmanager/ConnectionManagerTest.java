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

    private volatile PV<TypeDouble> pv, pv1;

    /**
     * checks writes to the collector
     * 
     * @throws Exception
     */
    @Test
    public void simpleTest() throws Exception {
	final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(
		TypeDouble.class);
//	final Collector collector = new Collector(cache);
//	final Collector collector1 = new Collector(cache);
	final Collector collector = new Collector(cache);
	final Collector collector1 = new Collector(cache);

	SwingUtilities.invokeAndWait(new Runnable() {

	    @Override
	    public void run() {
		// ConnectionManager.getInstance().connectToPV("pvk01",
		// collector);
		pv = PV.createPv("pvk01", TypeDouble.class);

		MonitorRecipe monRecipe = new MonitorRecipe();
		monRecipe.cache = cache;
		monRecipe.collector = collector;
		monRecipe.pvName = pv.getName();
//		ConnectionManager.getInstance().monitor(monRecipe);
		ConnectionManager.getInstance().monitor("pvk01", collector, TypeDouble.class
			    .cast(cache.getValue()));
		ConnectionManager.getInstance().monitor("pvk01", collector1, TypeDouble.class
			    .cast(cache.getValue()));
	    }
	});
	Thread.sleep(5000);
	double[] doubleArray = collector.getData();
	logger.info("Completed write to collector Test1 : " + doubleArray.length);
	double[] doubleArray1 = collector1.getData();
	logger.info("Completed write to collector Test1 : " + doubleArray1.length);
//	for (double d : doubleArray) {
//	    logger.info("Simple 1:" + d);
//	}

    }
    
    @Test
    public void simpleTest2() throws Exception {
	System.gc();
	final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(
		TypeDouble.class);
//	final Collector collector2 = new Collector(cache);
	final Collector collector2 = new Collector(cache);

	SwingUtilities.invokeAndWait(new Runnable() {

	    @Override
	    public void run() {
		// ConnectionManager.getInstance().connectToPV("pvk01",
		// collector);
		pv1 = PV.createPv("pvk01", TypeDouble.class);

		MonitorRecipe monRecipe = new MonitorRecipe();
		monRecipe.cache = cache;
		monRecipe.collector = collector2;
		monRecipe.pvName = pv1.getName();
//		ConnectionManager.getInstance().monitor(monRecipe);
		ConnectionManager.getInstance().monitor("pvk01", collector2, TypeDouble.class
			    .cast(cache.getValue()));
	    }
	});
	Thread.sleep(5000);
	double[] doubleArray = collector2.getData();
	logger.info("Completed write to collector2 Test2 : " + doubleArray.length);
//	for (double d : doubleArray) {
//	    logger.info("Simple 2:" + d);
//	}

    }

    /**
     * Checks writes and connection status updates
     * 
     * @throws InvocationTargetException
     * @throws InterruptedException
     */
 //   @Test
    public void connectionStatusTest() throws InterruptedException,
	    InvocationTargetException {

	final ValueCache<TypeDouble> cache = new ValueCache<TypeDouble>(
		TypeDouble.class);
	final Collector collector = new Collector(cache);

	SwingUtilities.invokeAndWait(new Runnable() {

	    @Override
	    public void run() {
		// ConnectionManager.getInstance().connectToPV("pvk01",
		// collector);
		pv = PV.createPv("pvk01", TypeDouble.class);

		pv.addPropertyChangeListener(new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			// Testing this by starting and stopping the ioc.
			System.out.println("Test2: detected a property change in PV.");
		    }
		});
		ConnectionManager.useCAConnectionManager();
		// Connection
		ConnectionRecipe connectionRecipe = new ConnectionRecipe();
		connectionRecipe.pv = pv;
		connectionRecipe.channelNames = Collections.singleton(pv
			.getName());
		ConnectionManager.getInstance().connect(connectionRecipe);
	    }
	});
	Thread.sleep(5000);

	double[] doubleArray = collector.getData();
	for (double d : doubleArray) {
	    System.out.println(d);
	}

    }

    /**
     * Test the working from start to finish.
     * 
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
//    @Test
    public void PVtest() throws InterruptedException, InvocationTargetException {
	SwingUtilities.invokeAndWait(new Runnable() {

	    @Override
	    public void run() {
		// TODO Auto-generated method stub
		pv = PVManager.read(doublePv("pvk02")).atHz(50);
		pv.addPVValueChangeListener(new PVValueChangeListener() {
		    @Override
		    public void pvValueChanged() {
			logger.info("Test3a: " + pv.getName() + " state: " + pv.getState()
				+ " and value: " + pv.getValue().getDouble());
		    }
		});
		pv.addPVValueChangeListener(new PVValueChangeListener() {
		    @Override
		    public void pvValueChanged() {
			logger.info("Test3b: " + pv.getName() + " state: " + pv.getState()
				+ " and value: " + pv.getValue().getDouble());
		    }
		});
		pv.addPropertyChangeListener(new PropertyChangeListener() {

		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			// Testing this by starting and stopping the ioc.
			logger.severe("Test3: " + "detected a property change in PV.");
		    }
		});
	    }
	});
	Thread.sleep(5000);
    }

//    @Test
    public void multiThreadedPVTest() throws InterruptedException,
	    InvocationTargetException {

	class Test implements Runnable {
	    String name;
	    PV<TypeDouble> pvNew;

	    public Test(String name) {
		this.name = name;
	    }

	    public void run() {
		pvNew = PVManager.read(doublePv(name)).atHz(50);
		pvNew.addPVValueChangeListener(new PVValueChangeListener() {
		    @Override
		    public void pvValueChanged() {
			logger.info("Test4: " + Thread.currentThread().getName() + " "
				+ pvNew.getName() + " state: "
				+ pvNew.getState() + " and value: "
				+ pvNew.getValue().getDouble());
		    }
		});
		pvNew.addPropertyChangeListener(new PropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent evt) {
			// TODO Auto-generated method stub
			// Testing this by starting and stopping the ioc.
			logger.severe("Test4: " + "detected a property change in PV.");
		    }
		});
	    }
	}
	
	Thread t1 = new Thread(new Test("pvk02"));
	Thread t2 = new Thread(new Test("pvk03"));
	t1.start();
	t2.start();
	Thread.sleep(5000);
	t1.interrupt();
	t2.interrupt();
	t1.join();
	t2.join();
    }
}
