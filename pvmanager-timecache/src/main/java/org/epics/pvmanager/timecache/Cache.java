/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.timecache;

/**
 *
 * @author carcassi
 */
public interface Cache {
    public Query createQuery(String channelName, QueryParameters parameters);
}
