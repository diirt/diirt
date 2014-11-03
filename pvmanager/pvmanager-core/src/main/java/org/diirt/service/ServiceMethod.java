/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
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
    
    /**
     * The full description of an argument of a result of the service.
     */
    public final static class DataDescription {
        private final String name;
        private final String description;
        private final Class<?> type;

        DataDescription(String name, String description, Class<?> type) {
            this.name = name;
            this.description = description;
            this.type = type;
        }

        /**
         * The name of the argument/result.
         * 
         * @return a short name; not null
         */
        public String getName() {
            return name;
        }

        /**
         * The description of the argument/result.
         * 
         * @return a meaningful description; not null
         */
        public String getDescription() {
            return description;
        }

        /**
         * The type of the argument/result.
         * 
         * @return the type; not null
         */
        public Class<?> getType() {
            return type;
        }
        
    }
    private final String name;
    private final String description;
    private final List<DataDescription> arguments;
    private final Map<String, DataDescription> argumentMap;
    private final List<DataDescription> results;
    private final Map<String, DataDescription> resultMap;

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
        this.arguments = Collections.unmodifiableList(new ArrayList<>(serviceMethodDescription.arguments));
        this.argumentMap = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.argumentMap));
        this.results = Collections.unmodifiableList(new ArrayList<>(serviceMethodDescription.results));
        this.resultMap = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.resultMap));
    }

    /**
     * A brief name for the service method. Used for service registration and lookup.
     * 
     * @return the service method name; not null
     */
    public final String getName() {
        return name;
    }

    /**
     * A description for the service method.
     * 
     * @return the service method description; not null
     */
    public final String getDescription() {
        return description;
    }

    /**
     * The list of arguments, with their name, description and type.
     * 
     * @return the arguments for this method; not null
     */
    public List<DataDescription> getArguments() {
        return arguments;
    }

    /**
     * The arguments, indexed by name
     * 
     * @return the arguments for this method; not null
     */
    public Map<String, DataDescription> getArgumentMap() {
        return argumentMap;
    }

    /**
     * The list of results, with their name, description and type.
     * 
     * @return the results for this method; not null
     */
    public List<DataDescription> getResults() {
        return results;
    }

    /**
     * The results, indexed by name
     * 
     * @return the results for this method; not null
     */
    public Map<String, DataDescription> getResultMap() {
        return resultMap;
    }
    
    void validateParameters(Map<String, Object> parameters) {
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            String parameterName = parameter.getKey();
            Object parameterValue = parameter.getValue();
            Class<?> parameterType = argumentMap.get(parameterName).getType();
            if (parameterType == null) {
                throw new IllegalArgumentException("ServiceMethod " + name + ": unexpected parameter " + parameterName);
            }
            if (!parameterType.isInstance(parameterValue)) {
                throw new IllegalArgumentException("ServiceMethod " + name + ": parameter " + parameterName + " should be of type " + parameterType.getSimpleName() + " but was " + parameterValue.getClass().getSimpleName());
            }
        }
    }
    
    /**
     * Executes the service method with the given parameters, the result of which
     * will be communicated through the callbacks.
     * <p>
     * This method validates the parameters and then calls the implementation method.
     * 
     * @param parameters the parameters for the service; can't be  null
     * @param callback the result callback, for success; can't be null
     * @param errorCallback the error callback, for failures; can't be null
     */
    public final void execute(Map<String, Object> parameters, WriteFunction<Map<String, Object>> callback, WriteFunction<Exception> errorCallback) {
        try {
            validateParameters(parameters);
            executeMethod(parameters, callback, errorCallback);
        } catch (Exception ex) {
            errorCallback.writeValue(ex);
        }
    }
    
    /**
     * Executes the service methods with the given parameters, and waits for the
     * response. This is a blocking call and should be used with caution.
     * 
     * @param parameters the parameters for the service; can't be  null
     * @return the result callback, for success; can't be null
     */
    public final Map<String, Object> syncExecute(Map<String, Object> parameters) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Map<String, Object>> result = new AtomicReference<>();
        final AtomicReference<Exception> exception = new AtomicReference<>();
        execute(parameters, new WriteFunction<Map<String, Object>>() {

            @Override
            public void writeValue(Map<String, Object> newValue) {
                result.set(newValue);
                latch.countDown();
            }
        }, new WriteFunction<Exception>() {

            @Override
            public void writeValue(Exception newValue) {
                exception.set(newValue);
                latch.countDown();
            }
        });
        
        try {
            latch.await();
        } catch (InterruptedException ex) {
            throw new RuntimeException("Interrupted", ex);
        }
        
        if (result.get() != null) {
            return result.get();
        }
        
        throw new RuntimeException("Failed", exception.get());
    }

    /**
     * Implementation of the service method.
     * <p>
     * The call should be implemented asynchronously, only one callback
     * should be called once with the result. If the method throws an exception,
     * this will be sent to the errorCallbak, you can leave exceptions go through
     * in which case no callback should be used.
     * 
     * @param parameters the parameters for the called, already type checked
     * @param callback the result callback, for success; not null
     * @param errorCallback the error callback, for failures; not null
     */
    protected abstract void executeMethod(Map<String, Object> parameters, WriteFunction<Map<String, Object>> callback, WriteFunction<Exception> errorCallback);
}
