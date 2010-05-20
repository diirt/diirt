/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

import java.util.EnumSet;

/**
 *
 * @author carcassi
 */
public interface DbrSts {
    AlarmSeverity getAlarmSeverity();
    void setAlarmSeverity(AlarmSeverity alarmSeverity);
    EnumSet<AlarmStatus> getAlarmStatus();
    void setAlarmStatus(EnumSet<AlarmStatus> alarmStatus);
}
