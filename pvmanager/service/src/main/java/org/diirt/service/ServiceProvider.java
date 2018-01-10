/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collection;

/**
 * Provides a set of services that can be registered to the framework.
 * <p>
 * Instances of this class made available to the <code>ServiceLoader</code> are automatically
 * added to the {@link ServiceRegistry} at startup.
 *
 * @author carcassi
 */
public interface ServiceProvider {

    /**
     * Returns the name of the service provider.
     *
     * @return a short name
     */
    public String getName();

    /**
     * Returns a collection of services to be registered.
     *
     * @return an immutable collection of services; never null
     */
    public Collection<Service> createServices();

}
