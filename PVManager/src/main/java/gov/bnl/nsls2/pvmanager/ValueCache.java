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

    private final Class<T> dataType;
    private final T value;

    public ValueCache(Class<T> dataType) {
        this.dataType = dataType;
        try {
            this.value = this.dataType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Can't create value of type " + dataType.getName(), e);
        }
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public Class<T> getType() {
        return dataType;
    }

}
