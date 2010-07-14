/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager.epics;

/**
 * Scalar double with alarm. Auto-unboxing makes the extra method for the primitive type
 * unnecessary.
 *
 * @author carcassi
 */
public interface DbrStsDouble extends Sts, Scalar<Double> {
}
