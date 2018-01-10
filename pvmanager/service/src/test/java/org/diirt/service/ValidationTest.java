/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import static org.diirt.service.MathService.addMethod;
import org.junit.Test;

/**
 *
 * @author asbarber
 */
public class ValidationTest {

    // Duplicate method name
    @Test (expected = IllegalArgumentException.class)
    public void validateParameters1() {
        new ServiceDescription("math", "Simple math service")
                .addServiceMethod(addMethod())
                .addServiceMethod(addMethod())
                .createService();
    }

    // Service method does not implement sync or async execution
    @Test (expected = RuntimeException.class)
    public void validateMethodImplementation1() {
        ServiceMethod x = new ServiceMethodImpl(
                addMethod(),
                new ServiceDescription("math", "Simple math service").addServiceMethod(addMethod()));
    }

    private static class ServiceMethodImpl extends ServiceMethod {

        public ServiceMethodImpl(ServiceMethodDescription smd, ServiceDescription sd) {
            super(smd, sd);
        }

    }
}
