/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import org.diirt.service.ServiceDescription;

/**
 * The description on how to construct an exec service.
 * <p>
 * This class encapsulate the description of a service, including:
 * <ul>
 * <li>A number of commands</li>
 * <li>The arguments for each command and how should they be mapped</li>
 * <li>The results of each command</li>
 * </ul>
 *
 * @author carcassi
 */
public class ExecServiceDescription extends ServiceDescription {

    String shell = GenericExecServiceMethod.defaultShell();
    String shellArg = GenericExecServiceMethod.defaultShellArg();

    /**
     * A new service description with the given service name and description.
     *
     * @param name the name of the service
     * @param description a brief description
     */
    public ExecServiceDescription(String name, String description) {
        super(name, description);
    }

    public ExecServiceDescription shell(String shell) {
        this.shell = shell;
        return this;
    }

    public ExecServiceDescription shellArg(String shellArg) {
        this.shellArg = shellArg;
        return this;
    }
}
