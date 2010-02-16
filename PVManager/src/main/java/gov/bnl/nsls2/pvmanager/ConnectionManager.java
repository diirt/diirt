package gov.bnl.nsls2.pvmanager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import gov.aps.jca.CAException;
import gov.aps.jca.CAStatus;
import gov.aps.jca.CAStatusException;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.JCALibrary;
import gov.aps.jca.Monitor;
import gov.aps.jca.dbr.DBR;
import gov.aps.jca.dbr.DBR_Double;
import gov.aps.jca.event.AccessRightsEvent;
import gov.aps.jca.event.AccessRightsListener;
import gov.aps.jca.event.ConnectionEvent;
import gov.aps.jca.event.ConnectionListener;
import gov.aps.jca.event.MonitorEvent;
import gov.aps.jca.event.MonitorListener;

class ConnectionManager {

    private static Logger logger = Logger.getLogger(ConnectionManager.class
	    .getName());

    private static ConnectionManager instance = new ConnectionManager();
    // Get the JCALibrary instance.
    private static JCALibrary jca = JCALibrary.getInstance();
    private static Context ctxt = null;

    // Executor to manage the updating of the collector.

    // Maintain a list of all the connections being managed.

    /*
     * When a connection manager is created we also create the jca context.
     */
    private ConnectionManager() {
    }

    static ConnectionManager getInstance() {
	return instance;
    }

    void removedoublePV() {

    }

    /*
     * This Metod will initialize the jca context.
     */
    private void JCAinit() {
	// Create a context which uses pure channel access java with HARDCODED
	// configuration
	// values.
	// TDB create the context reading some configuration file????
	if (ctxt == null) {
	    try {
		logger.info("Initializing the context.");
		ctxt = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);
	    } catch (CAException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    // Display basic information about the context.
	}
	ctxt.printInfo();
    }

    private final static ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * Implementation of ConnectionListener update the PV status based on the
     * connection event.
     */
    private class ConnectionListenerImpl implements ConnectionListener {

	final private PV pv;

	public ConnectionListenerImpl(PV pv) {
	    this.pv = pv;
	}

	@Override
	public void connectionChanged(ConnectionEvent ev) {
	    // TODO Auto-generated method stub
	    logger.info("Detected a change in the connection status of pv "
		    + pv.getName() + " -status- " + ev.toString());
	    pv.setName(pv.getName() + "my state changed");
	    pv.setState(PV.State.Connected); // just putting connected.
	}

    }

    /**
     * Implementation of AccessRightslistener throw an access error???? inform
     * the PV
     */
    private class AccessRightsListenerImpl implements AccessRightsListener {

	@Override
	public void accessRightsChanged(AccessRightsEvent arg0) {
	    // TODO Auto-generated method stub

	}

    }

    /**
     * Implementation of MonitorListener. Update the collector associated with
     * this PV.
     */
    private class MonitorListenerImpl implements MonitorListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.aps.jca.event.MonitorListener#monitorChanged(gov.aps.jca.event
	 * .MonitorEvent)
	 */

	private final Collector collector;

	// public volatile CAStatus status;
	// public volatile DBR response;

	public MonitorListenerImpl(Collector collector) {
	    this.collector = collector;
	}

	public synchronized void monitorChanged(MonitorEvent ev) {

	    // TODO Auto-generated method stub
	    // System.out.println(Thread.currentThread().getName());
	    try {
		Double value;
		DBR_Double rawvalue = (DBR_Double) ev.getDBR().convert(
			DBR_Double.TYPE);
		value = rawvalue.getDoubleValue()[0];
		// System.out
		// .println("Static conversion to double for pv "
		// + rawvalue.toString() + " = " + value);
		collector.post(value);
	    } catch (CAStatusException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	public synchronized void reset() {
	}
    }

    public synchronized void connectToPV(String name, Collector collector) {
	JCAinit();

	try {
	    Channel channel = ctxt.createChannel(name,
		    new ConnectionListener() {
			@Override
			public void connectionChanged(ConnectionEvent ev) {
			    // TODO Auto-generated method stub
			    System.out
				    .println("connection change event detected "
					    + ev.toString());
			}
		    });
	    channel.addMonitor(DBR_Double.TYPE, 1, Monitor.VALUE,
		    new MonitorListenerImpl(collector));

	} catch (Exception e) {
	    e.printStackTrace();
	}

	// at this point we have a context ready.
    }

    public synchronized void createConnection(PV pv, Collector collector) {
	JCAinit();

	try {
	    Channel channel = ctxt.createChannel(pv.getName(),
		    new ConnectionListenerImpl(pv));
	    // we assume it to be a double and move on....
	    channel.addMonitor(DBR_Double.TYPE, 1, Monitor.VALUE,
		    new MonitorListenerImpl(collector));

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public synchronized void dispose() {

    }

}
