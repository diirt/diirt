/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.epics.pvmanager.WriteFunction;
import static org.epics.pvmanager.service.Service.namePattern;

/**
 *
 * @author carcassi
 */
public abstract class ServiceMethod {
    private String name;
    private final Map<String, Class<?>> parameterTypes;
    private final Map<String, String> parameterDescriptions;
    private final Map<String, Class<?>> outputTypes;
    private final Map<String, String> outputDescriptions;

    public ServiceMethod(ServiceMethodDescription serviceMethodDescription) {
        this.name = serviceMethodDescription.name;
        this.parameterTypes = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.parameterTypes));
        this.parameterDescriptions = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.parameterDescriptions));
        this.outputTypes = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.outputTypes));
        this.outputDescriptions = Collections.unmodifiableMap(new HashMap<>(serviceMethodDescription.outputDescriptions));
    }

    public String getName() {
        return name;
    }

    public Map<String, Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public Map<String, String> getParameterDescriptions() {
        return parameterDescriptions;
    }

    public Map<String, Class<?>> getOutputTypes() {
        return outputTypes;
    }
    
    public abstract void execute(Map<String, Object> parameters, WriteFunction<Map<String, Object>> callback);
}
