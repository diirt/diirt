/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.services.math;

import java.util.concurrent.Executors;
import org.diirt.service.Service;
import org.diirt.service.ServiceDescription;

/**
 *
 * @author carcassi
 */
public class MathService extends Service {

    public MathService() {
        super(new ServiceDescription("math", "Simple math service")
                .addServiceMethod(new AddServiceMethod(Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool("mathService"))))
                .addServiceMethod(new MultiplyServiceMethod(Executors.newSingleThreadExecutor(org.diirt.util.concurrent.Executors.namedPool("mathService")))));
    }
    
}
