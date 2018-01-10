/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import static org.diirt.service.Service.namePattern;

/**
 * A utility class to gather all the elements that define the service.
 * <p>
 * This class is not thread-safe and is meant to be used right before the
 * creation of {@link Service} objects.
 *
 * @author carcassi
 */
public class ServiceDescription {

    // Access is package private so we don't even bother creating accessors for
    // these
    String name;
    String description;
    List<ServiceMethodDescription> serviceMethodDescriptions = new ArrayList<>();
    ExecutorService executorService;

    /**
     * Creates a new service description with the given name and description,
     * both of which are mandatory attributes of the service.
     *
     * @param name the service name, can't be null
     * @param description the service description; can't be null
     */
    public ServiceDescription(String name, String description) {
        // Validate the parameters (non-null and non-empty)
        if (name == null){
            throw new NullPointerException("Name must not be null");
        }
        if (description == null){
            throw new NullPointerException("Description must not be null");
        }
        if (name.isEmpty()){
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (description.isEmpty()){
            throw new IllegalArgumentException("Description must not be empty");
        }

        this.name = name;
        this.description = description;
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
    }

    /**
     * Adds the given method to this service description. The name of the
     * given service method must be unique to the service (i.e. can't match the
     * name of an already added service method).
     *
     * @param serviceMethodDescription a service method description, can't be null
     * @return this description
     */
    public ServiceDescription addServiceMethod(ServiceMethodDescription serviceMethodDescription) {
        // Validate parameters (non-null, non-duplicate method)
        if (serviceMethodDescription == null) {
            throw new NullPointerException("ServiceMethodDescription must not be null");
        }
        // Throws exception if parameter has a duplicate name of a service method
        if (serviceMethodDescriptions.stream()
                .anyMatch(otherMethodDescription -> otherMethodDescription.name.equals(serviceMethodDescription.name))) {
            throw new IllegalArgumentException("ServiceMethodDescription with name \'" + serviceMethodDescription.name + "\' already exists");
        }

        serviceMethodDescriptions.add(serviceMethodDescription);

        return this;
    }

    /**
     * Determines the executor to be used by the service. The executor
     * can't be changed once set.
     * <p>
     * The executor is used for wrapping synchronous calls into asynchronous
     * calls.
     *
     * @param executor an executor service, can't be null
     * @return this description
     */
    public ServiceDescription executorService(ExecutorService executor) {
        // Validate parameters (non-null, not already set)
        if (executor == null) {
            throw new NullPointerException("ExecutorService must not be null");
        }
        if (this.executorService != null) {
            throw new IllegalArgumentException("ExecutorService was already set");
        }

        this.executorService = executor;
        return this;
    }

    /**
     * Creates the service method instances that belongs to this service.
     *
     * @return the new service methods indexed by method name
     */
    public final Map<String, ServiceMethod> createServiceMethods() {
        Map<String, ServiceMethod> map = new HashMap<>();
        for (ServiceMethodDescription serviceMethodDescription : serviceMethodDescriptions) {
            ServiceMethod method = serviceMethodDescription.createServiceMethod(this);
            map.put(method.getName(), method);
        }
        return map;
    }

    /**
     * Creates a service with the given description.
     * <p>
     * A client may override this to return their service
     * implementation.
     *
     * @return a service based off of this description state
     */
    public Service createService() {
        return new Service(this);
    }
}
