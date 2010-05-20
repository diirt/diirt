/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrGr<T extends Number> extends DbrScalar<T> {
    T getLowerAlarmLimit();
    T getLowerDispLimit();
    T getLowerWarningLimit();
    String getUnits();
    T getUpperAlarmLimit();
    T getUpperDispLimit();
    T getUpperWarningLimit();
    void setLowerAlarmLimit(T lowerAlarmLimit);
    void setLowerDispLimit(T lowerDispLimit);
    void setLowerWarningLimit(T lowerWarningLimit);
    void setUnits(String units);
    void setUpperAlarmLimit(T upperAlarmLimit);
    void setUpperDispLimit(T upperDispLimit);
    void setUpperWarningLimit(T upperWarningLimit);
}
