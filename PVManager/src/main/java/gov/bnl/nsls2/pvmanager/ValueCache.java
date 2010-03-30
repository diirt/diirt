/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
class ValueCache<T> extends PVFunction<T> {

    private T value;

    ValueCache(Class<T> dataType) {
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
