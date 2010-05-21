/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

import java.util.EnumSet;

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
    EnumSet<AlarmStatus> getAlarmStatus();
    void setAlarmStatus(EnumSet<AlarmStatus> alarmStatus);
}
