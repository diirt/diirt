/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.types;

import org.epics.pvmanager.TimeStamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data class that represents an array of different pvs where their values
 * have been collected at a given timestamp.
 *
 * @author carcassi
 */
public class SynchronizedArray<T> {
    private TimeStamp timeStamp;
    private List<String> names = new ArrayList<String>();
    private List<T> values = new ArrayList<T>();

    public int getNElements() {
        return names.size();
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = Collections.unmodifiableList(new ArrayList<String>(names));
        values = new ArrayList<T>(Collections.nCopies(names.size(), (T) null));
    }

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(TimeStamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<T> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return timeStamp + " " + values;
    }

}
