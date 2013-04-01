/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.service;

import java.util.HashMap;
import java.util.Map;
import static org.epics.pvmanager.service.Service.namePattern;

/**
 *
 * @author carcassi
 */
public class ServiceMethodDescription {
    
    String name;
    Map<String, Class<?>> parameterTypes = new HashMap<>();
    Map<String, String> parameterDescriptions = new HashMap<>();
    Map<String, Class<?>> outputTypes = new HashMap<>();
    Map<String, String> outputDescriptions = new HashMap<>();

    public ServiceMethodDescription(String name) {
        this.name = name;
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
    }
    
    public ServiceMethodDescription addParameter(String name, String description, Class<?> type) {
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        parameterTypes.put(name, type);
        parameterDescriptions.put(name, description);
        return this;
    }
    
    public ServiceMethodDescription addOutput(String name, String description, Class<?> type) {
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        outputTypes.put(name, type);
        outputDescriptions.put(name, description);
        return this;
    }
}
