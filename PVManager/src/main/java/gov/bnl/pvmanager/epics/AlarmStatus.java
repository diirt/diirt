/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public enum AlarmStatus {
    BAD_SUB_ALARM, CALC_ALARM, COMM_ALARM, COS_ALARM, DISABLE_ALARM, HIGH_ALARM,
    HIHI_ALARM, HW_LIMIT_ALARM, LINK_ALARM, LOLO_ALARM,
    LOW_ALARM, NO_ALARM, READ_ACCESS_ALARM, READ_ALARM, SCAN_ALARM,
    SIMM_ALARM, SOFT_ALARM, STATE_ALARM, TIMEOUT_ALARM, UDF_ALARM,
    WRITE_ACCESS_ALARM, WRITE_ALARM;
}
