/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.integration;

import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public interface Event {
    
    public Timestamp getTimestamp();
    
    public String getPvName();
    
    public abstract Object getEvent();
}
