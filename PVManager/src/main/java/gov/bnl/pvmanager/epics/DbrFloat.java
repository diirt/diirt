/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrFloat extends Scalar<Float> {
    float getFloat();
    void setFloat(float value);
}
