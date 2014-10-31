/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Provides access to command/response type of communication for sources of
 * data or RPC-like services. Each service is a collection of one of more methods.
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

    /**
     * Creates a new service given the description.
     * 
     * @param serviceDescription the service description
     */
    public Service(ServiceDescription serviceDescription) {
        this.name = serviceDescription.name;
        this.description = serviceDescription.description;
        this.serviceMethods = Collections.unmodifiableMap(new HashMap<>(serviceDescription.serviceMethods));
    }

    /**
     * Returns a brief name for the service. Used for registering and looking the
     * service up.
     * 
     * @return the service name, can't be null
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns a description for the service.
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
    
}
