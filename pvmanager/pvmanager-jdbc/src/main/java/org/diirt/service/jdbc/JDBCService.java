/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.jdbc;

import org.diirt.service.Service;

/**
 * A pvmanager service based on database queries.
 *
 * @author carcassi
 */
public class JDBCService extends Service {

    /**
     * Creates a new database service from the given service description.
     * <p>
     * The description will consist of connection parameters, queries
     * and how arguments and results should be mapped.
     * 
     * @param serviceDescription the service description; can't be null
     */
    public JDBCService(JDBCServiceDescription serviceDescription) {
        super(serviceDescription.createService());
    }
    
}
