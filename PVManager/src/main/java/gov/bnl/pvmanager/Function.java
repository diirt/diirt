/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.pvmanager;

/**
 *
 * @author carcassi
 */
public abstract class Function<T> {

    private Class<T> type;

    public Function(Class<T> type) {
        this.type = type;
    }

    public abstract T getValue();
    public Class<T> getType() {
        return type;
    }
}
