/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.diirt.datasource.WriteFunction;

/**
 * A call that provides access to command/response type of communication for sources of
 * data or RPC-like services. Each method can be executed with a set of parameters, and returns with a set
 * of results. Methods are grouped into services.
 * <p>
 * This class is immutable and the method execution is thread-safe and asynchronous (non-blocking).
 * Subclasses must guarantee these properties.
 *
 * @author carcassi
 */
public abstract class ServiceMethod {
    private final String name;
    private final String description;
    private final Map<String, Class<?>> argumentTypes;
    private final Map<String, String> argumentDescriptions;
    private final Map<String, Class<?>> resultTypes;
    private final Map<String, String> resultDescriptions;

    /**
     * Creates a new service method with the given description. All properties
     * are copied out of the description, guaranteeing the immutability
     * of objects of this class. Nonetheless, service method descriptions
     * should not be reused for different services.
     * 
     * @param serviceMethodDescription the description of the service method, can't be null
     */
    public ServiceMethod(ServiceMethodDescription serviceMethodDescription) {
        this.name = serviceMethodDescription.name;
        this.description = serviceMethodDescription.description;
        this.argumentTypes = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.argumentTypes));
        this.argumentDescriptions = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.argumentDescriptions));
        this.resultTypes = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.resultTypes));
        this.resultDescriptions = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.resultDescriptions));
    }

    /**
     * A brief name for the service method. Used for service registration and lookup.
     * 
     * @return the service method name, can't be null
     */
    public final String getName() {
        return name;
    }

    /**
     * A description for the service method.
     * 
     * @return the service method description, can't be null
     */
    public final String getDescription() {
        return description;
    }

    public final Map<String, Class<?>> getArgumentTypes() {
        return argumentTypes;
    }

    public final Map<String, String> getArgumentDescriptions() {
        return argumentDescriptions;
    }

    public final Map<String, Class<?>> getResultTypes() {
        return resultTypes;
    }

    public final Map<String, String> getResultDescriptions() {
        return resultDescriptions;
    }
    
    void validateParameters(Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String parameterName = parameter.getKey();
            Object parameterValue = parameter.getValue();
            Class<?> parameterType = argumentTypes.get(parameterName);
            if (parameterType == null) {
                throw new IllegalArgumentException("ServiceMethod " + name + ": unexpected parameter " + parameterName);
            }
            if (!parameterType.isInstance(parameterValue)) {
                throw new IllegalArgumentException("ServiceMethod " + name + ": parameter " + parameterName + " should be of type " + parameterType.getSimpleName() + " but was " + parameterValue.getClass().getSimpleName());
            }
        }
    }
    
    public final void execute(Map<String, Object> parameters, WriteFunction<Map<String, Object>> callback, WriteFunction<Exception> errorCallback) {
        try {
            validateParameters(parameters);
            executeMethod(parameters, callback, errorCallback);
        } catch (Exception ex) {
            errorCallback.writeValue(ex);
        }
    }
    
    public abstract void executeMethod(Map<String, Object> parameters, WriteFunction<Map<String, Object>> callback, WriteFunction<Exception> errorCallback);
}
