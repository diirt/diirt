/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrDouble extends DbrScalar<Double>{
    double getDouble();
    void setDouble(double value);
}
