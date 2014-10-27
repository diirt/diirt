/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import org.diirt.service.Service;

/**
 * A pvmanager service based on command line execution.
 *
 * @author carcassi
 */
public class ExecService extends Service {

    /**
     * Creates a new execution service from the given service description.
     * <p>
     * The description will consist of environment information, commands
     * and how arguments and results should be mapped.
     * 
     * @param serviceDescription 
     */
    public ExecService(ExecServiceDescription serviceDescription) {
        super(serviceDescription.createService());
    }
    
}
