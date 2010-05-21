/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Limit and unit information needed for display. The limits must
 * be given in terms of the same type of the scalar type, which needs
 * to be a number.
 *
 * @author carcassi
 */
public interface Gr<T extends Number> extends Scalar<T> {
    @Metadata
    T getLowerAlarmLimit();
    @Metadata
    T getLowerDispLimit();
    @Metadata
    T getLowerWarningLimit();
    @Metadata
    String getUnits();
    @Metadata
    T getUpperAlarmLimit();
    @Metadata
    T getUpperDispLimit();
    @Metadata
    T getUpperWarningLimit();
    void setLowerAlarmLimit(T lowerAlarmLimit);
    void setLowerDispLimit(T lowerDispLimit);
    void setLowerWarningLimit(T lowerWarningLimit);
    void setUnits(String units);
    void setUpperAlarmLimit(T upperAlarmLimit);
    void setUpperDispLimit(T upperDispLimit);
    void setUpperWarningLimit(T upperWarningLimit);
}
