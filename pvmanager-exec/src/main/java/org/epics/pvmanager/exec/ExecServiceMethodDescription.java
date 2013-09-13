/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import org.epics.pvmanager.service.ServiceMethodDescription;
import org.epics.vtype.VTable;

/**
 * The description for a JDBC service method (i.e. a query).
 *
 * @author carcassi
 */
public class ExecServiceMethodDescription {
    
    final ServiceMethodDescription serviceMethodDescription;
    boolean resultAdded = false;
    ExecutorService executorService;
    String shell;
    String shellArg;
    final List<String> orderedParameterNames = new ArrayList<>();

    /**
     * A new service method with the given name and description.
     * 
     * @param name the method name
     * @param description the method description
     */
    public ExecServiceMethodDescription(String name, String description) {
        serviceMethodDescription = new ServiceMethodDescription(name, description);
    }
    
    /**
     * Adds an argument for the query.
     * <p>
     * Arguments need to be specified in the same order as they appear in the query.
     * 
     * @param name argument name
     * @param description argument description
     * @param type the expected type of the argument
     * @return this
     */
    public ExecServiceMethodDescription addArgument(String name, String description, Class<?> type) {
        serviceMethodDescription.addArgument(name, description, type);
        orderedParameterNames.add(name);
        return this;
    }
    
    /**
     * Adds a result for the query.
     * <p>
     * The result must be specified if the query returns data (i.e. it is a SELECT)
     * and must not be specified if the query does not return data (i.e. INSERT, UPDATE, DELETE, ...).
     * 
     * @param name the result name
     * @param description the result description
     * @return this
     */
    public ExecServiceMethodDescription queryResult(String name, String description) {
        if (resultAdded) {
            throw new IllegalArgumentException("The query can only have one result");
        }
        serviceMethodDescription.addResult(name, description, VTable.class);
        return this;
    }
    
    ExecServiceMethodDescription executorService(ExecutorService executorService) {
        if (this.executorService != null) {
            throw new IllegalArgumentException("ExecutorService was already set");
        }
        this.executorService = executorService;
        return this;
    }
    
}
