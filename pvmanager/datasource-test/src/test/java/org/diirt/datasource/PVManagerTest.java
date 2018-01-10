/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.diirt.datasource.test.MockExecutor;
import org.diirt.datasource.test.MockDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.diirt.datasource.ExpressionLanguage.*;
import static org.diirt.util.concurrent.Executors.*;
import static org.diirt.util.time.TimeDuration.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Tests PVManager defaults are overrides are honored.
 *
 * @author carcassi
 */
public class PVManagerTest {

    public PVManagerTest() {
    }

    @Before @After
    public void restoreDefaults() {
        PVManager.setDefaultDataSource(null);
        PVManager.setDefaultNotificationExecutor(localThread());
    }

    private PV<?, ?> pv;
    private PVReader<?> pvReader;

    @After
    public void closePVs() {
        if (pv != null) {
            pv.close();
            pv = null;
        }

        if (pvReader != null) {
            pvReader.close();
            pvReader = null;
        }
    }

    @Test(expected=IllegalStateException.class)
    public void lackDataSource() {
        pvReader = PVManager.read(channel("test")).maxRate(ofHertz(10));
    }

    @Test(expected=IllegalStateException.class)
    public void lackNotificationExecutor() {
        PVManager.setDefaultDataSource(new MockDataSource());
        PVManager.setDefaultNotificationExecutor(null);
        pvReader = PVManager.read(channel("test")).maxRate(ofHertz(10));
    }

    @Test
    public void defaultDataSource() {
        MockDataSource defaultDataSource = new MockDataSource();

        PVManager.setDefaultDataSource(defaultDataSource);
        pv = PVManager.readAndWrite(channel("test")).asynchWriteAndMaxReadRate(ofHertz(10));

        assertThat(defaultDataSource.getReadRecipe(), not(equalTo(null)));
        assertThat(defaultDataSource.getWriteRecipe(), not(equalTo(null)));
    }

    @Test
    public void defaultThreadSwitch() throws Exception {
        PVManager.setDefaultDataSource(new MockDataSource());
        MockExecutor defaultExecutor = new MockExecutor();

        PVManager.setDefaultNotificationExecutor(defaultExecutor);
        pvReader = PVManager.read(constant("Test")).maxRate(ofHertz(10));
        Thread.sleep(100);

        assertThat(defaultExecutor.getCommand(), not(equalTo(null)));
    }

    @Test
    public void overrideDataSource() {
        MockDataSource defaultDataSource = new MockDataSource();
        MockDataSource overrideDataSource = new MockDataSource();

        PVManager.setDefaultDataSource(defaultDataSource);
        pvReader = PVManager.readAndWrite(channel("test")).from(overrideDataSource).asynchWriteAndMaxReadRate(ofHertz(10));

        assertThat(defaultDataSource.getReadRecipe(), equalTo(null));
        assertThat(defaultDataSource.getWriteRecipe(), equalTo(null));
        assertThat(overrideDataSource.getReadRecipe(), not(equalTo(null)));
        assertThat(overrideDataSource.getWriteRecipe(), not(equalTo(null)));
    }

    @Test
    public void overrideThreadSwitch() throws Exception {
        PVManager.setDefaultDataSource(new MockDataSource());
        MockExecutor defaultExecutor = new MockExecutor();
        MockExecutor overrideExecutor = new MockExecutor();

        PVManager.setDefaultNotificationExecutor(defaultExecutor);
        pvReader = PVManager.read(constant("Test")).notifyOn(overrideExecutor).maxRate(ofHertz(10));
        Thread.sleep(100);

        assertThat(defaultExecutor.getCommand(), equalTo(null));
        assertThat(overrideExecutor.getCommand(), not(equalTo(null)));
    }

}