/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.util.config.Configuration;

/**
 * DataSourceProvider for a data source that require configuration.
 *
 * @author carcassi
 */
public abstract class ConfigurableDataSourceProvider<D extends DataSource, C extends DataSourceConfiguration<D>> extends DataSourceProvider {
    
    protected Class<C> clazz;

    protected ConfigurableDataSourceProvider(Class<C> clazz) {
        this.clazz = clazz;
    }
    
    public String getConfigurationPath() {
        return "datasources/" + getName();
    }
    
    public String getConfigurationFilename() {
        return getName() + ".xml";
    }
    
    public String getBundledDefaultConfiguration() {
        return getName() + ".default.xml";
    }

    @Override
    public D createInstance() {
        return createInstance(getConfigurationPath());
    }
    
    public D createInstance(String confPath) {
        C configuration;
        try {
            configuration = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Counding instanciate configuration object " + clazz.getSimpleName(), ex);
            return null;
        }
        
        try (InputStream input = Configuration.getFileAsStream(confPath + "/" + getConfigurationFilename(), this, getBundledDefaultConfiguration())) {
            configuration.read(input);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Couldn't load DIIRT_HOME/" + confPath + "/file.xml", ex);
            return null;
        }
        return configuration.create();
    }
}
