/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
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
