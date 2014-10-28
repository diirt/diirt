/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.ca;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceFactory;

/**
 * DataSourceFactory for channel access.
 *
 * @author carcassi
 */
public class JCADataSourceFactory extends DataSourceFactory {

    @Override
    public String getName() {
        return "ca";
    }

    @Override
    public DataSource createInstance() {
        return new JCADataSource();
    }
    
}
