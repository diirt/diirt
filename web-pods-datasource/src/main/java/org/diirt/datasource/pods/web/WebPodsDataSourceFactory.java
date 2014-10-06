/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.util.config.Configuration;

/**
 * Factory for {@link WebPodsDataSource}.
 *
 * @author carcassi
 */
public final class WebPodsDataSourceFactory {

    public WebPodsDataSourceFactory() {
    }
    
    public String getName() {
        return "wp";
    }
    
    public String getDefaultConfPath() {
        return "datasources/wp";
    }
    
    public WebPodsDataSource createDataSource() {
        return createDataSource(getDefaultConfPath());
    }
    
    public WebPodsDataSource createDataSource(String confPath) {
        WebPodsDataSourceConfiguration conf = WebPodsDataSourceConfiguration.readConfiguration(confPath);
        if (conf != null) {
            return new WebPodsDataSource(conf);
        } else {
            return null;
        }
    }
}
