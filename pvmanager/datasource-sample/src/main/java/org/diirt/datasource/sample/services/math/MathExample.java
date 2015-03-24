/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.diirt.service.ServiceMethod;
import org.diirt.vtype.next.Alarm;
import org.diirt.vtype.next.Display;
import org.diirt.vtype.next.Time;
import org.diirt.vtype.next.VDouble;
import org.diirt.vtype.next.VInt;
import org.diirt.vtype.next.VNumber;

/**
 * An example use of {@link org.diirt.service.Service} utilities. Performs
 * arithmetic on {@link VNumber}s.
 *
 * <p>
 * <b>Service Methods</b>: Actions (add, multiply) provided by the service.
 * <b>Service</b>: Links related service methods.
 *
 * @author asbarber
 */
public class MathExample {

    /**
     * Sample driver for Math services.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        System.out.println("SYNCHRONOUS EXECUTION: ");
        syncExecuteAdd();
        syncExecuteMultiply();

        System.out.println("ASYNCHRONOUS EXECUTION: ");
        asyncExecuteAdd();
        asyncExecuteMultiply();
    }

    /**
     * Demonstration of the {@link VNumber} addition service method. Executes
     * synchronously.
     */
    public static void syncExecuteAdd() {        
        // Obtains service method
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");

        // Creates data
        VNumber arg1 = VDouble.create(1, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(2, Alarm.noValue(), Time.now(), Display.none());
        
        // Formats data into correct parameters (two numbers)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 1 + 2");

        // Executes the service method (adds)
        Map<String, Object> returnValues = method.executeSync(parameters);

        // Obtains the service method result (sum)
        VNumber result = (VNumber) returnValues.get("result");
        System.out.println("result: " + result.getValue().doubleValue());

        // Expected output:
        // service: 1 + 2
        // result: 3.0     
    }

    /**
     * Demonstration of the {@link VNumber} multiplication service. Executes
     * synchronously.
     */
    public static void syncExecuteMultiply() {        
        // Obtains service method        
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("multiply");

        // Creates data
        VNumber arg1 = VDouble.create(2, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(3, Alarm.noValue(), Time.now(), Display.none());

        // Formats data into correct parameters (two numbers)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 2 * 3");

        // Executes the service method (adds)
        Map<String, Object> returnValues = method.executeSync(parameters);

        // Obtains the service method result (product)
        VNumber result = (VNumber) returnValues.get("result");
        System.out.println("result: " + result.getValue().doubleValue());

        // Expected output:
        // service: 2 * 3
        // result: 6.0
    }

    /**
     * Demonstration of the {@link VNumber} addition service. Executes
     * asynchronously.
     */
    public static void asyncExecuteAdd() {
        // Obtains service method
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");

        // Creates data
        VNumber arg1 = VDouble.create(1, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(2, Alarm.noValue(), Time.now(), Display.none());

        // Formats data into correct parameters (two numbers)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 1 + 2");

        // Obtains the service method result (sum) (ASYNCHRONOUS CALLBACK)
        Consumer<Map<String, Object>> callback = new Consumer<Map<String, Object>>() {

            @Override
            public void accept(Map<String, Object> returnValues) {
                VNumber result = (VNumber) returnValues.get("result");
                System.out.println("result: " + result.getValue().doubleValue());
            }

        };
        Consumer<Exception> errorCallback = System.out::println;

        // Executes the service method (add) asynchronously, callbacks handles what happens afterwards
        method.executeAsync(parameters, callback, errorCallback);

        // Expected output:
        // service: 1 + 2
        // result: 3.0          
    }

    /**
     * Demonstration of the {@link VNumber} multiplication service. Executes
     * asynchronously.
     */
    public static void asyncExecuteMultiply() {
        // Obtains service method        
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("multiply");

        // Creates data
        VNumber arg1 = VDouble.create(2, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(3, Alarm.noValue(), Time.now(), Display.none());

        // Formats data into correct parameters (two numbers)
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 2 * 3");

        // Obtains the service method result (product) (ASYNCHRONOUS CALLBACK)        
        Consumer<Map<String, Object>> callback = new Consumer<Map<String, Object>>() {

            @Override
            public void accept(Map<String, Object> returnValues) {
                // Obtains the mathematical result
                VNumber result = (VNumber) returnValues.get("result");
                System.out.println("result: " + result.getValue().doubleValue());
            }

        };
        Consumer<Exception> errorCallback = System.out::println;

        // Executes the service method (multiply) asynchronously, callbacks handles what happens afterwards
        method.executeAsync(parameters, callback, errorCallback);

        // Expected output:
        // service: 2 * 3
        // result: 6.0          
    }

}
