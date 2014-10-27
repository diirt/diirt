/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.service;

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
