/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import org.epics.pvmanager.ValueCache;

/**
 *
 * @author carcassi
 */
public interface PayloadToTypeConverter<ConnectionPayload, MessagePayload> {
    
    /**
     * Determines whether the converter can take values from the channel
     * described by the connection payload and transform them in a 
     * type required by the cache.
     * 
     * @param cache the cache where data will need to be written
     * @param connPayload the connection information
     * @return zero if there is no match, or the position of the type matched
     */
    int match(ValueCache<?> cache, ConnectionPayload connection);
    
    /**
     * The parameters required to open a monitor for the channel. The
     * type of the parameters will be datasource specific.
     * 
     * @param cache the cache where data will need to be written
     * @param connPayload the connection information
     * @return datasource specific subscription information
     */
    Object getSubscriptionParameter(ValueCache<?> cache, ConnectionPayload connection);
    
    /**
     * Takes the information in the message and updates the cache.
     * 
     * @param cache cache to be updated
     * @param connection the connection information
     * @param message the payload of each message
     * @return true if a new value was stored
     */
    boolean updateCache(ValueCache cache, ConnectionPayload connection, MessagePayload message);
}
