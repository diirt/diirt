/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.service.Service;
import org.diirt.service.ServiceFactory;
import org.diirt.util.config.Configuration;

/**
 * A service factory that crawls a directory for xml files, and creates a JDBC
 * service from each of them.
 *
 * @author carcassi
 */
public class ExecServiceFactory implements ServiceFactory {

    private static final Logger log = Logger.getLogger(ExecServiceFactory.class.getName());

    private final File directory;
    private final boolean includeGenericExecService;

    /**
     * Creates a new factory that reads from the given directory.
     * <p>
     * If the directory does not exist, it simply returns an empty set.
     *
     * @param directory a directory
     * @param includeGenericExecService whether to include the general purpose
     * exec service
     */
    public ExecServiceFactory(File directory, boolean includeGenericExecService) {
        this.directory = directory;
        this.includeGenericExecService = includeGenericExecService;
    }

    /**
     * Creates a new factory using the default configuration directory.
     */
    public ExecServiceFactory() {
        this(new File(Configuration.getDirectory(), "services/exec"), true);
    }

    /**
     * Crawls the directory and creates JDBC services.
     * <p>
     * XML files that do not parse correctly are skipped.
     *
     * @return the created services
     */
    @Override
    public Collection<Service> createServices() {
        List<Service> services = new ArrayList<>();
        if (includeGenericExecService) {
            services.add(new GenericExecService());
        }
        if (directory.exists()) {
            if (directory.isDirectory()) { // We have a configuration directory
                log.log(Level.CONFIG, "Loading exec services from '{0}'", directory);
                for (File file : directory.listFiles()) {
                    if (file.getName().endsWith(".xml")) {
                        try {
                            ExecService service = ExecServices.createFromXml(new FileInputStream(file));
                            services.add(service);
                            log.log(Level.CONFIG, "Adding exec service '{0}'", service.getName());
                        } catch (Exception ex) {
                            log.log(Level.WARNING, "Failed to create exec service from '" + file + "'", ex);
                        }
                    }
                }
            } else { // The path is not a directory
                log.log(Level.WARNING, "Configuration path for exec services '{0}' is not a directory", directory);
            }
        } else { // The path does not exist
            directory.mkdirs();
            log.log(Level.CONFIG, "Creating configuration path for exec services at '{0}'", directory);
        }
        return services;
    }

}
