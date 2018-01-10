/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample.graphene;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.diirt.graphene.NumberColorMap;
import org.diirt.graphene.NumberColorMaps;

/**
 *
 * @author carcassi
 */
public class RegisteredColorMaps {
    public static void main(String[] args) throws Exception {
        // Increasing logging at CONFIG level
        Logger.getLogger("").getHandlers()[0].setLevel(Level.CONFIG);
        Logger.getLogger("").setLevel(Level.CONFIG);

        System.out.println("Listing all registered color maps");
        for (Map.Entry<String, NumberColorMap> registeredMap : NumberColorMaps.getRegisteredColorSchemes().entrySet()) {
            NumberColorMap colorMap = registeredMap.getValue();
            System.out.println("- " + colorMap);
        }
        System.out.println("Done");
    }
}
