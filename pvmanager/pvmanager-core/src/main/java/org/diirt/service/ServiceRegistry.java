/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
public class ServiceRegistry {
    private static final Logger log = Logger.getLogger(ServiceRegistry.class.getName());
    private static final ServiceRegistry registry = new ServiceRegistry();

    public static ServiceRegistry getDefault() {
        return registry;
    }
    
    static {
        log.config("Fetching service providers");
        ServiceLoader<ServiceFactory> sl = ServiceLoader.load(ServiceFactory.class);
        int count = 0;
        for (ServiceFactory factory : sl) {
            log.log(Level.CONFIG, "Adding service provider ({0})", new Object[] {factory.getClass().getSimpleName()});
            registry.registerServices(factory);
            count++;
        }
        log.log(Level.CONFIG, "Found {0} service providers", count);
    }
    
    private final Map<String, Service> services = new ConcurrentHashMap<>();
    
    public void registerService(Service service) {
        log.log(Level.CONFIG, "Adding service {0} ({1})", new Object[] {service.getName(), service.getClass().getSimpleName()});
        services.put(service.getName(), service);
    }
    
    public void registerServices(ServiceFactory serviceFactory) {
        for (Service service : serviceFactory.createServices()) {
            registerService(service);
        }
    }
    
    public Set<String> listServices() {
        return Collections.unmodifiableSet(new HashSet<>(services.keySet()));
    }
    
    public Service findService(String name) {
        return services.get(name);
    }
    
    public ServiceMethod findServiceMethod(String serviceName, String methodName) {
        Service service = findService(serviceName);
        if (service == null) {
            return null;
        }
        return service.getServiceMethods().get(methodName);
    }
    
    public ServiceMethod findServiceMethod(String fullName) {
        String[] tokens = fullName.split("/");
        if (tokens.length != 2) {
            throw new IllegalArgumentException("Service method id must be \"service/method\"");
        }
        ServiceMethod method = findServiceMethod(tokens[0], tokens[1]);
        if (method == null) {
            throw new IllegalArgumentException("Service \"" + fullName + "\" not found");
        }
        return method;
    }
}
