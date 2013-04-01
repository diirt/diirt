/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author carcassi
 */
public class Service {
    static Pattern namePattern = Pattern.compile("[a-zA-Z_]\\w*");
    
    private String name;
    private Map<String, ServiceMethod> serviceMethods;

    public Service(String name, Map<String, ServiceMethod> serviceMethods) {
        this.name = name;
        if (!namePattern.matcher(name).matches()) {
            throw new IllegalArgumentException("Name must start by a letter and only consist of letters and numbers");
        }
        this.serviceMethods = Collections.unmodifiableMap(new HashMap<>(serviceMethods));
    }

    public String getName() {
        return name;
    }

    public Map<String, ServiceMethod> getServiceMethods() {
        return serviceMethods;
    }
    
}
