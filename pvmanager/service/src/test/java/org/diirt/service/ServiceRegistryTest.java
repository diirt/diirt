/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

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

        registry.registerService(MathService.createMathService());
        service = registry.findService("test");
        assertThat(service, nullValue());
        service = registry.findService("math");
        assertThat(service, instanceOf(Service.class));
    }

    @Test
    public void findServiceMethod1() {
        ServiceRegistry registry = new ServiceRegistry();
        ServiceMethod serviceMethod = registry.findServiceMethod("math", "add");
        assertThat(serviceMethod, nullValue());

        registry.registerService(MathService.createMathService());
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

        registry.registerService(MathService.createMathService());
        ServiceMethod serviceMethod = registry.findServiceMethod("math/add");
        assertThat(serviceMethod, instanceOf(AddServiceMethod.class));
        serviceMethod = registry.findServiceMethod("math/multiply");
        assertThat(serviceMethod, instanceOf(MultiplyServiceMethod.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void findServiceMethod4() {
        ServiceRegistry registry = new ServiceRegistry();
        registry.registerService(MathService.createMathService());
        registry.findServiceMethod("math/invent");
    }

    @Test
    public void findServiceMethod5() {
        ServiceRegistry registry = new ServiceRegistry();
        ServiceMethod serviceMethod = registry.findServiceMethod("math", "add");
        assertThat(serviceMethod, nullValue());

        registry.registerServices(new ServiceProvider() {

            @Override
            public String getName() {
                return "test";
            }

            @Override
            public Collection<Service> createServices() {
                return Arrays.<Service>asList(MathService.createMathService());
            }
        });
        serviceMethod = registry.findServiceMethod("test", "add");
        assertThat(serviceMethod, nullValue());
        serviceMethod = registry.findServiceMethod("math", "add");
        assertThat(serviceMethod, instanceOf(AddServiceMethod.class));
        serviceMethod = registry.findServiceMethod("math", "multiply");
        assertThat(serviceMethod, instanceOf(MultiplyServiceMethod.class));
        serviceMethod = registry.findServiceMethod("math", "invent");
        assertThat(serviceMethod, nullValue());
    }

    @Test
    public void close1() {
        Service mockService = spy(MathService.createMathService());

        ServiceRegistry registry = new ServiceRegistry();
        registry.registerService(mockService);
        assertThat(registry.findService("math"), equalTo(mockService));
        registry.close();
        assertThat(registry.findService("math"), nullValue());

        verify(mockService).close();
    }
}