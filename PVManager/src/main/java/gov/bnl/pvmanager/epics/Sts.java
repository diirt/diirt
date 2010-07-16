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
 * set or unset. This is implemented in Java by the use of EnumSets.
 *
 * @author carcassi
 */
public interface Sts {
    AlarmSeverity getAlarmSeverity();
    
    /**
     * Returns the set of alarm statuses that are currently active.
     *
     * @return a set of enabled alarms
     */
    Set<String> getAlarmStatus();
    
    /**
     * Defines all possible alarm statuses that are valid on this pv (better name?).
     * 
     * @return a set of labels
     */
    Set<String> getPossibleAlarms();
}
