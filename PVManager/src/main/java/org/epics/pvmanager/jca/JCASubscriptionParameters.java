/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import gov.aps.jca.dbr.DBRType;

/**
 *
 * @author carcassi
 */
public class JCASubscriptionParameters {
    
    private final DBRType epicsValueType;
    private final DBRType epicsMetaType;
    private final int count;

    public JCASubscriptionParameters(DBRType epicsValueType, DBRType epicsMetaType, int count) {
        this.epicsValueType = epicsValueType;
        this.epicsMetaType = epicsMetaType;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public DBRType getEpicsMetaType() {
        return epicsMetaType;
    }

    public DBRType getEpicsValueType() {
        return epicsValueType;
    }
    
}
