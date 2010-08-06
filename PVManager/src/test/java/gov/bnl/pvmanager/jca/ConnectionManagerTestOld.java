/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package gov.bnl.pvmanager.jca;


import gov.bnl.pvmanager.Collector;
import gov.bnl.pvmanager.DataSource;
import gov.bnl.pvmanager.MonitorRecipe;
import gov.bnl.pvmanager.PV;
import gov.bnl.pvmanager.QueueCollector;
import gov.bnl.pvmanager.ValueCache;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author shroffk
 */
public class ConnectionManagerTestOld {

    private static Logger logger = Logger.getLogger(DataSource.class
	    .getName());

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
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
    //@Test
    public void simpleTest() throws Exception {
	final ValueCache<Double> cache = new ValueCache<Double>(
		Double.class);
	final Collector<Double> collector = new QueueCollector<Double>(cache);
	counter1 = new AtomicInteger();

	SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
		MonitorRecipe monRecipe = new MonitorRecipe();
		monRecipe.collector = collector;
                monRecipe.caches.put("pvk01", cache);
		JCASupport.jca().monitor(monRecipe);
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
//		// DataSource.getInstance().connectToPV("pvk01",
//		// collector);
//		pv = PV.createPv("pvk01", Double.class);
//		// Connection
//		ConnectionRecipe connectionRecipe = new ConnectionRecipe();
//		connectionRecipe.pv = pv;
//		connectionRecipe.channelNames = Collections.singleton(pv
//			.getName());
//		DataSource.getInstance().connect(connectionRecipe);
//	    }
//	});
//	Thread.sleep(5000);
//    }

//    /**
//     * Test the working from start to finish.
//     *
//     * @throws InterruptedException
//     */
//    @Test
//    public void PVtest() throws InterruptedException {
//	pv = PVManager.read(doublePv("pvk01")).atHz(10);
//	Thread.sleep(5000);
//    }

}
