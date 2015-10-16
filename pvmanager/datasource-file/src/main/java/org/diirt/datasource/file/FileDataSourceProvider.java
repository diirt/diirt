/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.ConfigurableDataSourceProvider;
import org.diirt.datasource.DataSourceConfiguration;
import org.diirt.datasource.DataSourceProvider;
import org.diirt.util.config.Configuration;

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
