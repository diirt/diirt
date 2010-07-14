/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Enumeration for severity of alarm.
 *
 * @author carcassi
 */
public enum AlarmSeverity {
    NONE, MINOR, MAJOR, INVALID,
    /**
     * Can't read the record. Either disconnected or connected with no
     * read access.
     */
    UNDEFINED;
}
