/**
 * 
 */
package gov.bnl.nsls2.pvmanager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
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

	private volatile PV<TypeDouble> pv, pvchannel;
	private Random rand = new Random();
	private AtomicInteger counter1, counter2;

	/**
	 * checks writes to the collector
	 * @throws Exception
	 */
	@Test 
	public void simpleTest() throws Exception {
		final Collector collector = new Collector();
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
                        System.out.println("New value " + pv.getValue());
                        counter1.incrementAndGet();
                    }
                });
				ConnectionManager.getInstance().createConnection(pv, collector);
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
	public void connectionStatusTest() throws InterruptedException, InvocationTargetException{
		final Collector collector = new Collector();
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
                        System.out.println("value changes " + pv.getName()+ " " +pv.getState());
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
				ConnectionManager.getInstance().createConnection(pv, collector);
			}
		});
		Thread.sleep(50000);
		double[] doubleArray = collector.getData();
		for (double d : doubleArray) {
			System.out.println(d);
		}
	}
	/*
	private volatile DoublePV pv1, pv2;
	
	private class testRun implements Runnable{
		DoublePV pv;
		Collector collector;
		String PVName;
		AtomicInteger counter;
		
		testRun(String PVName, DoublePV pv, Collector collector, AtomicInteger counter){
			this.PVName = PVName;
			this.pv = pv;
			this.collector = collector;
			this.counter = counter;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			DoublePV pvChannel = ConnectionManager.getInstance().getdoublePV(PVName, collector);
			pv.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					System.out.println("New value " + pv.getValue());
					counter.incrementAndGet();
				}
			});
		}
		
	}
	
	@Test
	public void simpleTest2() throws Exception {
		final Collector collector1 = new Collector();
		final Collector collector2 = new Collector();
		
		counter1 = new AtomicInteger();
		counter2 = new AtomicInteger();
		Aggregator aggregator1 = new SimpleAggregator(collector1);
		Aggregator aggregator2 = new SimpleAggregator(collector2);
		/*
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				pvchannel = ConnectionManager.getInstance().getdoublePV("pvk01", collector);
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
		*/
	/*
		pv1 = new DoublePV();
		SwingUtilities.invokeAndWait(new testRun("pvk01", pv1, collector1, counter1));
		PullNotificator notificator1 = new PullNotificator(pv1, aggregator1);
		Scanner.scan(notificator1, 400);
		
		pv2 = new DoublePV();
		SwingUtilities.invokeAndWait(new testRun("pvk02", pv2, collector2, counter2));
		PullNotificator notificator2 = new PullNotificator(pv2, aggregator2);
		Scanner.scan(notificator2, 800);
		
		Thread.sleep(5000);
		System.out.println(counter1.get());
		System.out.println(counter2.get());
		
	}
	*/

}
