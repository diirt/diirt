/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 * Payload for PV of type double.
 *
 * @author carcassi
 */
public class TypeDouble extends PVType<TypeDouble> {

    private double value;

    /**
     * Returns the double value;
     * @return the value
     */
    public double getDouble() {
        return value;
    }

    /**
     * Changes the double value;
     * @param value the new value
     */
    public void setDouble(double value) {
        if (value != this.value) {
            this.value = value;
                firePvValueChanged();
            }
        }

    @Override
    void setTo(TypeDouble newValue) {
        synchronized (newValue) {
            if (newValue.value != value) {
                value = newValue.value;
                firePvValueChanged();
            }
        }
    }


}
