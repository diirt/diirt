/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.jdbc;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.epics.pvmanager.service.ServiceMethodDescription;
import org.epics.vtype.VTable;

/**
 *
 * @author carcassi
 */
public class JDBCServiceMethodDescription {
    
    final ServiceMethodDescription serviceMethodDescription;
    boolean resultAdded = false;
    Connection connection;
    ExecutorService executorService;
    String query;
    final List<String> orderedParameterNames = new ArrayList<>();

    public JDBCServiceMethodDescription(String name, String description) {
        serviceMethodDescription = new ServiceMethodDescription(name, description);
    }
    
    public JDBCServiceMethodDescription addArgument(String name, String description, Class<?> type) {
        serviceMethodDescription.addArgument(name, description, type);
        orderedParameterNames.add(name);
        return this;
    }
    
    public JDBCServiceMethodDescription queryResult(String name, String description) {
        if (resultAdded) {
            throw new IllegalArgumentException("The query can only have one result");
        }
        serviceMethodDescription.addResult(name, description, VTable.class);
        return this;
    }
    
    public JDBCServiceMethodDescription connection(Connection connection) {
        if (this.connection != null) {
            throw new IllegalArgumentException("Connection was already set");
        }
        this.connection = connection;
        return this;
    }
    
    public JDBCServiceMethodDescription executorService(ExecutorService executorService) {
        if (this.executorService != null) {
            throw new IllegalArgumentException("ExecutorService was already set");
        }
        this.executorService = executorService;
        return this;
    }
    
    public JDBCServiceMethodDescription query(String query) {
        if (this.query != null) {
            throw new IllegalArgumentException("Query was already set");
        }
        this.query = query;
        return this;
    }
    
    
    
}
