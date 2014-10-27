/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.diirt.datasource;

import org.diirt.datasource.DesiredRateEvent;
import org.diirt.datasource.SourceDesiredRateDecoupler;
import org.diirt.datasource.DesiredRateEventListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carcassi
 */
 class DesiredRateEventLog implements DesiredRateEventListener {
    
    private final List<DesiredRateEvent> events = new CopyOnWriteArrayList<>();
    private volatile SourceDesiredRateDecoupler decoupler;
    private final Integer pause;

    public DesiredRateEventLog() {
        pause = null;
    }

    public DesiredRateEventLog(Integer msPause) {
        this.pause = msPause;
    }

    public void setDecoupler(SourceDesiredRateDecoupler decoupler) {
        this.decoupler = decoupler;
    }

    public SourceDesiredRateDecoupler getDecoupler() {
        return decoupler;
    }

    @Override
    public void desiredRateEvent(DesiredRateEvent event) {
        events.add(event);
        if (pause != null) {
            try {
                Thread.sleep(pause);
            } catch (InterruptedException ex) {
                Logger.getLogger(DesiredRateEventLog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        decoupler.readyForNextEvent();
    }
    
    public List<DesiredRateEvent.Type> getEventTypes(int n) {
        return events.get(n).getTypes();
    }

    public List<DesiredRateEvent> getEvents() {
        return events;
    }
    
}
