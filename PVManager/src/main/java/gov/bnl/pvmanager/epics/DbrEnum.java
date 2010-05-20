/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 *
 * @author carcassi
 */
public interface DbrEnum extends DbrScalar<Integer> {
    int getInt();
    void setInt(int value);
}
