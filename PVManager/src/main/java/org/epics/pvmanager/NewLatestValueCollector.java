/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager;

/**
 * A collector that keeps only the latest value.
 *
 * @author carcassi
 */
public class NewLatestValueCollector<T> implements NewCollector<T, T> {
    
    private T value;

    @Override
    public void setValue(T newValue) {
        value = newValue;
    }

    @Override
    public T getValue() {
        return value;
    }
    
}
