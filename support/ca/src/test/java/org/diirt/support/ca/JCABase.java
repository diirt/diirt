/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.ca;

import java.util.ArrayList;
import java.util.List;
import org.diirt.datasource.PVManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author carcassi
 */
public class JCABase {
    static JCADataSource jca;

    public JCABase() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        jca = new JCADataSourceConfiguration().create();
        PVManager.setDefaultDataSource(jca);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        PVManager.setDefaultDataSource(null);
    }

    private List<Thread> activeThreads() {
        Thread[] buffer = new Thread[30];
        int size = Thread.enumerate(buffer);
        List<Thread> result = new ArrayList<Thread>();
        for (int i = 0; i < size; i++) {
            result.add(buffer[i]);
        }
        return result;
    }

    private boolean isJCAThreadPresent() {
        for (Thread thread : activeThreads()) {
            if (thread.getName().equals("com.cosylab.epics.caj.util.Timer")) {
                return true;
            }
        }
        return false;
    }

    public void assertJCAOn() {
        assertNotNull("Context is not present", jca.getContext());
        assertTrue("JCA Timer thread not found", isJCAThreadPresent());
    }

    public void assertJCAOff() {
        assertNull("Context is present", jca.getContext());
        assertFalse("JCA Timer thread was found", isJCAThreadPresent());
    }
}
