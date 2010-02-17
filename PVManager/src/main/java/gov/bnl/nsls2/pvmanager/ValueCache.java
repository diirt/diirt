/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
class ValueCache<T extends PVType<T>> extends PVFunction<T> {

    private final T value;

    ValueCache(Class<T> dataType) {
        super(dataType);
        this.value = PVType.newInstanceOf(dataType);
    }

    @Override
    public T getValue() {
        return value;
    }

}
