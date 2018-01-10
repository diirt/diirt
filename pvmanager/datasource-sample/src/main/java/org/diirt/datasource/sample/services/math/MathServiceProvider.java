/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.Arrays;
import java.util.Collection;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.service.ServiceProvider;
import org.diirt.vtype.VNumber;

/**
 * A factory for generation of the Math sample service.
 *
 * @author asbarber
 */
public class MathServiceProvider implements ServiceProvider {

    /**
     * Creates the service method description for the addition service method.
     *
     * @return addition service method description
     */
    private static ServiceMethodDescription addMethod() {
        return new ServiceMethodDescription("add", "Adds two numbers.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
                return new AddServiceMethod(this, serviceDescription);
            }

        }.addArgument("arg1", "First argument", VNumber.class)
                .addArgument("arg2", "Second argument", VNumber.class)
                .addResult("result", "The sum of arg1 and arg2", VNumber.class);
    }

    /**
     * Creates the service method description for the multiplication service
     * method.
     *
     * @return multiplication service method description
     */
    public static ServiceMethodDescription multiplyMethod() {
        return new ServiceMethodDescription("multiply", "Multiplies two numbers.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
                return new MultiplyServiceMethod(this, serviceDescription);
            }

        }.addArgument("arg1", "First argument", VNumber.class)
                .addArgument("arg2", "Second argument", VNumber.class)
                .addResult("result", "The product of arg1 and arg2", VNumber.class);
    }

    /**
     * Creates the the math service.
     *
     * @return math service
     */
    private static Service createMathService() {
        return new ServiceDescription("math", "Simple math service for VNumber")
                .addServiceMethod(addMethod())
                .addServiceMethod(multiplyMethod())
                .createService();
    }

    @Override
    public String getName() {
        // The name of the service provider: this is not the name under which
        // the services are registered. This is mainly used for logging
        // purposes.
        return "mathProvider";
    }

    @Override
    public Collection<Service> createServices() {
        // Creates all the services that need to be registered
        return Arrays.asList(createMathService());
    }

}
