/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT All rights
 * reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
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
        executeAdd();
        executeMultiply();
    }

    /**
     * Demonstration of the addition service. The service accepts {@link VNumber
     * } arguments and stores the result (addition of the values) in a
     * {@link ValueCache}.
     */
    public static void executeAdd() {
        //Generate the service action to be called
        ServiceMethod method = new AddServiceMethod();

        //Generate the parameters to be supplied to the addition service
        VNumber arg1 = VDouble.create(1, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(2, Alarm.noValue(), Time.now(), Display.none());

        //Puts the arguments in a map to supply to the the service
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 1 + 2");

        //Executes the service
        //Stores the results in a simple object
        ValueCache<Map<String, Object>> cache = new ValueCache<>();
        ValueCache<Exception> exceptionCache = new ValueCache<>();
        method.execute(parameters, cache, exceptionCache);

        //Obtains the result
        VNumber result = (VNumber) cache.get().get("result");
        System.out.println("result: " + result.getValue().doubleValue());

        //Expected output:
        //service: 1 + 2
        //result: 3.0     
    }

    /**
     * Demonstration of the multiplication service. The service accepts 
     * {@link VNumber } arguments and stores the result (product of the values)
     * in a {@link ValueCache}.
     */
    public static void executeMultiply() {
        //Generate the service action to be called
        ServiceMethod method = new MultiplyServiceMethod();

        //Generate the parameters to be supplied to the addition service
        VNumber arg1 = VDouble.create(2, Alarm.noValue(), Time.now(), Display.none());
        VNumber arg2 = VInt.create(3, Alarm.noValue(), Time.now(), Display.none());

        //Puts the arguments in a map to supply to the the service        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("arg1", arg1);
        parameters.put("arg2", arg2);
        System.out.println("service: 2 * 3");

        //Executes the service
        //Stores the results in a simple object        
        ValueCache<Map<String, Object>> cache = new ValueCache<>();
        ValueCache<Exception> exceptionCache = new ValueCache<>();
        method.execute(parameters, cache, exceptionCache);

        //Obtains the results
        VNumber result = (VNumber) cache.get().get("result");
        System.out.println("result: " + result.getValue().doubleValue());

        //Expected output:
        //service: 2 * 3
        //result: 6.0
    }
}
