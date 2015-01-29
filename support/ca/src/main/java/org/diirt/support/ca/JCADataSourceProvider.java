/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceProvider;
import org.diirt.util.config.Configuration;

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
    
    public String getDefaultConfPath() {
        return "datasources/" + getName();
    }

    @Override
    public JCADataSource createInstance() {
        return createInstance(getDefaultConfPath());
    }
    
    public JCADataSource createInstance(String confPath) {
        JCADataSourceConfiguration conf = readConfiguration(confPath);
        if (conf != null) {
            return new JCADataSource(conf);
        } else {
            return null;
        }
    }
    
    public JCADataSourceConfiguration readDefaultConfiguration() {
        return readConfiguration(getDefaultConfPath());
    }
    
    public JCADataSourceConfiguration readConfiguration(String confPath) {
        try (InputStream input = Configuration.getFileAsStream(confPath + "/ca.xml", this, "ca.default.xml")) {
            JCADataSourceConfiguration conf = new JCADataSourceConfiguration(input);
            return conf;
        } catch (Exception ex) {
            Logger.getLogger(JCADataSourceConfiguration.class.getName()).log(Level.SEVERE, "Couldn't load DIIRT_HOME/" + confPath + "/ca.xml", ex);
            return null;
        }
    }
    
}
