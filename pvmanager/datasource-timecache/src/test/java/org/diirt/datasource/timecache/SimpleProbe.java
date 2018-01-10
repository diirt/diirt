/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
public class SimpleProbe {
        public static void main(String[] args) {
                Logger.getGlobal().setLevel(Level.FINEST);
                org.diirt.datasource.sample.SimpleProbe.main(args);
        }

}