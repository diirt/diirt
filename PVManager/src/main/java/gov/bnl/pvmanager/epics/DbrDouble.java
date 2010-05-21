/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Scalar double. Adds a convenience method for the primitive type, which
 * returns 0.0 in case it never connected.
 *
 * @author carcassi
 */
public interface DbrDouble extends Scalar<Double>{
    double getDouble();
    void setDouble(double value);
}
