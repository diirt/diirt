/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.ArrayList;
import java.util.List;
import static org.diirt.service.Service.namePattern;
import org.diirt.service.ServiceMethod.DataDescription;

/**
 * A utility class to gather all the elements that define the service method.
 * <p>
 * This class is not thread-safe and is meant to be used right before
 * the creation of {@link ServiceMethod} objects.
 *
 * @author carcassi
 */
public abstract class ServiceMethodDescription {

    // Access is package private so we don't even bother creating accessors for
    // these
    String name;
    String description;
    List<ServiceMethod.DataDescription> arguments = new ArrayList<>();
    List<ServiceMethod.DataDescription> results = new ArrayList<>();

    /**
     * Creates a new service method description with the given name and description,
     * both of which are mandatory attributes of the service methods.
     *
     * @param name the service method name, can't be null or empty
     * @param description the service method description, can't be null or empty
     */
    public ServiceMethodDescription(String name, String description) {
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
     * Adds an argument for this method, with the given name, description and type.
     * The order in which arguments are added is retained as the preferred order
     * of arguments for the service method.
     *
     * @param name a short argument name; can't be null or empty
     * @param description a meaningful description; can't be null or empty
     * @param type the type of the argument
     * @return this description
     */
    public ServiceMethodDescription addArgument(String name, String description, Class<?> type) {
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

        // Ensures the name makes sense (matches the pattern)
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }

        // Throws exception if parameter has a duplicate name of an argument
        if (arguments.stream().anyMatch((DataDescription t) -> {
            return t.getName().equals(name);
        })) {
            throw new IllegalArgumentException("Argument with name \'" + name + "\' already exists");
        }

        ServiceMethod.DataDescription dataDescription = new ServiceMethod.DataDescription(name, description, type);
        arguments.add(dataDescription);
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

        // Ensures the name makes sense (matches the pattern)
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }

        // Throws exception if parameter has a duplicate name of an argument
        if (results.stream().anyMatch((DataDescription t) -> {
            return t.getName().equals(name);
        })) {
            throw new IllegalArgumentException("Result with name \'" + name + "\' already exists");
        }

        ServiceMethod.DataDescription dataDescription = new ServiceMethod.DataDescription(name, description, type);
        results.add(dataDescription);
        return this;
    }

    /**
     * Creates a service method with the given description.
     * <p>
     * A client should implement this to return their service
     * method implementation.
     *
     * @param serviceDescription service description for the new service method
     * @return a new service method
     */
    public abstract ServiceMethod createServiceMethod(ServiceDescription serviceDescription);
}
