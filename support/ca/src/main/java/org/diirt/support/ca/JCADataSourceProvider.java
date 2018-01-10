/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import org.diirt.datasource.ConfigurableDataSourceProvider;

/**
 * DataSourceProvider for channel access.
 *
 * @author carcassi
 */
public class JCADataSourceProvider extends ConfigurableDataSourceProvider<JCADataSource, JCADataSourceConfiguration> {

    public JCADataSourceProvider() {
        super(JCADataSourceConfiguration.class);
    }

    @Override
    public String getName() {
        return "ca";
    }

}
