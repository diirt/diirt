/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.jca;

import gov.aps.jca.dbr.CTRL;
import gov.aps.jca.dbr.TIME;
import java.text.NumberFormat;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.data.Alarm;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;
import org.epics.pvmanager.data.Display;
import org.epics.pvmanager.data.Time;

/**
 *
 * @author carcassi
 */
class VNumberMetadata<TValue extends TIME, TMetadata extends CTRL> implements Alarm, Time, Display {

    final TValue dbrValue;
    private final TMetadata metadata;
    private final boolean disconnected;

    VNumberMetadata(TValue dbrValue, TMetadata metadata) {
        this(dbrValue, metadata, false);
    }

    VNumberMetadata(TValue dbrValue, TMetadata metadata, boolean disconnected) {
        this.dbrValue = dbrValue;
        this.metadata = metadata;
        this.disconnected = disconnected;
    }

    @Override
    public AlarmSeverity getAlarmSeverity() {
        if (disconnected)
            return AlarmSeverity.UNDEFINED;
        return DataUtils.fromEpics(dbrValue.getSeverity());
    }

    @Override
    public AlarmStatus getAlarmStatus() {
        return DataUtils.fromEpics(dbrValue.getStatus());
    }

    @Override
    public TimeStamp getTimeStamp() {
        if (dbrValue.getTimeStamp() == null)
            return null;
        
        return DataUtils.fromEpics(dbrValue.getTimeStamp());
    }

    @Override
    public Integer getTimeUserTag() {
        return null;
    }

    @Override
    public Double getLowerDisplayLimit() {
        return (Double) metadata.getLowerDispLimit();
    }

    @Override
    public Double getLowerCtrlLimit() {
        return (Double) metadata.getLowerCtrlLimit();
    }

    @Override
    public Double getLowerAlarmLimit() {
        return (Double) metadata.getLowerAlarmLimit();
    }

    @Override
    public Double getLowerWarningLimit() {
        return (Double) metadata.getLowerWarningLimit();
    }

    @Override
    public String getUnits() {
        return metadata.getUnits();
    }

    @Override
    public NumberFormat getFormat() {
        // TODO: this needs to be revised
        return NumberFormat.getNumberInstance();
    }

    @Override
    public Double getUpperWarningLimit() {
        return (Double) metadata.getUpperWarningLimit();
    }

    @Override
    public Double getUpperAlarmLimit() {
        return (Double) metadata.getUpperAlarmLimit();
    }

    @Override
    public Double getUpperCtrlLimit() {
        return (Double) metadata.getUpperCtrlLimit();
    }

    @Override
    public Double getUpperDisplayLimit() {
        return (Double) metadata.getUpperDispLimit();
    }

}
