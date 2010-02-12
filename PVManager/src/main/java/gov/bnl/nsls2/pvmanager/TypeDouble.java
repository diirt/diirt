/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class TypeDouble extends PVType {

    private double value;

    public double getDouble() {
        return value;
    }

    public void setDouble(double value) {
        if (value != this.value) {
            this.value = value;
            firePvValueChanged();
        }
    }

}
