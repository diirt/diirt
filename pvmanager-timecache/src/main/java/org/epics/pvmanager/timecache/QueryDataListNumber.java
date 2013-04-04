/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
