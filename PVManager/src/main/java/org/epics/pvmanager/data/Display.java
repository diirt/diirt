/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.data;

import java.text.NumberFormat;

/**
 * Limit and unit information needed for display and control.
 * <p>
 * The numeric limits are given in double precision no matter which numeric
 * type. The unit is a simple String, which can be empty if no unit information
 * is provided. The number format can be used to convert the value to a String.
 *
 * @author carcassi
 */
public interface Display {

    /**
     * Lowest possible value to be displayed. Never null.
     *
     * @return lower display limit
     */
    @Metadata
    Double getLowerDisplayLimit();

    /**
     * Lowest possible value (included). Never null.
     * 
     * @return lower limit
     */
    @Metadata
    Double getLowerCtrlLimit();

    /**
     * Lowest value before the alarm region. Never null.
     *
     * @return lower alarm limit
     */
    @Metadata
    Double getLowerAlarmLimit();

    /**
     * Lowest value before the warning region. Never null.
     *
     * @return lower warning limit
     */
    @Metadata
    Double getLowerWarningLimit();

    /**
     * String representation of the units using for all values.
     * Never null. If not available, returns the empty String.
     *
     * @return units
     */
    @Metadata
    String getUnits();

    /**
     * Returns a NumberFormat that creates a String with just the value (no units).
     * Format is locale independent and should be used for all values (values and
     * lower/upper limits). Never null.
     *
     * @return the default format for all values
     */
    @Metadata
    NumberFormat getFormat();

    /**
     * Highest value before the warning region. Never null.
     *
     * @return upper warning limit
     */
    @Metadata
    Double getUpperWarningLimit();

    /**
     * Highest value before the alarm region. Never null.
     *
     * @return upper alarm limit
     */
    @Metadata
    Double getUpperAlarmLimit();

    /**
     * Highest possible value (included). Never null.
     * @return upper limit
     */
    @Metadata
    Double getUpperCtrlLimit();

    /**
     * Highest possible value to be displayed. Never null.
     *
     * @return upper display limit
     */
    @Metadata
    Double getUpperDisplayLimit();
}
