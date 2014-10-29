/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import org.diirt.datasource.DataSourceProvider;

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
        return "datasources/wp";
    }
    
    @Override
    public WebPodsDataSource createInstance() {
        return createInstance(getDefaultConfPath());
    }
    
    public WebPodsDataSource createInstance(String confPath) {
        WebPodsDataSourceConfiguration conf = WebPodsDataSourceConfiguration.readConfiguration(confPath);
        if (conf != null) {
            return new WebPodsDataSource(conf);
        } else {
            return null;
        }
    }
}
