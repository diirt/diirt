/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package gov.bnl.pvmanager.epics;

import java.text.NumberFormat;

/**
 * Limit and unit information needed for display. The limits must
 * be given in terms of the same type of the scalar type, which needs
 * to be a number.
 * <p>
 * Note: NumberFormats. If there is no direct write on metadata,
 * number formats can be used to provide a default way to convert the value
 * to a String.
 * <p>
 *
 * @author carcassi
 */
public interface Gr<T extends Number> extends Scalar<T> {
    @Metadata
    T getLowerAlarmLimit();
    @Metadata
    T getLowerDispLimit();
    @Metadata
    T getLowerWarningLimit();
    @Metadata
    String getUnits();
    /**
     * Returns a NumberFormat that creates a String with just the value (no units).
     * Format should be locale independent.
     */
    @Metadata
    NumberFormat getFormat();
    @Metadata
    T getUpperAlarmLimit();
    @Metadata
    T getUpperDispLimit();
    @Metadata
    T getUpperWarningLimit();
}
