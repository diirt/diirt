/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.time.Instant;
import java.util.ArrayList;

import org.diirt.datasource.DesiredRateEvent;
import org.diirt.datasource.SourceDesiredRateDecoupler;
import org.diirt.datasource.DesiredRateEventListener;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
 class DesiredRateEventLog implements DesiredRateEventListener {

    private final Object lock = new Object();

    private final List<DesiredRateEvent> events = new ArrayList<>();
    private final List<Instant> timestamps = new ArrayList<>();
    private SourceDesiredRateDecoupler decoupler;
    private final Integer pause;

    public DesiredRateEventLog() {
        pause = null;
    }

    public DesiredRateEventLog(Integer msPause) {
        this.pause = msPause;
    }

    public void setDecoupler(SourceDesiredRateDecoupler decoupler) {
        synchronized(lock) {
            this.decoupler = decoupler;
        }
    }

    public SourceDesiredRateDecoupler getDecoupler() {
        synchronized(lock) {
            return decoupler;
        }
    }

    @Override
    public void desiredRateEvent(DesiredRateEvent event) {
        synchronized(lock) {
            events.add(event);
            timestamps.add(Instant.now());
        }
        if (pause != null) {
            try {
                Thread.sleep(pause);
            } catch (InterruptedException ex) {
                Logger.getLogger(DesiredRateEventLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        SourceDesiredRateDecoupler decoupler;
        synchronized(lock) {
            decoupler = this.decoupler;
        }
        decoupler.readyForNextEvent();
    }

    public List<DesiredRateEvent.Type> getEventTypes(int n) {
        synchronized(lock) {
            return events.get(n).getTypes();
        }
    }

    public List<DesiredRateEvent> getEvents() {
        synchronized(lock) {
            return events;
        }
    }

    public void printLog() {
        synchronized(lock) {
            for (int i = 0; i < events.size(); i++) {
                System.out.println(timestamps.get(i) + " " + events.get(i).getTypes());
            }
        }
    }

}
