/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceRegistry;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VDouble;


/**
 * Locates a service method and executes it synchronously (i.e. waits for the
 * call to finish).
 *
 * @author asbarber
 */
public class SynchonousMethodExecutionExample {

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
        arguments.put("arg1", VDouble.of(1.0, Alarm.none(), Time.now(), Display.none()));
        arguments.put("arg2", VDouble.of(2.0, Alarm.none(), Time.now(), Display.none()));
        System.out.println("Arguments: " + arguments);

        System.out.println("Executing service...");
        Map<String, Object> result = method.executeSync(arguments);
        System.out.println("Result: " + result);
    }

}
