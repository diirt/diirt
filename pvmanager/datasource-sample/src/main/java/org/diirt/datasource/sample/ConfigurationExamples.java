/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import gov.aps.jca.Context;
import gov.aps.jca.Monitor;
import java.util.concurrent.Executor;
import org.diirt.datasource.CompositeDataSource;
import org.diirt.datasource.CompositeDataSourceConfiguration;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.sim.SimulationDataSource;
import org.diirt.support.ca.JCADataSourceConfiguration;
import static org.diirt.util.concurrent.Executors.swingEDT;
import static org.diirt.javafx.util.Executors.*;
import org.diirt.support.ca.JCADataSourceProvider;
import static java.time.Duration.*;

/**
 * Examples for basic configuration of pvmanager
 *
 * @author carcassi
 */
public class ConfigurationExamples {

    public void datasourcesInSWT_CSSTudio() {
        // In CS-Studio, you should never change the default configuration programmatically
        // or you will create problems for other applications. All configuration
        // shoul be done on your own readers/writers.

        // If you are developing user interfaces in SWT, you will want to route the notifications on the SWT thread.

        // Import from here
        // import static org.csstudio.utility.pvmanager.ui.SWTUtil.*;

        // When creating a pv, remember to ask for notification on the SWT thread
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(swtThread()).maxRate(ofMillis(100));
    }

    public void datasourcesInSwing() {
        // When creating UIs in swing, you will need to route notification directly
        // on the Event Dispatch Thread.
        // You can do this on a PV by PV basis, or you can change the default if
        // you control the whole application.

        // Import from here
        // import static org.diirt.util.concurrent.Executors.*;

        // Route notification for this pv on the Swing EDT
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(swingEDT()).maxRate(ofMillis(100));

        // Or you can change the default
        PVManager.setDefaultNotificationExecutor(swingEDT());
    }

    public void datasourcesInJavaFX() {
        // When creating UIs in JavaFX, you will need to route notification directly
        // on the Application Thread.
        // You can do this on a PV by PV basis, or you can change the default if
        // you control the whole application.

        // Import from here
        // import static org.diirt.javafx.util.Executors.*;

        // Route notification for this pv on the Swing EDT
        PVReader<?> pvReader = PVManager.read(channel("test")).notifyOn(javaFXAT()).maxRate(ofMillis(100));

        // Or you can change the default
        PVManager.setDefaultNotificationExecutor(javaFXAT());
    }

    public void programmaticDataSourceConfiguration() {
        // The recommended method to configure data sources is through
        // the configuration files in DIIRT_HOME.

        // If is needed, you can still change the configuration through
        // the programmatic API. This can be useful for unit testing
        // or standalone applications. You should not do this in shared
        // environment, like CS-Studio.

        // This loads the configuration from DIIRT_HOME, and sets the
        // resulting JCADataSource as the only data source.
        // This can be used when creating an application that still
        // allows the users to provide their own configuration.
        PVManager.setDefaultDataSource(new JCADataSourceProvider().createInstance());

        // This uses the default configuration, ignorint the user configuration
        // and sets the resulting JCADataSource as the only data source.
        // This can be useful for unit testsing, where the code should be
        // in complete control of the settings.
        PVManager.setDefaultDataSource(new JCADataSourceConfiguration().create());

        // As a general rule: creating the data source from the provider, uses
        // the user configuration. Creating it from the configuration allows you
        // to create everything from scratch, ignoring user settings.

        // For ultimate control, you can modify all the parameters,
        // and even create the JCA context yourself
        Context jcaContext = null;
        PVManager.setDefaultDataSource(new JCADataSourceConfiguration()
                .monitorMask(Monitor.VALUE | Monitor.ALARM)
                .jcaContext(jcaContext).create());

        // For more options of this and other data sources,
        // check their javadocs.
    }

    public void programmaticCompositeDataSourceConfiguration() {
        // The recommended method to configure data sources is through
        // the configuration files in DIIRT_HOME.

        // If is needed, you can still change the configuration through
        // the programmatic API. This can be useful for unit testing
        // or standalone applications. You should not do this in shared
        // environment, like CS-Studio.

        // Create a composite data source, and add different data sources.
        // You can either add a DataSourceProvider or a DataSource.
        CompositeDataSource composite = new CompositeDataSource();
        composite.putDataSource(new JCADataSourceProvider());
        composite.putDataSource("sim", new SimulationDataSource());

        // If no prefix is given to a channel, use JCA as default
        composite.setConfiguration(new CompositeDataSourceConfiguration().defaultDataSource("ca"));

        // Set the composite as the default
        PVManager.setDefaultDataSource(composite);

        // For more options, check CompositeDataSource.
    }






















    public static Executor swtThread() {
        // Mock method to make the examples compile
        // This method will be in CSS
        return null;
    }
}
