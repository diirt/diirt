/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.util.Map;
import org.diirt.service.ServiceMethod;
import org.diirt.vtype.VString;

/**
 * The implementation of an exec service method: for execution of shell
 * commands (both synchronously and asynchronously).
 *
 * @author carcassi
 */
class ExecServiceMethod extends ServiceMethod {

    private final String shell;
    private final String shellArg;
    private final String command;

    /**
     * Creates a new exec service method, for executing shell commands.
     *
     * @param serviceMethodDescription the description of the exec service
     * method; can't be null
     * @param serviceDescription the description of the exec service; can't be
     * null
     */
    ExecServiceMethod(ExecServiceMethodDescription serviceMethodDescription, ExecServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
        this.shell = serviceDescription.shell;
        this.shellArg = serviceDescription.shellArg;
        this.command = serviceMethodDescription.command;
    }

    @Override
    public Map<String, Object> syncExecImpl(final Map<String, Object> parameters) throws Exception {
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
                throw new IllegalArgumentException("Can't map parameter '" + name + "': was " + object);
            }
            expandedCommand = expandedCommand.replaceAll("#" + name + "#", value);
        }
        return GenericExecServiceMethod.syncExecuteCommand(parameters, shell, shellArg, expandedCommand);
    }
}
