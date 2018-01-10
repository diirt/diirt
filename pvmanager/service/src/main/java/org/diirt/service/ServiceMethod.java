/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A request/response operation. Each method can be executed with a set
 * of parameters, and returns with a set of results. Methods are grouped into
 * services.
 * <p>
 * This class is immutable and the method execution is thread-safe. Each method
 * can either be executed synchronously or asynchronously. The method itself,
 * its arguments and its results are self-describing: they provide a name, description
 * and a type.
 * <p>
 * Implementations of this class must guarantee immutability and thread-safety.
 * They can choose whether to provide a synchronous implementation, an asynchronous implementation
 * or both. The framework will route or wrap the call appropriately (i.e if a
 * synchronous call is requested but only an asynchronous implementation is provided,
 * the call will wait until the asynchronous implementation returns).
 * Implementations are provided by overriding
 * {@link org.diirt.service.ServiceMethod#asyncExecImpl(java.util.Map, java.util.function.Consumer, java.util.function.Consumer) }
 * and/or {@link org.diirt.service.ServiceMethod#syncExecImpl(java.util.Map) }.
 * <p>
 * Service methods can have parameters that are set by the corresponding service
 * method description. Refer to the examples and wiki documentation for how
 * to do it.
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
    private final ExecutorService executor;
    private final List<DataDescription> arguments;
    private final Map<String, DataDescription> argumentMap;
    private final List<DataDescription> results;
    private final Map<String, DataDescription> resultMap;
    private final boolean asyncExecute;
    private final boolean syncExecute;

    /**
     * Creates a new service method with the given description. All properties
     * are copied out of the description, guaranteeing the immutability
     * of objects of this class.
     *
     * @param serviceMethodDescription the description of the service method; can't be null
     * @param serviceDescription the description of the service; can't be null
     */
    public ServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        if (serviceMethodDescription == null) {
            throw new IllegalArgumentException("Service method description should not be null.");
        }
        if (serviceDescription == null){
            throw new IllegalArgumentException("Service description should not be null.");
        }

        this.name = serviceMethodDescription.name;
        this.description = serviceMethodDescription.description;
        this.executor = serviceDescription.executorService;
        this.arguments = Collections.unmodifiableList(new ArrayList<>(serviceMethodDescription.arguments));
        this.results = Collections.unmodifiableList(new ArrayList<>(serviceMethodDescription.results));

        // Create a map of the names to argument/result
        this.argumentMap = Collections.unmodifiableMap(arguments.stream().collect(Collectors.toMap(DataDescription::getName, Function.identity())));
        this.resultMap = Collections.unmodifiableMap(results.stream().collect(Collectors.toMap(DataDescription::getName, Function.identity())));

        // Checks if the subclass overrides the synchronous implementation
        Method method = null;
        try {
            method = this.getClass().getMethod("syncExecImpl", Map.class);
        } catch (NoSuchMethodException | SecurityException ex) {
        }
        syncExecute = method != null;

        // Checks if the subclass overrides the asynchronous implementation
        method = null;
        try {
            method = this.getClass().getMethod("asyncExecImpl", Map.class, Consumer.class, Consumer.class);
        } catch (NoSuchMethodException | SecurityException ex) {
        }
        asyncExecute = method != null;

        // Validates that the subclass contains an implementation
        if (!asyncExecute && !syncExecute){
            throw new RuntimeException("Neither synchronous or asynchronous implementation was provided.");
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
     * The arguments, indexed by name.
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
     * The results, indexed by name.
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
            if (parameterValue != null &&!parameterType.isInstance(parameterValue)) {
                throw new IllegalArgumentException("ServiceMethod " + name + ": parameter " + parameterName + " should be of type " + parameterType.getSimpleName() + " but was " + parameterValue.getClass().getSimpleName());
            }
        }
    }

    /**
     * Provides a text representation for the method (i.e. <code>name(Type arg1, Type arg2): Type res1</code> ).
     *
     * @return the string representation; not null
     */
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

    /**
     * Synchronous implementation of the service method.
     * <p>
     * The implementation should return the value in case of success, or
     * throw an exception in case of failure.
     *
     * @param parameters the parameters for the method; not null
     * @return the result callback, for success; not null
     * @throws Exception the result callback, for failure
     */
    protected Map<String, Object> syncExecImpl(Map<String, Object> parameters) throws Exception {
        throw new RuntimeException("Sychronuous implementation not provided.");
    }

    /**
     * Asynchronous implementation of the service method.
     * <p>
     * The implementation should call only one callback, the callback or the
     * errorCallback. If the method throws an exception
     * it will be sent to the errorCallbak (i.e. you can let exceptions go through)
     * in which case no callback must be used.
     *
     * @param parameters the parameters for the method, already type checked
     * @param callback the result callback, for success; not null
     * @param errorCallback the error callback, for failures; can't be null
     */
    protected void asyncExecImpl(Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
        throw new RuntimeException("Asychronuous implementation not provided.");
    }

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

    private void wrapAsAsync(ExecutorService executor, Map<String, Object> parameters, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.accept(syncExecImpl(parameters));
                } catch (Exception ex) {
                    errorCallback.accept(ex);
                }
            }
        });
    }

    /**
     * Executes the service method with the given parameters, and waits for the
     * response (synchronous execution of this service method).
     *
     * @param arguments the parameters for the service; can't be null
     * @return the result callback, for success; can't be null
     * @throws RuntimeException if the method execution is unsuccessful
     */
    public Map<String, Object> executeSync(Map<String, Object> arguments) {
        if (arguments == null){
            throw new IllegalArgumentException("Parameters should not be null.");
        }

        validateParameters(arguments);

        if (syncExecute) {
            try {
                return syncExecImpl(arguments);
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException("Method execution failed", ex);
            }
        } else if (asyncExecute) {
            return wrapAsSync(arguments);
        } else {
            throw new RuntimeException("Neither synchronous or asynchronous implementation was provided.");
        }
    }

    /**
     * Executes the service method with the given parameters, the result of
     * which will be communicated through the callbacks (asynchronous execution
     * of this service method).
     *
     * @param arguments the parameters for the service; can't be null
     * @param callback the result callback, for success; can't be null
     * @param errorCallback the error callback, for failures; can't be null
     */
    public void executeAsync(Map<String, Object> arguments, Consumer<Map<String, Object>> callback, Consumer<Exception> errorCallback) {
        if (arguments == null){
            throw new IllegalArgumentException("Parameters should not be null.");
        }
        if (callback == null){
            throw new IllegalArgumentException("Callback should not be null.");
        }
        if (errorCallback == null){
            throw new IllegalArgumentException("Error callback should not be null.");
        }

        validateParameters(arguments);

        if (asyncExecute) {
            asyncExecImpl(arguments, callback, errorCallback);
        } else if (syncExecute) {
            wrapAsAsync(executor, arguments, callback, errorCallback);
        } else {
            throw new RuntimeException("Neither synchronous or asynchronous implementation was provided.");
        }
    }
}