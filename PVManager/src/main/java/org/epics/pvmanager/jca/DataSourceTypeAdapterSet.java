/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import java.util.Collection;

/**
 *
 * @author carcassi
 */
public interface DataSourceTypeAdapterSet {
    Collection<? extends DataSourceTypeAdapter<?, ?>> getConverters();
}
