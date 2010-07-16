/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

import java.util.Set;

/**
 * Alarm information.
 * <p>
 * The alarm status is represented by a set of status bits that could be either
 * set or unset. This is implemented in Java by the use of Sets.
 *
 * @author carcassi
 */
public interface Sts {

    /**
     * Returns the alarm severity, which describes the quality of the
     * value returned. Never null.
     *
     * @return the alarm severity
     */
    AlarmSeverity getAlarmSeverity();
    
    /**
     * Returns the set of alarm statuses that are currently active. Never null.
     *
     * @return a set of enabled alarms
     */
    Set<String> getAlarmStatus();
    
    /**
     * Defines all possible alarm statuses that are valid on this pv (better name?).
     *  Never null.
     * 
     * @return a set of labels
     */
    Set<String> getPossibleAlarms();
}
