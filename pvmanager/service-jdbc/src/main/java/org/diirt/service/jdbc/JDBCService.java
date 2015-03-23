/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.diirt.service.Service;

/**
 * TODO
 * @author asbarber
 */
public class JDBCService extends Service {

    private final DataSource dataSource;

    public JDBCService(JDBCServiceDescription serviceDescription) {
        super(serviceDescription);
        dataSource = serviceDescription.dataSource;
    }

    @Override
    public void close() {
        try {
            dataSource.getConnection().close();
        } catch (SQLException ex) {
            // TODO does this need logging?
            throw new RuntimeException("Unable to close the JDBC data source connection", ex);
        }
    }
}
