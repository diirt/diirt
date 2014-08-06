/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.pvmanager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author carcassi
 */
 class DesiredRateEventLog implements DesiredRateEventListener {
    
    private List<DesiredRateEvent> events = new CopyOnWriteArrayList<DesiredRateEvent>();
    private volatile SourceDesiredRateDecoupler decoupler;

    public void setDecoupler(SourceDesiredRateDecoupler decoupler) {
        this.decoupler = decoupler;
    }

    public SourceDesiredRateDecoupler getDecoupler() {
        return decoupler;
    }

    @Override
    public void desiredRateEvent(DesiredRateEvent event) {
        events.add(event);
        decoupler.readyForNextEvent();
    }
    
    public List<DesiredRateEvent.Type> getEventTypes(int n) {
        return events.get(n).getTypes();
    }

    public List<DesiredRateEvent> getEvents() {
        return events;
    }
    
}
