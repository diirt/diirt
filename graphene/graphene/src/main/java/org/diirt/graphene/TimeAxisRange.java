/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene;

import org.diirt.util.time.TimeInterval;

/**
 *
 * @author carcassi
 */
public interface TimeAxisRange {
    public TimeInterval axisRange(TimeInterval dataRange, TimeInterval aggregatedRange);
}
