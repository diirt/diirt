/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sample;

import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import java.util.concurrent.Executor;
import org.epics.pvmanager.CompositeDataSource;
import static org.epics.pvmanager.ExpressionLanguage.*;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.PVReader;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.jca.JCADataSourceBuilder;
import org.epics.pvmanager.sim.SimulationDataSource;
import static org.epics.pvmanager.util.Executors.*;
import static org.epics.util.time.TimeDuration.*;

/**
 * Examples for basic configuration of pvmanager
 *
 * @author carcassi
 */
public class ConfigurationExamples {

    public void e1_pvManagerInCSS() {
        // In CSS, data sources are configured by adding the appropriate plug-ins,
        // so you **must not change the default configuration**.
        // If you are developing user interfaces in SWT, you will want to route the notifications on the SWT thread.

        // Import from here
        // import static org.csstudio.utility.pvmanager.ui.SWTUtil.*;
 
        // When creating a pv, remember to ask for notification on the SWT thread
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(swtThread()).maxRate(ofMillis(100));
    }

    public void e2_pvManagerInSwing() {
        // You will first need to configure the data sources yourself (see other examples).
        // You will want to route notification directly on the Event Dispatch Thread.
        // You can do this on a PV by PV basis, or you can change the default. 
        
        // Import from here
        // import static org.epics.pvmanager.util.Executors.*;
 
        // Route notification for this pv on the Swing EDT
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(swingEDT()).maxRate(ofMillis(100));

        // Or you can change the default
        PVManager.setDefaultNotificationExecutor(swingEDT());
    }

    public void e3_configuringJcaAsDefaultDataSource() {
        // Sets CAJ (pure java implementation) as the default data source,
        // monitoring both value and alarm changes
        PVManager.setDefaultDataSource(new JCADataSource());

        // For ultimate control, you can modify all the parameters, 
        // and even create the JCA context yourself
        Context jcaContext = null;
        PVManager.setDefaultDataSource(new JCADataSourceBuilder()
                .monitorMask(Monitor.VALUE | Monitor.ALARM)
                .jcaContext(jcaContext)
                .build());
        
        // For more options, check JCADataSource and JCADataSourceBuilder.
    }

    public void e4_configuringMultipleDataSources() {
        // Create a multiple data source, and add different data sources
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource("ca", new JCADataSource());
        composite.putDataSource("sim", new SimulationDataSource());

        // If no prefix is given to a channel, use JCA as default
        composite.setDefaultDataSource("ca");

        // Set the composite as the default
        PVManager.setDefaultDataSource(composite);
        
        // For more options, check CompositeDataSource.
    }
    
    public static Executor swtThread() {
        return null;
    }
}
