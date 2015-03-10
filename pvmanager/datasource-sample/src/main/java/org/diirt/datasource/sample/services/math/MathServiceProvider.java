/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.Arrays;
import java.util.Collection;
import org.diirt.service.Service;
import org.diirt.service.ServiceProvider;

/**
 *
 * @author asbarber
 */
public class MathServiceProvider implements ServiceProvider {

    @Override
    public String getName() {
        return "math";
    }

    @Override
    public Collection<Service> createServices() {
        return Arrays.asList(MathService.createMathService());
    }

}
