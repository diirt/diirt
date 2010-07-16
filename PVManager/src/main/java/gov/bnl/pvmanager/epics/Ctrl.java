/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

/**
 * Limit information needed for control.
 *
 * @param <T> a {@link java.lang.Number} type
 * @author carcassi
 */
public interface Ctrl<T extends Number> extends Gr<T> {

    /**
     * Lowest possible value (included). Never null.
     * @return lower limit
     */
    @Metadata
    T getLowerCtrlLimit();

    /**
     * Highest possible value (included). Never null.
     * @return upper limit
     */
    @Metadata
    T getUpperCtrlLimit();
}
