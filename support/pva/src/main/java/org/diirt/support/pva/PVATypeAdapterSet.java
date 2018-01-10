/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.pva;

import java.util.Collection;
import org.diirt.datasource.DataSourceTypeAdapterSet;

/**
 *
 * @author carcassi
 */
public interface PVATypeAdapterSet extends DataSourceTypeAdapterSet {

    @Override
    Collection<PVATypeAdapter> getAdapters();
}
