/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.util.config.Configuration;

/**
 * A service provider that can create a service instance for each file in
 * the configuration directory.
 * <p>
 * If the configuration directory does not exist, it is created. If the path given is
 * not a directory, an empty list is return and a warning message is logged.
 * By default, the configuration directory will be located in $DIIRT_HOME/services/SERVICE_PROVIDER_NAME.
 * <p>
 * This class provides the crawling of the configuration directory
 * and the logging of both service creation and errors.
 *
 * @author carcassi
 */
public abstract class AbstractFileServiceProvider implements ServiceProvider {

    private static final Logger log = Logger.getLogger(AbstractFileServiceProvider.class.getName());

    private final File directory;

    /**
     * Creates a new provider that looks for configuration files in the
     * given directory.
     *
     * @param directory the configuration directory
     */
    public AbstractFileServiceProvider(File directory) {
        this.directory = directory;
    }

    /**
     * Creates a new factory using the default configuration directory.
     */
    public AbstractFileServiceProvider() {
        this(null);
    }

    /**
     * The default location for the configuration. By default, it is
     * $DIIRT_HOME/services/SERVICE_PROVIDER_NAME.
     *
     * @return the configuration location
     */
    public File getDefaultConfigurationDirectory() {
        return new File(Configuration.getDirectory(), "services/" + getName());
    }

    /**
     * Creates a service from the given file.
     * <p>
     * Implementors of this method need not to care about logging and service registration.
     *
     * @param file a file in the configuration directory
     * @return the new service or null if no service corresponds to the file
     * @throws Exception if there is a problem creating the service from the file
     */
    public abstract Service createService(File file) throws Exception;

    /**
     * Creates additional services that are not read from files. This can be
     * useful in case the service provider has additional services that are
     * not user defined through configuration files.
     * <p>
     * Implementors need not to care about logging and service registration.
     *
     * @return a collection of services
     */
    public Collection<Service> additionalServices() {
        return Collections.emptyList();
    }

    /**
     * Creates all the service instances by crawling the configuration
     * directory, creating a service for each file found, and creating
     * the additional services.
     * <p>
     * Given that this method provides the error handling and logging, it
     * is declared final so that subclasses cannot accidently remove it.
     *
     * @return the new service instances
     */
    @Override
    public final Collection<Service> createServices() {
        List<Service> services = new ArrayList<>();
        File path = directory;
        if (path == null) {
            path = getDefaultConfigurationDirectory();
        }
        if (path.exists()) {
            if (path.isDirectory()) { // We have a configuration directory
                log.log(Level.CONFIG, "Loading {0} services from ''{1}''", new Object[] {getName(), path});
                for (File file : path.listFiles()) {
                    try {
                        Service service = createService(file);
                        if (service != null) {
                            services.add(service);
                            log.log(Level.CONFIG, "Created {0} service ''{1}'' from ''{2}''", new Object[] {getName(), service.getName(), file.getName()});
                        }
                    } catch (Exception ex) {
                        log.log(Level.WARNING, "Failed to create " + getName() + " service from '" + file + "'", ex);
                    }
                }
            } else { // The path is not a directory
                log.log(Level.WARNING, "Configuration path for {0} services ''{1}'' is not a directory", new Object[] {getName(), path});
            }
        } else { // The path does not exist
            path.mkdirs();
            log.log(Level.CONFIG, "Creating configuration path for {0} services at ''{1}''", new Object[] {getName(), path});
        }
        for (Service additionalService : additionalServices()) {
            services.add(additionalService);
            log.log(Level.CONFIG, "Created {0} service ''{1}''", new Object[] {getName(), additionalService.getName()});
        }

        log.log(Level.CONFIG, "Created {0} {1} services", new Object[] {services.size(), getName()});
        return services;
    }

}
