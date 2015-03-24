/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VNumber;

/**
 * Utility class for the creation of service descriptions and service method
 * descriptions for the Math Service example.
 *
 * @author carcassi
 * @author asbarber
 */
public class MathService {

    /**
     * Used to execute the service methods asynchronously.
     */
    private static final ExecutorService defaultExecutor = Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool("Math services"));
    
    /**
     * Creates the service method description for the addition service method.
     *
     * @return addition service method description
     */
    public static ServiceMethodDescription addMethod() {
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
    public static Service createMathService() {
        return new ServiceDescription("math", "Simple math service for VNumber")
                .addServiceMethod(addMethod())
                .addServiceMethod(multiplyMethod())
                .executorService(defaultExecutor)
                .createService();
    }
}
