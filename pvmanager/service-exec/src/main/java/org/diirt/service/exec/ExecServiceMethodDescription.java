/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.util.ArrayList;
import java.util.List;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VType;

/**
 * The description for an executor service method (i.e. a shell command).
 *
 * @author carcassi
 */
public class ExecServiceMethodDescription extends ServiceMethodDescription {

    boolean resultAdded = false;
    String command;
    final List<String> orderedParameterNames = new ArrayList<>();

    /**
     * A new service method with the given name and description.
     *
     * @param name the method name; can't be null
     * @param description the method description; can't be null
     */
    public ExecServiceMethodDescription(String name, String description) {
        super(name, description);
    }

    /**
     * Adds a result for the script; cannot have multiple query results.
     *
     * @param name the result name; can't be null
     * @param description the result description; can't be null
     * @return this
     */
    public ExecServiceMethodDescription queryResult(String name, String description) {
        if (resultAdded) {
            throw new IllegalArgumentException("The query can only have one result");
        }
        resultAdded = true;

        addResult(name, description, VType.class);
        return this;
    }

    /**
     * Add a command for the script.
     *
     * @param command the shell command; can't be null
     * @return this description
     */
    public ExecServiceMethodDescription command(String command) {
        if (command == null){
            throw new IllegalArgumentException("Command must not be null");
        }
        this.command = command;
        return this;
    }

    @Override
    public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
        return new ExecServiceMethod(this, (ExecServiceDescription) serviceDescription);
    }

}
