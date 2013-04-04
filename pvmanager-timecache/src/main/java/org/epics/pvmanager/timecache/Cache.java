/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.timecache;

/**
 *
 * @author carcassi
 */
public interface Cache {
    public Query createQuery(String channelName, QueryParameters parameters);
}
