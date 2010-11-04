/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.jca;

import gov.aps.jca.dbr.Severity;
import gov.aps.jca.dbr.Status;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.epics.pvmanager.data.AlarmSeverity;
import org.epics.pvmanager.data.AlarmStatus;

/**
 * Utilities to convert JCA types to VData types.
 *
 * @author carcassi
 */
class DataUtils {

    private static final Logger log = Logger.getLogger(DataUtils.class.getName());
    private static final Map<gov.aps.jca.dbr.Status, AlarmStatus> statusConverter;
    
    static {
        // Prepares the map to convert from JCA status to the
        // standard status
        Map<gov.aps.jca.dbr.Status, AlarmStatus> newMap = new HashMap<Status, AlarmStatus>();
        newMap.put(Status.NO_ALARM, AlarmStatus.NONE);
        newMap.put(Status.READ_ALARM, AlarmStatus.DRIVER);
        newMap.put(Status.WRITE_ALARM, AlarmStatus.DRIVER);
        newMap.put(Status.HIHI_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.HIGH_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.LOLO_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.LOW_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.STATE_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.COS_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.COMM_ALARM, AlarmStatus.DRIVER);
        newMap.put(Status.TIMEOUT_ALARM, AlarmStatus.DRIVER);
        newMap.put(Status.HW_LIMIT_ALARM, AlarmStatus.DEVICE);
        newMap.put(Status.CALC_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.SCAN_ALARM, AlarmStatus.DB);
        newMap.put(Status.LINK_ALARM, AlarmStatus.DB);
        newMap.put(Status.SOFT_ALARM, AlarmStatus.CONF);
        newMap.put(Status.BAD_SUB_ALARM, AlarmStatus.CONF);
        newMap.put(Status.UDF_ALARM, AlarmStatus.UNDEFINED);
        newMap.put(Status.DISABLE_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.SIMM_ALARM, AlarmStatus.RECORD);
        newMap.put(Status.READ_ACCESS_ALARM, AlarmStatus.DRIVER);
        newMap.put(Status.WRITE_ACCESS_ALARM, AlarmStatus.DRIVER);
        statusConverter = Collections.unmodifiableMap(newMap);
    }

    /**
     * Converts an alarm severity from JCA to VData.
     *
     * @param severity the JCA severity
     * @return the VData severity
     */
    static AlarmSeverity fromEpics(Severity severity) {
        if (Severity.NO_ALARM.isEqualTo(severity)) {
            return AlarmSeverity.NONE;
        } else if (Severity.MINOR_ALARM.isEqualTo(severity)) {
            return AlarmSeverity.MINOR;
        } else if (Severity.MAJOR_ALARM.isEqualTo(severity)) {
            return AlarmSeverity.MAJOR;
        } else if (Severity.INVALID_ALARM.isEqualTo(severity)) {
            return AlarmSeverity.INVALID;
        } else {
            return AlarmSeverity.UNDEFINED;
        }
    }

    // Creates the list of EPICS 3 status by
    // iterating over all Status defined in JCA
    private static final List<String> epicsPossibleStatus;
    static {
        List<String> mutableList = new ArrayList<String>();
        Status aStatus = Status.forValue(0);
        while (aStatus != null) {
            mutableList.add(aStatus.getName());
            aStatus = Status.forValue(aStatus.getValue() + 1);
        }
        epicsPossibleStatus = Collections.unmodifiableList(mutableList);
    }

    /**
     * Returns all the possible EPICS 3 status.
     *
     * @return all possible status
     */
    static List<String> epicsPossibleStatus() {
        return epicsPossibleStatus;
    }

    /**
     * Converts a JCA status to a VData status.
     *
     * @param status JCA status
     * @return VData status
     */
    static AlarmStatus fromEpics(Status status) {
        if (status == null) {
            log.log(Level.INFO, "Received null alarm status", new Exception());
            return AlarmStatus.NONE;
        }
        return statusConverter.get(status);
    }

}
