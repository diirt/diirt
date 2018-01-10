/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import org.diirt.datasource.ConfigurableDataSourceProvider;

/**
 * DataSourceProvider for file-system based data source.
 *
 * @author carcassi
 */
public class FileDataSourceProvider extends ConfigurableDataSourceProvider<FileDataSource, FileDataSourceConfiguration> {

    public FileDataSourceProvider() {
        super(FileDataSourceConfiguration.class);
    }

    @Override
    public String getName() {
        return "file";
    }
}
