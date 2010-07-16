/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

import java.util.List;

/**
 * Multi channel array.
 *
 * @author carcassi
 */
public interface MultiChannel<T> {
    List<T> getValues();
}
