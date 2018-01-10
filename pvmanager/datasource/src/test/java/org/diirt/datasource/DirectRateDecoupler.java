/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.diirt.datasource.DesiredRateEvent;
import org.diirt.datasource.SourceDesiredRateDecoupler;
import org.diirt.datasource.DesiredRateEventListener;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author carcassi
 */
class DirectRateDecoupler extends SourceDesiredRateDecoupler {

    public DirectRateDecoupler(ScheduledExecutorService scannerExecutor, Duration maxDuration, DesiredRateEventListener listener) {
        super(scannerExecutor, maxDuration, listener);
    }

    @Override
    void newReadConnectionEvent() {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addType(DesiredRateEvent.Type.READ_CONNECTION);
        sendDesiredRateEvent(event);
    }

    @Override
    void newWriteConnectionEvent() {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addType(DesiredRateEvent.Type.WRITE_CONNECTION);
        sendDesiredRateEvent(event);
    }

    @Override
    void newValueEvent() {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addType(DesiredRateEvent.Type.VALUE);
        sendDesiredRateEvent(event);
    }

    @Override
    void newReadExceptionEvent() {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addType(DesiredRateEvent.Type.READ_EXCEPTION);
        sendDesiredRateEvent(event);
    }

    @Override
    void newWriteExceptionEvent() {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addType(DesiredRateEvent.Type.WRITE_EXCEPTION);
        sendDesiredRateEvent(event);
    }

    @Override
    void newWriteSuccededEvent() {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addType(DesiredRateEvent.Type.WRITE_SUCCEEDED);
        sendDesiredRateEvent(event);
    }

    @Override
    void newWriteFailedEvent(Exception ex) {
        DesiredRateEvent event = new DesiredRateEvent();
        event.addWriteFailed(ex);
        sendDesiredRateEvent(event);
    }

}
