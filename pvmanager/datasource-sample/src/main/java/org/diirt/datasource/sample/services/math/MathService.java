/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.next.VNumber;

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

        }.addArgument("arg1", "First argument", VNumber.class)
                .addArgument("arg2", "Second argument", VNumber.class)
                .addResult("result", "The sum of arg1 and arg2", VNumber.class);
    }

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

    public static Service createMathService() {
        return new ServiceDescription("math", "Simple math service for VNumber")
                .addServiceMethod(addMethod())
                .addServiceMethod(multiplyMethod())
                .createService();
    }
}
