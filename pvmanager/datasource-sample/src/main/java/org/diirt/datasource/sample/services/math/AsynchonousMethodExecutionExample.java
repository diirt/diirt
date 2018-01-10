/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.ValueFactory;

/**
 * Locates a service method and executes it asynchronously (i.e. uses
 * callbacks when call is finished).
 *
 * @author asbarber
 */
public class AsynchonousMethodExecutionExample {

    /**
     * Sample execution of Math services.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        System.out.println("Locating service method...");
        ServiceMethod method = ServiceRegistry.getDefault().findServiceMethod("math/add");
        System.out.println("Service method found: " + method);

        System.out.println("Preparing arguments...");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("arg1", ValueFactory.newVDouble(1.0));
        arguments.put("arg2", ValueFactory.newVDouble(2.0));
        System.out.println("Arguments: " + arguments);

        System.out.println("Executing service...");
        method.executeAsync(arguments, result -> {
            System.out.println("Result: " + result);
        }, ex -> {
            ex.printStackTrace();
        });
    }

}
