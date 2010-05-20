/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * The information needed to visualize a particular value. The limits must
 * be given in terms of the same type of the scalar type, which needs
 * to be a number.
 *
 * @author carcassi
 */
public interface Gr<T extends Number> extends Scalar<T> {
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
