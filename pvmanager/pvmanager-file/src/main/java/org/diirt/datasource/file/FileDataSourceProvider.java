/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceProvider;
import org.diirt.util.config.Configuration;

/**
 * DataSourceProvider for file-system based data source.
 *
 * @author carcassi
 */
public class FileDataSourceProvider extends DataSourceProvider {

    @Override
    public String getName() {
        return "file";
    }
    
    public String getDefaultConfPath() {
        return "datasources/" + getName();
    }

    @Override
    public FileDataSource createInstance() {
        return createInstance(getDefaultConfPath());
    }
    
    public FileDataSource createInstance(String confPath) {
        FileDataSourceConfiguration conf = readConfiguration(confPath);
        if (conf != null) {
            return new FileDataSource(conf);
        } else {
            return null;
        }
    }
    
    public FileDataSourceConfiguration readDefaultConfiguration() {
        return readConfiguration(getDefaultConfPath());
    }
    
    public FileDataSourceConfiguration readConfiguration(String confPath) {
        try (InputStream input = Configuration.getFileAsStream(confPath + "/file.xml", this, "file.default.xml")) {
            FileDataSourceConfiguration conf = new FileDataSourceConfiguration(input);
            return conf;
        } catch (Exception ex) {
            Logger.getLogger(FileDataSourceConfiguration.class.getName()).log(Level.SEVERE, "Couldn't load DIIRT_HOME/" + confPath + "/file.xml", ex);
            return null;
        }
    }
}
