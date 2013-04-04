/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.timecache;

import org.epics.util.time.TimeRelativeInterval;

/**
 *
 * @author carcassi
 */
public class QueryParameters {
    TimeRelativeInterval timeInterval;
    
    public QueryParameters timeInterval(TimeRelativeInterval timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

}
