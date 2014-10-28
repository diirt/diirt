/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.ServiceLoader;

/**
 * A factory class to register a DataSource.
 * <p>
 * This interface allows different modules to registers a DataSource through
 * the ServiceLoader. Implementations that are correctly registered will
 * be asked to create a DataSource instance which will be registered into
 * a CompositeDataSource with the given name.
 * <p>
 * The factory only needs to care about the DataSource creation, and not the
 * rest of the life-cycle.
 *
 * @author carcassi
 */
public abstract class DataSourceFactory {
    /**
     * The name to be used when registering the DataSource with the
     * CompositeDataSource.
     * 
     * @return a short String
     */
    public abstract String getName();
    
    /**
     * Creates a new instance of the DataSource.
     * 
     * @return a new DataSource
     */
    public abstract DataSource createInstance();
    
    /**
     * Looks up the registered factories and creates a CompositeDataSource
     * using them.
     * 
     * @return a new DataSource
     */
    public static CompositeDataSource createDataSource() {
        CompositeDataSource composite = new CompositeDataSource();
        // Find formula functions to register using the ServiceLoader
        ServiceLoader<DataSourceFactory> sl = ServiceLoader.load(DataSourceFactory.class);
        for (DataSourceFactory factory : sl) {
            composite.putDataSource(factory);
        }
        return composite;
    }
}
