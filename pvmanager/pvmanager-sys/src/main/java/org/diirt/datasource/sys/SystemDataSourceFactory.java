/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sys;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceFactory;

/**
 * DataSourceFactory for system information.
 *
 * @author carcassi
 */
public class SystemDataSourceFactory extends DataSourceFactory {

    @Override
    public String getName() {
        return "sys";
    }

    @Override
    public DataSource createInstance() {
        return new SystemDataSource();
    }
    
}
