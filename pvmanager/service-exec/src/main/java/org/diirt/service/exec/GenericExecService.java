/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VString;
import org.diirt.vtype.VType;

/**
 * A utility class to create service method and services for general exec
 * services.
 *
 * @author carcassi
 */
public class GenericExecService {

    static final ExecutorService defaultExecutor = Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool("Exec services"));

    /**
     * Creates description for the "run" service method; which simply runs the
     * command on the shell and returns the output.
     *
     * @return service method description for exec
     */
    public static ServiceMethodDescription runMethod() {
        return new ServiceMethodDescription("run", "Executes a command.") {

            @Override
            public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
                return new GenericExecServiceMethod(this, serviceDescription);
            }

        }.addArgument("command", "The command", VString.class)
                .addResult("output", "The output of the command", VType.class);
    }

    /**
     * Creates a service for generic shell execution, containing the "run" service
     * method.
     *
     * @return generic exec service
     */
    public static Service createGenericExecService() {
        return new ServiceDescription("exec", "Command execution service")
                .addServiceMethod(runMethod())
                .executorService(defaultExecutor)
                .createService();
    }
}
