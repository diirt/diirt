/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents all information needed to create a data connection.
 * 
 *
 * @author carcassi
 */
public class MonitorRecipe {
    public Map<String, ValueCache> caches = new HashMap<String, ValueCache>();
    public Collector collector;
}
