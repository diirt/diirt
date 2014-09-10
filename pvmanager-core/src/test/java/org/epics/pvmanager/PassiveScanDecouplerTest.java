/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.pvmanager;

import java.util.concurrent.Callable;
import org.epics.util.time.TimeDuration;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.epics.pvmanager.DesiredRateEvent.Type.*;

/**
 *
 * @author carcassi
 */
public class PassiveScanDecouplerTest {

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
    public void noEvents() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog();
                SourceDesiredRateDecoupler decoupler = new PassiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(10), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                Thread.sleep(500);
                decoupler.stop();
                // 1 event: connection
                assertThat(log.getEvents().size(), equalTo(1));
                return null;
            }
        });
    }

    @Test
    public void slowEvents() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog();
                SourceDesiredRateDecoupler decoupler = new PassiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(50), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                Thread.sleep(100);
                decoupler.newValueEvent();
                Thread.sleep(100);
                decoupler.newValueEvent();
                Thread.sleep(100);
                decoupler.newValueEvent();
                Thread.sleep(100);
                decoupler.stop();
                // 4 events, connection and 3 values
                assertThat(log.getEvents().size(), equalTo(4));
                return null;
            }
        });
    }

    @Test
    public void fastEvents() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog();
                SourceDesiredRateDecoupler decoupler = new PassiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(10), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                Thread.sleep(100);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(1);
                decoupler.newValueEvent();
                Thread.sleep(100);
                decoupler.stop();
                // 3 events: connection, first value, last value
                assertThat(log.getEvents().size(), equalTo(3));
                return null;
            }
        });
    }

    @Test
    public void rescheduling() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog(10);
                SourceDesiredRateDecoupler decoupler = new PassiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(20), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                Thread.sleep(100);
                for (int i = 0; i < 4*5+1; i++) {
                    decoupler.newValueEvent();
                    Thread.sleep(10);
                }
                decoupler.stop();
                // 6 events: connection, 5 value
                assertThat(log.getEvents().size(), equalTo(6));
                return null;
            }
        });
    }

    @Test
    public void slowResponse() throws Exception {
        repeatTest(10, new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                DesiredRateEventLog log = new DesiredRateEventLog(100);
                SourceDesiredRateDecoupler decoupler = new PassiveScanDecoupler(PVManager.getReadScannerExecutorService(), TimeDuration.ofHertz(50), log);
                log.setDecoupler(decoupler);
                decoupler.start();
                Thread.sleep(25);
                decoupler.newValueEvent();
                Thread.sleep(25);
                decoupler.newValueEvent();
                Thread.sleep(25);
                decoupler.newValueEvent();
                Thread.sleep(200);
                decoupler.stop();
                assertThat(log.getEvents().size(), equalTo(2));
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
