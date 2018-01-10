/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import javax.sql.DataSource;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;

/**
 * The description on how to construct a JDBC service.
 * <p>
 * This class encapsulate the description of a service, including:
 * <ul>
 *   <li>The connection parameters</li>
 *   <li>A number queries</li>
 *   <li>The arguments for each query and how should they be mapped</li>
 *   <li>The results of each query</li>
 * </ul>
 *
 * @author carcassi
 */
public class JDBCServiceDescription extends ServiceDescription {

    DataSource dataSource;

    /**
     * A new service description with the given service name and description.
     *
     * @param name the name of the service
     * @param description a brief description
     */
    public JDBCServiceDescription(String name, String description) {
        super(name, description);
    }

    /**
     * The JDBC DataSource to use for database connection.
     * <p>
     * Use {@link SimpleDataSource} if you have a JDBC url.
     *
     * @param dataSource a JDBC datasource; can't be null
     * @return this
     */
    public JDBCServiceDescription dataSource(DataSource dataSource) {
        if (this.dataSource != null) {
            throw new IllegalArgumentException("DataSource was already set");
        }
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public Service createService(){
        return new JDBCService(this);
    }
}
