/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.pods.web;

import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.PVManager;

/**
 *
 * @author carcassi
 */
public class SimpleProbe {
    public static void main(String[] args) {
        org.epics.pvmanager.sample.SimpleProbe.main(args);
        CompositeDataSource composite = (CompositeDataSource) PVManager.getDefaultDataSource();
        composite.putDataSource("wp", new WebPodsDataSource());
        System.out.println("Done");
    }
}
