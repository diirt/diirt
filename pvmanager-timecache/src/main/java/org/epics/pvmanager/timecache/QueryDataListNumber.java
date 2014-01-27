/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.List;
import org.epics.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public interface QueryDataListNumber extends QueryData {
    
    /**
     * For a ListNumber (array) the data is an ordered list of
     * ListNumebr elements.
     * 
     * @return 
     */
    @Override
    public List<ListNumber> getData();
    
}
