/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.query;

import org.diirt.datasource.timecache.Parameter;
import org.diirt.util.time.TimeRelativeInterval;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class QueryParameters {

    TimeRelativeInterval timeInterval;
    public Parameter config = Parameter.Default;

    public QueryParameters timeInterval(TimeRelativeInterval timeInterval) {
        this.timeInterval = timeInterval;
        return this;
    }

}
