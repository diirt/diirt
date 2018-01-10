/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.pods.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.diirt.datasource.PVManager;

/**
 *
 * @author carcassi
 */
@WebListener
public class ShutdownCleanup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        PVManager.getReadScannerExecutorService().shutdownNow();
        PVManager.getDefaultDataSource().close();
    }

}
