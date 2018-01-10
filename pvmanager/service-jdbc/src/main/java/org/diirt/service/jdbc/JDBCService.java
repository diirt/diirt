/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.diirt.service.Service;

/**
 * Group of request/response operations sharing resources (a Service); applied
 * to querying a <a
 * href="http://www.oracle.com/technetwork/java/javase/jdbc/index.html">JDBC
 * datasource</a>.
 * <p>
 * It is important to {@link JDBCService#close()} the service to close shared
 * resources.
 *
 * @author asbarber
 */
public class JDBCService extends Service {

    private final DataSource dataSource;

    /**
     * Creates a new service for JDBC operations.
     *
     * @param serviceDescription the description of the JDBC service; can't be
     * null
     */
    public JDBCService(JDBCServiceDescription serviceDescription) {
        super(serviceDescription);
        dataSource = serviceDescription.dataSource;
    }

    /**
     * Closes the shared resources: executor and JDBC datasource.
     */
    @Override
    public void close() {
        // Executor close
        super.close();

        // Datasource close
        try {
            dataSource.getConnection().close();
        } catch (SQLException ex) {
            throw new RuntimeException("Unable to close the JDBC data source connection", ex);
        }
    }
}
