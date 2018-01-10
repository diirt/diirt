/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.cf.service;

import java.util.Collection;
import java.util.Collections;
import org.diirt.service.Service;
import org.diirt.service.ServiceProvider;

/**
 * The service factory for channel finder.
 *
 * @author carcassi
 */
public class ChannelFinderServiceProvider implements ServiceProvider {

    @Override
    public String getName() {
        return "cf";
    }

    @Override
    public Collection<Service> createServices() {
        return Collections.singleton(ChannelFinderService.createService());
    }

}
