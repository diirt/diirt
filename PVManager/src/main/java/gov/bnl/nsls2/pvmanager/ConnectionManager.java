package gov.bnl.nsls2.pvmanager;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import javax.sound.midi.MidiDevice.Info;

import junit.framework.Assert;

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

    private static final ConnectionManager connManager = new ConnectionManager();
    private static volatile ConnectionManager instance = connManager;
    // Get the JCALibrary instance.
    private static JCALibrary jca = JCALibrary.getInstance();
    private static Context ctxt = null;

    // Executor to manage the updating of the collector.
    // Maintain a list of all the connections being managed.

    static ConnectionManager getInstance() {
	return instance;
    }

    static void useMockConnectionManager() {
	instance = MockConnectionManager.instance;
    }

    static void useCAConnectionManager() {
	instance = connManager;
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
		logger.severe(e.toString());
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

	private final WeakReference<PV<?>> pvRef;
	private final Channel channel;

	public ConnectionListenerImpl(PV<?> pv, Channel channel) {
	    this.pvRef = new WeakReference<PV<?>>(pv);
	    this.channel = channel;
	}

	@Override
	public void connectionChanged(ConnectionEvent ev) {
	    PV<?> pv;
	    logger.info("detected change in connection state.");
	    // TODO Auto-generated method stub
	    if (pvRef.get() != null) {
		pv = pvRef.get();
		logger.info("Detected a change in the connection status of pv "
			+ pv.getName() + " -status- " + ev.toString());
		pv.setState(PV.State.Connected); // just putting connected.
	    } else {
		// remove this connection listener.
		disconnect(channel, this);
	    }

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

	private final WeakReference<Collector> collector;
	private final TypeDouble value;
	private final Monitor monitor;

	// public MonitorListenerImpl(Collector collector, TypeDouble value) {
	// this.value = value;
	// this.collector = new WeakReference<Collector>(collector);
	// }

	public MonitorListenerImpl(Collector collector, TypeDouble value,
		Monitor monitor) {
	    this.value = value;
	    this.collector = new WeakReference<Collector>(collector);
	    this.monitor = monitor;
	}

	public synchronized void monitorChanged(MonitorEvent ev) {
	    synchronized (collector) {
		// TODO Auto-generated method stub
		// System.out.println(Thread.currentThread().getName());
		try {
		    Double newValue;
		    DBR_Double rawvalue = (DBR_Double) ev.getDBR().convert(
			    DBR_Double.TYPE);
		    newValue = rawvalue.getDoubleValue()[0];
		    value.setDouble(newValue);
		    if (collector.get() != null) {
			System.out.println("adding value "+newValue+" to collector ");
			Collector c = collector.get();
			c.collect();
		    } else {
			// stop this monitor since collector does not exist.
			logger.info("removing monitor");
			removeMonitor(monitor, this);
		    }
		} catch (CAStatusException e) {
		    // TODO Auto-generated catch block
		    logger.severe(e.toString());
		}
	    }
	}

	public synchronized void reset() {

	}
    }

    synchronized void connect(ConnectionRecipe connRecipe) {
	JCAinit();

	try {
	    Channel channel = ctxt.createChannel(connRecipe.channelNames
		    .iterator().next());
	    // ctxt.pendIO(1.0);
	    ConnectionListenerImpl connectionListenerImpl = new ConnectionListenerImpl(
		    connRecipe.pv, channel);
	    channel.addConnectionListener(connectionListenerImpl);
	} catch (Exception e) {
	    logger.severe(e.toString());
	}
    }

    synchronized void monitor(MonitorRecipe connRecipe) {
	if (connRecipe.cache.getType().equals(TypeDouble.class)) {
	    monitor(connRecipe.pvName, connRecipe.collector, TypeDouble.class
		    .cast(connRecipe.cache.getValue()));
	} else {
	    throw new UnsupportedOperationException("Type "
		    + connRecipe.cache.getType().getName()
		    + " is not yet supported");
	}
    }

    synchronized void monitor(String name, Collector collector,
	    TypeDouble typeDouble) {
	JCAinit();
	
	try {
	    Channel channel = ctxt.createChannel(name);
	    System.out.println(channel.getName());
	    // ctxt.pendIO(1.0);
	    // we assume it to be a double and move on....
	    Monitor monitor = channel.addMonitor(DBR_Double.TYPE, 1,
		    Monitor.VALUE);
	    MonitorListenerImpl monitorListenerImpl = new MonitorListenerImpl(
		    collector, typeDouble, monitor);
	    monitor.addMonitorListener(monitorListenerImpl);
	    logger.info("# of minitors = "
		    + monitor.getMonitorListeners().length);
	} catch (Exception e) {
	    logger.severe(e.toString());
	}
    }

    /**
     * Destroy the channel.
     */
    private synchronized void destroyChannel(Channel channel) {
	try {
	    channel.destroy();
	} catch (IllegalStateException e) {
	    // TODO Auto-generated catch block
	    logger.severe(e.toString());
	} catch (CAException e) {
	    // TODO Auto-generated catch block
	    logger.severe(e.toString());
	}
    }

    /**
     * Remove connection listeners.
     */
    private synchronized void disconnect(Channel channel,
	    ConnectionListener connectionListener) {
	try {
	    // TODO add check to determine when to destroy channel.
	    channel.removeConnectionListener(connectionListener);
	} catch (IllegalStateException e) {
	    // TODO Auto-generated catch block
	    logger.severe(e.toString());
	} catch (CAException e) {
	    // TODO Auto-generated catch block
	    logger.severe(e.toString());
	}
    }

    /**
     * Remove monitor.
     */
    private synchronized void removeMonitor(Monitor monitor,
	    MonitorListener monitorListener) {
	monitor.removeMonitorListener(monitorListener);
    }

    /**
     * Destroy JCA context.
     */
    private void destroy() {
	try {

	    // Destroy the context, check if never initialized.
	    if (ctxt != null)
		// Destroys all the channels associated with this context.
		ctxt.destroy();

	} catch (Throwable th) {
	    th.printStackTrace();
	}
    }
}
