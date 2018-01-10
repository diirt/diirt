/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import org.diirt.service.ServiceDescription;

/**
 * The description setting attributes to construct an exec service.
 * <p>
 * Gathers elements to define the exec service, including:
 * <ul>
 * <li>Command to run the shell</li>
 * <li>Shell arguments</li>
 * <li>The results of each command</li>
 * </ul>
 *
 * @author carcassi
 */
public class ExecServiceDescription extends ServiceDescription {

    String shell = GenericExecServiceMethod.defaultShell();
    String shellArg = GenericExecServiceMethod.defaultShellArg();

    /**
     * Creates exec service description with the given service name and
     * description.
     *
     * @param name the name of the service; can't be null
     * @param description a brief description; can't be null
     */
    public ExecServiceDescription(String name, String description) {
        super(name, description);
    }

    /**
     * Adds the shell command for the service.
     *
     * @param shell command to open shell; can't be null
     * @return this description
     */
    public ExecServiceDescription shell(String shell) {
        if (shell == null) {
            throw new IllegalArgumentException("Shell must not be null");
        }

        this.shell = shell;
        return this;
    }

    /**
     * Adds the argument to the shell for the service.
     *
     * @param shellArg argument to apply to the terminal/shell; can't be null
     * @return this description
     */
    public ExecServiceDescription shellArg(String shellArg) {
        if (shellArg == null) {
            throw new IllegalArgumentException("Shell argument must not be null");
        }
        this.shellArg = shellArg;
        return this;
    }
}
