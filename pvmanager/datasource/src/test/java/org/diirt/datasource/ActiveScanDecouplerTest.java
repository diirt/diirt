/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.concurrent.Callable;

import org.diirt.util.time.TimeDuration;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class ActiveScanDecouplerTest {

    @Test
    public void pauseResume() {
        DesiredRateEventLog log = new DesiredRateEventLog();
        SourceDesiredRateDecoupler decoupler = new ActiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(100), log);
        log.setDecoupler(decoupler);
        decoupler.start();
        assertThat(decoupler.isPaused(), equalTo(false));
        assertThat(decoupler.isStopped(), equalTo(false));
        decoupler.pause();
        assertThat(decoupler.isPaused(), equalTo(true));
        assertThat(decoupler.isStopped(), equalTo(false));
        decoupler.resume();
        assertThat(decoupler.isPaused(), equalTo(false));
        assertThat(decoupler.isStopped(), equalTo(false));
        decoupler.stop();
        assertThat(decoupler.isPaused(), equalTo(false));
        assertThat(decoupler.isStopped(), equalTo(true));
    }

    @Test
    public void activeScanningRate() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog();
                SourceDesiredRateDecoupler decoupler = new ActiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(10), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                Thread.sleep(500);
                decoupler.stop();
                assertThat(log.getEvents().size(), lessThanOrEqualTo(5));
                assertThat(log.getEvents().size(), greaterThanOrEqualTo(4));
                return null;
            }
        });
    }

    @Test
    public void pausedScanningRate() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog();
                SourceDesiredRateDecoupler decoupler = new ActiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(10), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                decoupler.pause();
                Thread.sleep(300);
                decoupler.resume();
                decoupler.stop();
                assertThat(log.getEvents().size(), lessThan(2));
                return null;
            }
        });
    }


    public static void repeatTest(int nTimes, Callable<?> task) throws Exception {
        for (int i = 0; i < nTimes; i++) {
            task.call();
        }
    }
}
