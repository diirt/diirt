/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.DataSourceProvider;
import org.diirt.util.config.Configuration;

/**
 * Factory for {@link WebPodsDataSource}.
 *
 * @author carcassi
 */
public final class WebPodsDataSourceProvider extends DataSourceProvider {
    
    @Override
    public String getName() {
        return "wp";
    }
    
    public String getDefaultConfPath() {
        return "datasources/" + getName();
    }
    
    @Override
    public WebPodsDataSource createInstance() {
        return createInstance(getDefaultConfPath());
    }
    
    public WebPodsDataSource createInstance(String confPath) {
        WebPodsDataSourceConfiguration conf = readConfiguration(confPath);
        if (conf != null) {
            return new WebPodsDataSource(conf);
        } else {
            return null;
        }
    }
    
    public WebPodsDataSourceConfiguration readDefaultConfiguration() {
        return readConfiguration(getDefaultConfPath());
    }
    
    public WebPodsDataSourceConfiguration readConfiguration(String confPath) {
        try (InputStream input = Configuration.getFileAsStream(confPath + "/wp.xml", this, "wp.default.xml")) {
            WebPodsDataSourceConfiguration conf = new WebPodsDataSourceConfiguration(input);
            return conf;
        } catch (Exception ex) {
            Logger.getLogger(WebPodsDataSourceConfiguration.class.getName()).log(Level.SEVERE, "Couldn't load DIIRT_HOME/" + confPath + "/wp.xml", ex);
            return null;
        }
    }
}
