/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.datasource.ConfigurableDataSourceProvider;
import org.diirt.datasource.DataSourceProvider;
import org.diirt.util.config.Configuration;

/**
 * Factory for {@link WebPodsDataSource}.
 *
 * @author carcassi
 */
public final class WebPodsDataSourceProvider extends ConfigurableDataSourceProvider<WebPodsDataSource, WebPodsDataSourceConfiguration> {

    public WebPodsDataSourceProvider() {
        super(WebPodsDataSourceConfiguration.class);
    }

    @Override
    public String getName() {
        return "wp";
    }

}
