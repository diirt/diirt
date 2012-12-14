/**
 * Copyright (C) 2012 University of Michigan
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.rrdtool;

import java.util.List;
import java.util.Map;
import org.epics.util.array.ListDouble;
import org.epics.util.time.Timestamp;

/**
 *
 * @author carcassi
 */
public class TimeSeries {
    
    private List<Timestamp> time;
    private ListDouble values;

    public TimeSeries(List<Timestamp> time, ListDouble  values) {
        this.time = time;
        this.values = values;
    }
    
    public List<Timestamp> getTime() {
        return time;
    }
    
    public ListDouble getValues() {
        return values;
    }
}
