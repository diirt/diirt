/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceProvider;

/**
 * DataSourceProvider for channel access.
 *
 * @author carcassi
 */
public class JCADataSourceProvider extends DataSourceProvider {

    @Override
    public String getName() {
        return "ca";
    }

    @Override
    public DataSource createInstance() {
        return new JCADataSource();
    }
    
}
