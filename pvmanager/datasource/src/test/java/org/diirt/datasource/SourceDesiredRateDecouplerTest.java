/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class SourceDesiredRateDecouplerTest {

    @Test
    public void pauseResume() {
        SourceDesiredRateDecoupler decoupler = new DirectRateDecoupler(null, null, null);
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
    public void directRateDecoupler() throws Exception {
        DesiredRateEventLog log = new DesiredRateEventLog();
        SourceDesiredRateDecoupler decoupler = new DirectRateDecoupler(null, null, log);
        log.setDecoupler(decoupler);
        decoupler.start();
        decoupler.newReadConnectionEvent();
        decoupler.newReadExceptionEvent();
        decoupler.newValueEvent();
        decoupler.newWriteConnectionEvent();
        decoupler.newWriteExceptionEvent();
        decoupler.newWriteFailedEvent(new Exception());
        decoupler.newWriteSuccededEvent();
        decoupler.stop();
        assertThat(log.getEvents().size(), equalTo(7));
        assertThat(log.getEventTypes(0), contains(DesiredRateEvent.Type.READ_CONNECTION));
        assertThat(log.getEventTypes(1), contains(DesiredRateEvent.Type.READ_EXCEPTION));
        assertThat(log.getEventTypes(2), contains(DesiredRateEvent.Type.VALUE));
        assertThat(log.getEventTypes(3), contains(DesiredRateEvent.Type.WRITE_CONNECTION));
        assertThat(log.getEventTypes(4), contains(DesiredRateEvent.Type.WRITE_EXCEPTION));
        assertThat(log.getEventTypes(5), contains(DesiredRateEvent.Type.WRITE_FAILED));
        assertThat(log.getEventTypes(6), contains(DesiredRateEvent.Type.WRITE_SUCCEEDED));
    }

}
