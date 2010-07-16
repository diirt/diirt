/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Limit information needed for control.
 *
 * @author carcassi
 */
public interface Ctrl<T extends Number> extends Gr<T> {
    @Metadata
    T getLowerCtrlLimit();
    @Metadata
    T getUpperCtrlLimit();
}
