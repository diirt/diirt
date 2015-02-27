/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT All rights
 * reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import static org.diirt.service.Service.namePattern;

/**
 * A utility class to gather all the elements that define the service method.
 * <p>
 * This class is not thread-safe and is meant to be used right before the
 * creation of ServiceMethod objects.
 *
 * @author carcassi
 */
public class ServiceMethodDescription {

    // Access is package private so we don't even bother creating accessors for
    // these
    String name;
    String description;
    List<ServiceMethod.DataDescription> arguments = new ArrayList<>();
    List<ServiceMethod.DataDescription> results = new ArrayList<>();
    Map<String, ServiceMethod.DataDescription> argumentMap = new HashMap<>();
    Map<String, ServiceMethod.DataDescription> resultMap = new HashMap<>();
    ExecutorService executorService; //NEW
    AsyncImpl asyncImpl; //NEW
    SyncImpl syncImpl; //NEW

    /**
     * Creates a new service method description with the given name and
     * description, both of which are mandatory attributes of the service
     * methods.
     *
     * @param name the service method name, can't be null
     * @param description the service method description, can't be null
     */
    public ServiceMethodDescription(String name, String description) {
        this.name = name;
        this.description = description;
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
    }

    /**
     * Adds an argument for this method, with the given name, description and
     * type. The order in which arguments are added is retained as the preferred
     * order of arguments for the service method.
     *
     * @param name a short argument name; can't be null
     * @param description a meaningful description; can't be null
     * @param type the type of the argument
     * @return this description
     */
    public ServiceMethodDescription addArgument(String name, String description, Class<?> type) {
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        ServiceMethod.DataDescription dataDescription = new ServiceMethod.DataDescription(name, description, type);
        arguments.add(dataDescription);
        argumentMap.put(name, dataDescription);
        return this;
    }

    /**
     * Adds a result for this method, with the given name, description and type.
     * The order in which results are added is retained as the preferred order
     * of arguments for the service method.
     *
     * @param name a short result name; can't be null
     * @param description a meaningful description; can't be null
     * @param type the type of the result
     * @return this description
     */
    public ServiceMethodDescription addResult(String name, String description, Class<?> type) {
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        ServiceMethod.DataDescription dataDescription = new ServiceMethod.DataDescription(name, description, type);
        results.add(dataDescription);
        resultMap.put(name, dataDescription);
        return this;
    }

    //NEW
    public ServiceMethodDescription addExecutor(ExecutorService executor) {
        if (this.executorService != null) {
            throw new IllegalArgumentException("ExecutorService was already set");
        }
        this.executorService = executor;
        return this;
    }

    //NEW
    public ServiceMethodDescription addAsyncImplementation(AsyncImpl asyncImpl) {
        if (this.asyncImpl != null) {
            throw new IllegalArgumentException("AsyncImpl was already set");
        }
        this.asyncImpl = asyncImpl;
        return this;
    }

    //NEW
    public ServiceMethodDescription addSyncImplementation(SyncImpl syncImpl) {
        if (this.syncImpl != null) {
            throw new IllegalArgumentException("SyncImpl was already set");
        }
        this.syncImpl = syncImpl;
        return this;
    }
}
