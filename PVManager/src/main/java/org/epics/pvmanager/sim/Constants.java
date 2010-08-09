/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Constants that can be used across different values.
 *
 * @author carcassi
 */
class Constants {
    static final Set<String> NO_ALARMS =
            Collections.emptySet();
    static final List<String> POSSIBLE_ALARM_STATUS =
            Collections.unmodifiableList(Arrays.asList("BROKEN_SIMULATOR"));
    static final NumberFormat DOUBLE_FORMAT = new DecimalFormat();
}
