/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.log;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.service.ServiceRegistry;

/**
 *
 * @author carcassi
 */
public class ServiceConfigurationLog {
    public static void main(String[] args) throws Exception {
        // Increasing logging at CONFIG level
        Logger log1 = Logger.getLogger(ServiceRegistry.class.getName());
        log1.setLevel(Level.CONFIG);
        Logger.getLogger("").getHandlers()[0].setLevel(Level.CONFIG);
        
        System.out.println("Listing all services");
        for (String service : ServiceRegistry.getDefault().listServices()) {
            System.out.println("- " + service);
        }
        System.out.println("Done");
    }
}
