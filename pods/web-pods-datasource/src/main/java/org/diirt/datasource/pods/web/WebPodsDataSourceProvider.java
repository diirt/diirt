/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import org.diirt.datasource.ConfigurableDataSourceProvider;

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
