/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.common;

import java.io.File;

/**
 * Entry point for all configuration in diirt.
 * <p>
 * The configuration directory used is given by:
 * <ul>
 *     <li>The Java property <b>diirt.home</b> if set. It can either be
 *     set when creating the JVM using -D or programmatically using
 *     <code>System.setProperty</code>. When set programmatically, one
 *     must make sure that it is set before any call to {@link #configurationDirectory()},
 *     since the property is only read once and then cached.</li>
 *     <li>The environment variable <b>DIIRT_HOME</b> if set.</li>
 *     <li>The default <b>$USER_HOME/.diirt</b></li>
 * </ul>
 *
 * @author carcassi
 */
public class Configuration {
   
    private static final File configurationDirectory = configurationDirectory();
    
    private static File configurationDirectory() {
        // First look for java property
        String diirtHome = System.getProperty("diirt.home");
        
        // Second look for environment variable
        if (diirtHome == null) {
            diirtHome = System.getenv("DIIRT_HOME");
        }

        File dir;
        if (diirtHome != null) {
            dir = new File(diirtHome);
        } else {
            // Third use default in home directory
            dir = new File(System.getProperty("user.home"), ".diirt");
        }
        dir.mkdirs();
        return dir;
    }

    public static File getDirectory() {
        return configurationDirectory;
    }
}
