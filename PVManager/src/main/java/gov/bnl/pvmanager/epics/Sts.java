/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    void setAlarmSeverity(AlarmSeverity alarmSeverity);
    /**
     * Returns the set of alarm statuses that are currently active.
     *
     * @return a set of enabled alarms
     */
    Set<String> getAlarmStatus();
    void setAlarmStatus(Set<String> alarmStatus);
    
    /**
     * Defines all possible alarm statuses that are valid on this pv (better name?).
     * 
     * @return a set of labels
     */
    Set<String> getPossibleAlarms();
    void setPossibleAlarms(Set<String> possibleAlarms);
}
