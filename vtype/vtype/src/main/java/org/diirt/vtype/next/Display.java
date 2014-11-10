/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import java.text.NumberFormat;
import java.util.Objects;
import org.diirt.util.stats.Range;

/**
 * Limit and unit information needed for display and control.
 * <p>
 * The numeric limits are given in double precision no matter which numeric
 * type. The unit is a simple String, which can be empty if no unit information
 * is provided. The number format can be used to convert the value to a String.
 *
 * @author carcassi
 */
public abstract class Display {

    /**
     * The range for the value when displayed.
     * 
     * @return the display range; never null
     */
    public abstract Range getDisplayRange();

    /**
     * The range for the alarm associated to the value.
     * 
     * @return the alarm range; never null
     */
    public abstract Range getAlarmRange();

    /**
     * The range for the warning associated to the value.
     * 
     * @return the warning range; never null
     */
    public abstract Range getWarningRange();

    /**
     * The range used for changing the value.
     * 
     * @return the control range; never null
     */
    public abstract Range getControlRange();

    /**
     * String representation of the units using for all values.
     * Never null. If not available, returns the empty String.
     *
     * @return units
     */
    public abstract String getUnits();

    /**
     * Returns a NumberFormat that creates a String with just the value (no units).
     * Format is locale independent and should be used for all values (values and
     * min/max of the ranges). Never null.
     *
     * @return the default format for all values
     */
    public abstract NumberFormat getFormat();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
	if (obj instanceof Display) {
            Display other = (Display) obj;
        
            return Objects.equals(getFormat(), other.getFormat()) &&
                Objects.equals(getUnits(), other.getUnits()) &&
                Objects.equals(getDisplayRange(), other.getDisplayRange()) &&
                Objects.equals(getAlarmRange(), other.getAlarmRange()) &&
                Objects.equals(getWarningRange(), other.getWarningRange()) &&
                Objects.equals(getControlRange(), other.getControlRange());
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(getFormat());
        hash = 59 * hash + Objects.hashCode(getUnits());
        hash = 59 * hash + Objects.hashCode(getDisplayRange());
        hash = 59 * hash + Objects.hashCode(getAlarmRange());
        hash = 59 * hash + Objects.hashCode(getWarningRange());
        hash = 59 * hash + Objects.hashCode(getControlRange());
        return hash;
    }
}
