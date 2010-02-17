/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.bnl.nsls2.pvmanager;

/**
 *
 * @author carcassi
 */
public class ValueCache<T extends PVType<T>> extends PVFunction<T> {

    private final T value;

    public ValueCache(Class<T> dataType) {
        super(dataType);
        this.value = PVType.newInstanceOf(dataType);
    }

    @Override
    public T getValue() {
        return value;
    }

}
