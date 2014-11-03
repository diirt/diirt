/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.diirt.service.Service.namePattern;

/**
 *
 * @author carcassi
 */
public class ServiceMethodDescription {
    
    String name;
    String description;
    List<ServiceMethod.DataDescription> arguments = new ArrayList<>();
    List<ServiceMethod.DataDescription> results = new ArrayList<>();
    Map<String, ServiceMethod.DataDescription> argumentMap = new HashMap<>();
    Map<String, ServiceMethod.DataDescription> resultMap = new HashMap<>();

    public ServiceMethodDescription(String name, String description) {
        this.name = name;
        this.description = description;
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
    }
    
    public ServiceMethodDescription addArgument(String name, String description, Class<?> type) {
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        ServiceMethod.DataDescription dataDescription = new ServiceMethod.DataDescription(name, description, type);
        arguments.add(dataDescription);
        argumentMap.put(name, dataDescription);
        return this;
    }
    
    public ServiceMethodDescription addResult(String name, String description, Class<?> type) {
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        ServiceMethod.DataDescription dataDescription = new ServiceMethod.DataDescription(name, description, type);
        results.add(dataDescription);
        resultMap.put(name, dataDescription);
        return this;
    }
}
