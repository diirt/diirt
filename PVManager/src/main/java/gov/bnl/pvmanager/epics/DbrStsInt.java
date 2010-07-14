/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Scalar integer with alarm. Auto-unboxing makes the extra method for the primitive type
 * unnecessary.
 *
 * @author carcassi
 */
public interface DbrStsInt extends Sts, Scalar<Integer> {
}
