/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.timecache;

import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public interface QueryDataNumber extends QueryData {
    
    /**
     * For a number, the data is a ListNumber of the
     * ordered samples.
     * @return 
     */
    @Override
    public ListNumber getData();
    
}
