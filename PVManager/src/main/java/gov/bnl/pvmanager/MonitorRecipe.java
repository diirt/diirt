/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager;

/**
 * Represents all information needed to create a data connection.
 *
 * @author carcassi
 */
public class MonitorRecipe {
    public String pvName;
    public ValueCache<?> cache;
    public Collector collector;
}
