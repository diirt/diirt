/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.service.ServiceMethod;
import org.diirt.vtype.next.Alarm;
import org.diirt.vtype.next.Display;
import org.diirt.vtype.next.Time;
import org.diirt.vtype.next.VDouble;
import org.diirt.vtype.next.VInt;
import org.diirt.vtype.next.VNumber;

/**
 * An example use of {@link org.diirt.service.Service} utilities. This example
 * contains methods for arithmetic on {@link org.diirt.vtype.VNumber} data.
 *
 * <p>
 * <b>Service Methods</b>: The classes
 * {@link org.diirt.datasource.sample.services.math.AddServiceMethod} and
 * {@link org.diirt.datasource.sample.services.math.MultiplyServiceMethod} are
 * methods that represent the action of a service (to add, to multiply).
 *
 * <p>
 * <b>Service</b>: The class
 * {@link org.diirt.datasource.sample.services.math.MathService} registers the
 * service methods, or actions, to a service (allows execution of the service
 * methods).
 *
 * @author asbarber
 */
public class MathExample {

    /**
     * Sample driver for Math services.
     *
     * @param args ignored, command line arguments
     */
    public static void main(String[] args) {
//        for (String service : ServiceRegistry.getDefault().getRegisteredServiceNames()) {
//            System.out.println("- " + service);
//        }

        System.out.println("SYNCHRONOUS EXECUTION: ");
        syncExecuteAdd();
        syncExecuteMultiply();

        System.out.println("ASYNCHRONOUS EXECUTION: ");
        asyncExecuteAdd();
        asyncExecuteMultiply();
    }

    /**
     * Demonstration of the {@link VNumber} addition service. Executes
     * synchronously.
     */
    public static void syncExecuteAdd() {
        //Generate the service action to be called
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");

        //Generate the parameters to be supplied to the addition service
        VNumber arg1 = VDouble.create(1, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(2, Alarm.noValue(), Time.now(), Display.none());

        //Puts the arguments in a map to supply to the the service
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 1 + 2");

        //Executes the service and obtains all values returned
        Map<String, Object> returnValues = method.executeSync(parameters);

        //Obtains the mathematical result
        VNumber result = (VNumber) returnValues.get("result");
        System.out.println("result: " + result.getValue().doubleValue());

        //Expected output:
        //service: 1 + 2
        //result: 3.0     
    }

    /**
     * Demonstration of the {@link VNumber} multiplication service. Executes
     * synchronously.
     */
    public static void syncExecuteMultiply() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("multiply");

        //Generate the parameters to be supplied to the addition service
        VNumber arg1 = VDouble.create(2, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(3, Alarm.noValue(), Time.now(), Display.none());

        //Puts the arguments in a map to supply to the the service        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 2 * 3");

        //Executes the service and obtains all values returned
        Map<String, Object> returnValues = method.executeSync(parameters);

        //Obtains the mathematical results
        VNumber result = (VNumber) returnValues.get("result");
        System.out.println("result: " + result.getValue().doubleValue());

        //Expected output:
        //service: 2 * 3
        //result: 6.0
    }

    public static void asyncExecuteAdd() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("add");

        //Generate the parameters to be supplied to the addition service
        VNumber arg1 = VDouble.create(1, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(2, Alarm.noValue(), Time.now(), Display.none());

        //Puts the arguments in a map to supply to the the service
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 1 + 2");

        Consumer<Map<String, Object>> callback = new Consumer<Map<String, Object>>() {

            @Override
            public void accept(Map<String, Object> returnValues) {
                //Obtains the mathematical result
                VNumber result = (VNumber) returnValues.get("result");
                System.out.println("result: " + result.getValue().doubleValue());
            }

        };
        Consumer<Exception> errorCallback = new Consumer<Exception>() {

            @Override
            public void accept(Exception e) {
                Logger.getLogger(MathExample.class.getName()).log(Level.SEVERE, null, e);
            }

        };

        //Executes the service asynchronously, callbacks handle what happens afterwards
        method.executeAsync(parameters, callback, errorCallback);
        System.out.println("I might print out before the result.");

        //Expected output:
        //service: 1 + 2
        //result: 3.0          
    }
    
    public static void asyncExecuteMultiply() {
        ServiceMethod method = MathService.createMathService().getServiceMethods().get("multiply");

        //Generate the parameters to be supplied to the addition service
        VNumber arg1 = VDouble.create(2, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(3, Alarm.noValue(), Time.now(), Display.none());

        //Puts the arguments in a map to supply to the the service
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 2 * 3");

        Consumer<Map<String, Object>> callback = new Consumer<Map<String, Object>>() {

            @Override
            public void accept(Map<String, Object> returnValues) {
                //Obtains the mathematical result
                VNumber result = (VNumber) returnValues.get("result");
                System.out.println("result: " + result.getValue().doubleValue());
            }

        };
        Consumer<Exception> errorCallback = new Consumer<Exception>() {

            @Override
            public void accept(Exception e) {
                Logger.getLogger(MathExample.class.getName()).log(Level.SEVERE, null, e);
            }

        };

        //Executes the service asynchronously, callbacks handle what happens afterwards
        method.executeAsync(parameters, callback, errorCallback);
        System.out.println("I might print out before the result.");

        //Expected output:
        //service: 2 * 3
        //result: 6.0          
    }
    
}
