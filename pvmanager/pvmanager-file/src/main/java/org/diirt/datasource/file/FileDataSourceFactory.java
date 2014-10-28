/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceFactory;

/**
 * DataSourceFactory for file-system based data source.
 *
 * @author carcassi
 */
public class FileDataSourceFactory extends DataSourceFactory {

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public DataSource createInstance() {
        return new FileDataSource();
    }
    
}
