/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import java.util.Collection;

/**
 * A factory class for Services.
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
