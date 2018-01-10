/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceProvider;

/**
 * DataSourceProvider for pv access.
 *
 * @author carcassi
 */
public class PVADataSourceProvider extends DataSourceProvider {

    @Override
    public String getName() {
        return "pva";
    }

    @Override
    public DataSource createInstance() {
        return new PVADataSource();
    }

}
