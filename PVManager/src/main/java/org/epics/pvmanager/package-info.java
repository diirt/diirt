/*
 * Copyright 2010-11 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

/**
 * <p align="center"><img src="doc-files/PVManagerLogo150.png"/></p>
 * <div style="float: right; margin-top: -170px" id="contents"></div>
 * 
 * 
 * <h1>Contents</h1>
 * 
 * <h3>Configuration</h3>
 * <ol>
 *     <li><a href="#c1">Using PVManager in CSS</a></li>
 *     <li><a href="#c2">Using PVManager in Swing</a></li>
 *     <li><a href="#c3">Configuring JCA/CAJ as the default data source</a></li>
 *     <li><a href="#c4">Configuring multiple data sources with different prefixes</a></li>
 * </ol>
 * <h3>Basic usage</h3>
 * <ol>
 *     <li><a href="#b1">Reading a single channel</a></li>
 *     <li><a href="#b2">Writing a single channel asynchrnously</a></li>
 *     <li><a href="#b3">Writing a single channel synchrnously</a></li>
 *     <li><a href="#b4">Reading and writing a single channel</a></li>
 * </ol>
 * 
 * <h3 id="c1">Using PVManager in CSS</h3>
 * 
 * In CSS, data sources are configured by adding the appropriate plug-ins,
 * so you <b>must not change the default configuration</b>.
 * If you are developing user interfaces in SWT, you will want to route the notifications
 * on the SWT thread.
 * 
 * <pre>
 * // Import from here
 * import static org.csstudio.utility.pvmanager.ui.SWTUtil.*;
 * 
 * // When creating a pv, remember to ask for notification on the SWT thread
 * PVReader&lt;?&gt; pvReader = PVManager.read(...)..notifyOn(swtThread()).every(ms(100));
 * </pre>
 * 
 * <h3 id="c2">Using PVManager in Swing</h3>
 * 
 * You will first need to configure the data sources yourself (see other examples).
 * You will want to route notification directly on the Event Dispatch Thread. You can
 * do this on a PV by PV basis, or you can change the default.
 * 
 * <pre>
 * // Import from here
 * import static org.epics.pvmanager.util.Executors.*;
 * 
 * // Route notification for this pv on the Swing EDT
 * PVReader&lt;?&gt; pvReader = PVManager.read(...)..notifyOn(swingEDT()).every(ms(100));
 * 
 * // Or you can change the default
 * PVManager.setDefaultNotificationExecutor(swingEDT());
 * </pre>
 * 
 * <h3 id="c3">Configuring JCA/CAJ as the default data source</h3>
 * 
 * <pre>
 * // Sets CAJ (pure java implementation) as the default data source,
 * // monitoring both value and alarm changes
 * PVManager.setDefaultDataSource(new JCADataSource());
 * 
 * // For utltimate control, you can create the JCA context yourself
 * // and pass it to the data source
 * ...
 * Context jcaContext = ...
 * PVManager.setDefaultDataSource(new JCADataSource(jcaContext, Monitor.VALUE | Monitor.ALARM));
 * </pre>
 * 
 * For more options, check the constructors for JCADataSource.
 * <p>
 * 
 * <h3 id="c4">Configuring multiple data sources with different prefixes</h3>
 * 
 * <pre>
 * // Create a multiple data source, and add different data sources
 * CompositeDataSource composite = new CompositeDataSource();
 * composite.putDataSource("ca", new JCADataSource());
 * composite.putDataSource("sim", new SimulationDataSource());
 * 
 * // If no prefix is given to a channel, use JCA as default
 * composite.setDefaultDataSource("ca");
 * 
 * // Set the composite as the default
 * PVManager.setDefaultDataSource(composite);
 * </pre>
 * 
 * For more options, check the documentation for CompositeDataSource.
 * 
 * <h3 id="b1">Reading a single channel</h3>
 * 
 * <pre>
 * // Let's statically import so the code looks cleaner
 * import static org.epics.pvmanager.ExpressionLanguage.*;
 * import static org.epics.pvmanager.util.TimeDuration.*;
 * 
 * // Read channel "channelName" up to every 100 ms
 * PVReader&lt;Object&gt; pvReader = PVManager.read(channel("channelName")).every(ms(100));
 * pvReader.addPVReaderListener(new PVReaderListener() {
 *     public void pvChanged() {
 *         // Do something with each value
 *         Object newValue = pvReader.getValue();
 *         System.out.println(newValue);
 *     }
 * });
 * 
 * // Remember to close
 * pvReader.close();
 * </pre>
 * 
 * <h3 id="b2">Writing a single channel asynchronously</h3>
 * 
 * <pre>
 * PVWriter&lt;Object&gt; pvWriter = PVManager.write(channel("channelName")).async();
 * pvWriter.addPVWriterListener(new PVWriterListener() {
 *     public void pvWritten() {
 *         System.out.println("Write finished");
 *     }
 * });
 * // This will return right away, and the notification will be sent
 * // on the listener
 * pvWriter.write("New value");
 * 
 * // Remember to close
 * pvWriter.close();
 * </pre>
 * 
 * <h3 id="b3">Writing a single channel synchronously</h3>
 * 
 * <pre>
 * PVWriter&lt;Object&gt; pvWriter = PVManager.write(channel("channelName")).sync();
 * // This will block until the write is done
 * pvWriter.write("New value");
 * System.out.println("Write finished");
 * 
 * // Remember to close
 * pvWriter.close();
 * </pre>
 * 
 * <h3 id="b4">Reading and writing a single channel</h3>
 * 
 * <pre>
 * // A PV is both a PVReader and a PVWriter
 * PV&lt;Object, Object&gt; pv = PVManager.readAndWrite(channel("channelName")).asynchWriteAndReadEvery(ms(10));
 * pv.addPVReaderListener(new PVReaderListener() {
 *     public void pvChanged() {
 *         // Do something with each value
 *         Object newValue = pvReader.getValue();
 *         System.out.println(newValue);
 *     }
 * });
 * pv.write("New value");
 * 
 * // Remember to close
 * pv.close();
 * </pre>
 * 
 * <h1> Package description</h1>
 * 
 * This package contains all the basic compononents of the PVManager framework
 * and the basic support for the language to define the creation.
 * <p>
 * There are two distinct parts in the PVManager framework. The first part
 * includes all the elements that deal with data directly: read from various
 * sources ({@link ConnectionManager}), performing computation ({@link Function}),
 * collecting data ({@link Collector}), scanning at the UI rate ({@link Scanner})
 * and notify on appropriate threads ({@link PullNotificator}).
 * <p>
 * The second part consists of an expression language that allows to define
 * how to connect the first set of objects with each other. {@link PVExpression}
 * describes data as it's coming out at the network rate, {@link AggregatedPVExpression}
 * defines data at the scanning rate for the UI, and {@link PVExpressionLanguage}
 * defines static methods that define the operator in the expression language.
 * <p>
 * Users can extend both the first part (by extending support for different types,
 * providing different support for different data source or creating new computation
 * elements) and the second part (by extending the language to support other cases.
 * All support for data types is relegated to separate packages: you can use
 * the same style to extend the framework to your needs.
 */
package org.epics.pvmanager;

