/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Group of request/response operations that share the same resources. Each service is a collection of one of more methods.
 * Each method can be executed with a set of parameters, and returns with a set
 * of results.
 * <p>
 * This class is immutable and therefore thread-safe. Subclasses are allowed
 * and should also be immutable.
 *
 * @author carcassi
 */
public class Service {
    static Pattern namePattern = Pattern.compile("[a-zA-Z_]\\w*");

    private final String name;
    private final String description;
    private final Map<String, ServiceMethod> serviceMethods;
    private final ExecutorService executorService;

    /**
     * Creates a new service given the description. All properties
     * are copied out of the description, guaranteeing the immutability
     * of objects of this class. Nonetheless, service descriptions
     * should not be reused for different services.
     *
     * @param serviceDescription the description of the service, can't be null
     */
    protected Service(ServiceDescription serviceDescription) {
        this.name = serviceDescription.name;
        this.description = serviceDescription.description;

        // If no executor is attached to the description, we create one
        if (serviceDescription.executorService == null){
            serviceDescription.executorService = Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool(this.name + " services"));
        }
        this.executorService = serviceDescription.executorService;

        this.serviceMethods = Collections.unmodifiableMap(new HashMap<>(serviceDescription.createServiceMethods()));
    }

    /**
     * A brief name for the service. Used for service registration and lookup.
     *
     * @return the service name, can't be null
     */
    public final String getName() {
        return name;
    }

    /**
     * A description for the service.
     *
     * @return the service description, can't be null
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Returns all the methods associated with this service.
     *
     * @return the service method, can't be null
     */
    public final Map<String, ServiceMethod> getServiceMethods() {
        return serviceMethods;
    }

    /**
     * Shutdown procedure for the service, closing resources (e.g. executor
     * service).
     */
    public void close(){
        this.executorService.shutdown();
    }
}
