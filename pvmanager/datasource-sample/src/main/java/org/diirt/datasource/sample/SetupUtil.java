/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import org.diirt.datasource.PVManager;
import org.diirt.util.concurrent.Executors;

/**
 *
 * @author carcassi
 */
public class SetupUtil {
    public static void defaultCASetup() {
        System.setProperty("com.cosylab.epics.caj.CAJContext.max_array_bytes", "10000000");
    }
    public static void defaultCASetupForSwing() {
        PVManager.setDefaultNotificationExecutor(Executors.swingEDT());
        defaultCASetup();
    }
}
