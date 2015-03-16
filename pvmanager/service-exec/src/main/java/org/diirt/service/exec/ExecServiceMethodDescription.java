/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
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
     * @param name the method name
     * @param description the method description
     */
    public ExecServiceMethodDescription(String name, String description) {
        super(name, description);
    }

    /**
     * Adds a result for the script.
     *
     * @param name the result name
     * @param description the result description
     * @return this
     */
    public ExecServiceMethodDescription queryResult(String name, String description) {
        if (resultAdded) {
            throw new IllegalArgumentException("The query can only have one result");
        }
        addResult(name, description, VType.class);
        return this;
    }

    /**
     * Add a command for the script.
     * 
     * @param command the shell command
     * @return this description
     */
    public ExecServiceMethodDescription command(String command) {
        this.command = command;
        return this;
    }

    @Override
    public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
        return new ExecServiceMethod(this, (ExecServiceDescription) serviceDescription);
    }

}
