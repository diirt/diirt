/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
