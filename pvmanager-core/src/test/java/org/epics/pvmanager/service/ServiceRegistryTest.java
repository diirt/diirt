/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.service;

import org.epics.pvmanager.service.ServiceMethod;
import java.util.HashMap;
import java.util.Map;
import org.epics.pvmanager.WriteCache;
import org.epics.pvmanager.WriteFunction;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ServiceRegistryTest {
    
    public ServiceRegistryTest() {
    }

    @Test
    public void findService1() {
        ServiceRegistry registry = new ServiceRegistry();
        Service service = registry.findService("math");
        assertThat(service, nullValue());

        registry.registerService(new MathService());
        service = registry.findService("test");
        assertThat(service, nullValue());
        service = registry.findService("math");
        assertThat(service, instanceOf(MathService.class));
    }

    @Test
    public void findServiceMethod1() {
        ServiceRegistry registry = new ServiceRegistry();
        ServiceMethod serviceMethod = registry.findServiceMethod("math", "add");
        assertThat(serviceMethod, nullValue());

        registry.registerService(new MathService());
        serviceMethod = registry.findServiceMethod("test", "add");
        assertThat(serviceMethod, nullValue());
        serviceMethod = registry.findServiceMethod("math", "add");
        assertThat(serviceMethod, instanceOf(AddServiceMethod.class));
        serviceMethod = registry.findServiceMethod("math", "multiply");
        assertThat(serviceMethod, instanceOf(MultiplyServiceMethod.class));
        serviceMethod = registry.findServiceMethod("math", "invent");
        assertThat(serviceMethod, nullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findServiceMethod2() {
        ServiceRegistry registry = new ServiceRegistry();
        ServiceMethod serviceMethod = registry.findServiceMethod("math/add");
    }

    @Test
    public void findServiceMethod3() {
        ServiceRegistry registry = new ServiceRegistry();

        registry.registerService(new MathService());
        ServiceMethod serviceMethod = registry.findServiceMethod("math/add");
        assertThat(serviceMethod, instanceOf(AddServiceMethod.class));
        serviceMethod = registry.findServiceMethod("math/multiply");
        assertThat(serviceMethod, instanceOf(MultiplyServiceMethod.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findServiceMethod4() {
        ServiceRegistry registry = new ServiceRegistry();
        registry.registerService(new MathService());
        registry.findServiceMethod("math/invent");
    }
}