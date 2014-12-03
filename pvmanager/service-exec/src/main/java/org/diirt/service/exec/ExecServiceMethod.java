/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import org.diirt.service.ServiceMethod;
import org.diirt.vtype.VString;

/**
 * The implementation of an executor service method.
 *
 * @author carcassi
 */
class ExecServiceMethod extends ServiceMethod {
    
    private final ExecutorService executorService;
    private final String shell;
    private final String shellArg;
    private final String command;

    /**
     * Creates a new service method.
     * 
     * @param serviceMethodDescription a method description
     */
    ExecServiceMethod(ExecServiceMethodDescription serviceMethodDescription) {
        super(serviceMethodDescription.serviceMethodDescription);
        this.executorService = serviceMethodDescription.executorService;
        this.shell = serviceMethodDescription.shell;
        this.shellArg = serviceMethodDescription.shellArg;
        this.command = serviceMethodDescription.command;
    }

    @Override
    public void executeMethod(final Map<String, Object> parameters, final Consumer<Map<String, Object>> callback, final Consumer<Exception> errorCallback) {
        String expandedCommand = command;
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String name = entry.getKey();
            Object object = entry.getValue();
            String value = null;
            if (object instanceof VString) {
                value = ((VString) object).getValue();
            } else if (object == null) {
                value = "";
            } else {
                errorCallback.accept(new IllegalArgumentException("Can't map parameter '" + name + "': was " + object));
                return;
            }
            expandedCommand = expandedCommand.replaceAll("#" + name + "#", value);
        }
        GenericExecServiceMethod.executeCommand(parameters, callback, errorCallback, executorService, shell, shellArg, expandedCommand);
    }
}
