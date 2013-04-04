/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.timecache;

import java.util.List;

/**
 *
 * @author carcassi
 */
public interface Query {
    
    /**
     * Changes the parameters for the query.
     * This method is thread safe and returns immediately. It may schedule
     * asynchronous execution of service calls to fetch the data.
     * 
     * @param queryParameters 
     */
    public void update(QueryParameters queryParameters);
    
    /**
     * This method can be polled at regular interval and should return
     * right away with the data is already available, even if incomplete.
     * 
     * @return 
     */
    public QueryResult getResult();
    
    /**
     * This method can be polled at regular interval and should return
     * right away with the data is already available, even if incomplete.
     * 
     * @return 
     */
    public QueryResult getUpdate();
    
    /**
     * Query can be disposed. Future calls to getResult() and getUpdate() will
     * result in IllegalStateException.
     */
    public void close();
}
