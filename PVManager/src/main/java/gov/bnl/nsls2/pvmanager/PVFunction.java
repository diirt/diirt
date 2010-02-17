/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public abstract class PVFunction<T extends PVType<T>> {

    private Class<T> type;

    public PVFunction(Class<T> type) {
        this.type = type;
    }

    public abstract T getValue();
    public Class<T> getType() {
        return type;
    }
}
