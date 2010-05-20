/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrInt extends Scalar<Integer> {
    int getInt();
    void setInt(int value);
}
