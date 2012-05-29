/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

import java.util.Collection;

/**
 *
 * @author carcassi
 */
public interface DataSourceTypeAdapterSet {
    Collection<? extends DataSourceTypeAdapter<?, ?>> getAdapters();
}
