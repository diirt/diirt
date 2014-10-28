/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sim;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.DataSourceFactory;

/**
 * DataSourceFactory for simulated data.
 *
 * @author carcassi
 */
public class SimulationDataSourceFactory extends DataSourceFactory {

    @Override
    public String getName() {
        return "sim";
    }

    @Override
    public DataSource createInstance() {
        return new SimulationDataSource();
    }
    
}
