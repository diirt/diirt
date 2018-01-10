/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

/**
 *
 * @author carcassi
 */
public class MathService {

    public static ServiceMethodDescription addMethod() {
        return new ServiceMethodDescription("add", "Adds two numbers.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
                return new AddServiceMethod(this, serviceDescription);
            }
        }.addArgument("arg1", "First argument", Number.class)
                .addArgument("arg2", "Second argument", Number.class)
                .addResult("result", "The sum of arg1 and arg2", Number.class);
    }

    public static ServiceMethodDescription multiplyMethod() {
        return new ServiceMethodDescription("multiply", "Multiplies two numbers.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDesccription) {
                return new MultiplyServiceMethod(this, serviceDesccription);
            }
        }.addArgument("arg1", "First argument", Number.class)
                .addArgument("arg2", "Second argument", Number.class)
                .addResult("result", "The product of arg1 and arg2", Number.class);
    }

    public static Service createMathService() {
        return new ServiceDescription("math", "Simple math service")
                .addServiceMethod(addMethod())
                .addServiceMethod(multiplyMethod())
                .createService();
    }

}
