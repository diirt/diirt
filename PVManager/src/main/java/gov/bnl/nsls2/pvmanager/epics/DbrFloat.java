/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrFloat extends DbrScalar<Float> {
    float getFloat();
    void setFloat(float value);
}
