/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
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
        Logger.getLogger("").getHandlers()[0].setLevel(Level.CONFIG);
        Logger.getLogger("").setLevel(Level.CONFIG);

        System.out.println("Listing all services");
        for (String service : ServiceRegistry.getDefault().getRegisteredServiceNames()) {
            System.out.println("- " + service);
        }
        System.out.println("Done");
    }
}
