/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service;

import org.diirt.service.ServiceDescription;
import org.diirt.service.Service;

/**
 *
 * @author carcassi
 */
public class MathService extends Service {

    public MathService() {
        super(new ServiceDescription("math", "Simple math service")
                .addServiceMethod(new AddServiceMethod())
                .addServiceMethod(new MultiplyServiceMethod()));
    }
    
}
