/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class ValueCache<T> extends Function<T> {

    private T value;

    public ValueCache(Class<T> dataType) {
        super(dataType);
        this.value = null;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void setValue(T newValue) {
        this.value = newValue;
    }

}
