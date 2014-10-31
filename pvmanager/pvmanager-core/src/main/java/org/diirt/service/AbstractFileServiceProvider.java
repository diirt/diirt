/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.service.Service;
import org.diirt.service.ServiceProvider;
import org.diirt.util.config.Configuration;

/**
 * A service factory that crawls a directory for xml files, and creates an exec
 * service from each of them.
 *
 * @author carcassi
 */
public abstract class AbstractFileServiceProvider implements ServiceProvider {

    private static final Logger log = Logger.getLogger(AbstractFileServiceProvider.class.getName());

    private final File directory;

    /**
     * Creates a new factory that reads from the given directory.
     * <p>
     * If the directory does not exist, it simply returns an empty set.
     *
     * @param directory a directory
     * exec service
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
     * $DIIRT_HOME/SERVICE_PROVIDER_NAME
     * 
     * @return the configuration location
     */
    public File getDefaultConfigurationDirectory() {
        return new File(Configuration.getDirectory(), "services/" + getName()); 
    }
    
    public abstract Service createService(File file) throws Exception;

    /**
     * Crawls the directory and creates services.
     *
     * @return the created services
     */
    @Override
    public Collection<Service> createServices() {
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
                            log.log(Level.CONFIG, "Adding {0} service ''{1}''", new Object[] {getName(), service.getName()});
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
        return services;
    }

}
