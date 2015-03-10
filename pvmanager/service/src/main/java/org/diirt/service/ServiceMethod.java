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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    //TODO: this was changed to protected
    protected final ExecutorService executor;
    
    private final boolean asyncExecute;
    private final boolean syncExecute;
            
    /**
     * Creates a new service method with the given description. All properties
     * are copied out of the description, guaranteeing the immutability
     * of objects of this class. Nonetheless, service method descriptions
     * should not be reused for different services.
     * 
     * @param serviceMethodDescription the description of the service method, can't be null
     * @param serviceDescription
     */
    public ServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        this.name = serviceMethodDescription.name;
        this.description = serviceMethodDescription.description;
        this.arguments = Collections.unmodifiableList(new ArrayList<>(serviceMethodDescription.arguments));
        this.argumentMap = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.argumentMap));
        this.results = Collections.unmodifiableList(new ArrayList<>(serviceMethodDescription.results));
        this.resultMap = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.resultMap));
        this.executor = serviceDescription.executorService;
        
        //Determines whether the synchronous or asynchronous method was overridden
        boolean sync = false;
        try {
            sync = !ServiceMethod.class.equals(this.getClass().getMethod("syncExecImpl", Map.class).getDeclaringClass());
        } catch (NoSuchMethodException | SecurityException ex) {
            //TODO what happens here
            //Logger.getLogger(ServiceMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        boolean async = false;
        try {
            async = !ServiceMethod.class.equals(this.getClass().getMethod("asyncExecImpl", Map.class, Consumer.class, Consumer.class).getDeclaringClass());
        } catch (NoSuchMethodException | SecurityException ex) {
            //TODO what happens here
            //Logger.getLogger(ServiceMethod.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        syncExecute = sync;
        asyncExecute = async;
        if (!asyncExecute && !syncExecute){
            throw new RuntimeException("Must implement the method \"syncExecImpl\" or \"asyncExecImpl\".");
        }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append("(");
        
        boolean first = true;
        for (DataDescription argument : getArguments()) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(argument.getType().getSimpleName())
                    .append(" ")
                    .append(argument.getName());
        }
        sb.append(")");
        
        if (!getResults().isEmpty()) {
            sb.append(": ");
        }
        first = true;
        for (DataDescription result : getResults()) {
            if (!first) {
                sb.append(", ");
            } else {
                first = false;
            }
            sb.append(result.getType().getSimpleName())
                    .append(" ")
                    .append(result.getName());
        }

        return sb.toString();
    }
    
    //Implementation of the method (OVERRIDDEN BY SUBCLASS)
    //TODO made this protected rather than public
    //--------------------------------------------------------------------------    
    protected Map<String, Object> syncExecImpl(Map<String, Object> parameters) throws Exception {
        throw new RuntimeException("syncExecImpl was not overridden.");
    }
    
    protected void asyncExecImpl(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback){
        throw new RuntimeException("asyncExecImpl was not overridden.");        
    }
    //--------------------------------------------------------------------------
    
    //Wrap from sync <--> async calls
    //--------------------------------------------------------------------------
    private Map<String, Object> wrapAsSync(Map<String, Object> parameters) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Map<String, Object>> result = new AtomicReference<>();
        final AtomicReference<Exception> exception = new AtomicReference<>();

        Consumer<Map<String, Object>> callback = new Consumer<Map<String, Object>>() {
            @Override
            public void accept(Map<String, Object> newValue) {
                result.set(newValue);
                latch.countDown();
            }
        };
        Consumer<Exception> errorCallback = new Consumer<Exception>() {
            @Override
            public void accept(Exception newValue) {
                exception.set(newValue);
                latch.countDown();
            }
        };

        try {
            asyncExecImpl(parameters, callback, errorCallback);
        } catch (Exception ex) {
            errorCallback.accept(ex);
        }

        try {
            latch.await(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted", ex);
        }

        if (result.get() != null) {
            return result.get();
        }

        throw new RuntimeException("Failed", exception.get());
    }
    
    private void wrapAsAsync(ExecutorService executor, Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback){
        executor.submit(new Runnable(){
            @Override
            public void run() {
                try{
                    callback.accept(syncExecImpl(parameters));
                } catch (Exception ex){
                    errorCallback.accept(ex);
                }
            }
        });
    }
    //--------------------------------------------------------------------------
    
    //Execute the method
    //--------------------------------------------------------------------------
    public Map<String, Object> executeSync(Map<String, Object> parameters){
        validateParameters(parameters);
        
        if (syncExecute){
            try {
                return syncExecImpl(parameters);
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException("Method execution failed", ex);
            }
        }
        else if (asyncExecute){
            return wrapAsSync(parameters);
        }
        else{
            throw new RuntimeException("Unimplemented syncExecImpl or asyncExecImpl.");
        }
    }
    
    public void executeAsync(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback){
        
        //TODO: try to find a way to ensure the service method has an executor
        //descr -> SM descrs -> service methods -> service
        //service methods are IMMUTABLE
        validateParameters(parameters);
        
        if (asyncExecute){
            asyncExecImpl(parameters, callback, errorCallback);
        }
        else if (syncExecute){
            if (executor != null){
                wrapAsAsync(executor, parameters, callback, errorCallback);
            }
            else{
                throw new RuntimeException("Attempts asyncExecImpl with no executor.");
            }
        }
        else{
            throw new RuntimeException("Unimplemented syncExecImpl or asyncExecImpl.");
        }        
    }
    //--------------------------------------------------------------------------    
}