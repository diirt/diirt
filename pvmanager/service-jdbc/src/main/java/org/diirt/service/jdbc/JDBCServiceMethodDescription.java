/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VTable;

/**
 * The description for a JDBC service method (i.e. a query).
 *
 * @author carcassi
 */
public class JDBCServiceMethodDescription extends ServiceMethodDescription {

    boolean resultAdded = false;
    String query;
    final List<String> orderedParameterNames = new ArrayList<>();

    /**
     * A new service method with the given name and description.
     *
     * @param name the method name; can't be null
     * @param description the method description; can't be null
     */
    public JDBCServiceMethodDescription(String name, String description) {
        super(name, description);
    }

    /**
     * Adds a result for the query.
     * <p>
     * The result must be specified if the query returns data (i.e. it is a
     * SELECT) and must not be specified if the query does not return data (i.e.
     * INSERT, UPDATE, DELETE, ...).
     *
     * @param name the result name; can't be null
     * @param description the result description; can't be null
     * @return this
     */
    public JDBCServiceMethodDescription queryResult(String name, String description) {
        if (resultAdded) {
            throw new IllegalArgumentException("The query can only have one result");
        }
        resultAdded = true;

        addResult(name, description, VTable.class);
        return this;
    }

    /**
     * The query mapped to this service method; cannot have multiple query
     * commands.
     *
     * @param query the query to execute; can't be null
     * @return this
     */
    public JDBCServiceMethodDescription query(String query) {
        if (query == null){
            throw new IllegalArgumentException("Query must not be null");
        }
        if (this.query != null) {
            throw new IllegalArgumentException("Query was already set");
        }
        this.query = query;
        return this;
    }

    @Override
    public ServiceMethod createServiceMethod(ServiceDescription serviceDescription) {
        return new JDBCServiceMethod(this, (JDBCServiceDescription) serviceDescription);
    }

}
