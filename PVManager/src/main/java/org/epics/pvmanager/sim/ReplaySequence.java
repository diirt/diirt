/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimeStamp;
import org.epics.pvmanager.TypeSupport;

/**
 *
 * @author carcassi
 */
class ReplaySequence<T> {

    private List<Item> sequence = new ArrayList<Item>();
    private TimeStamp lastTime;

    /**
     * Adds a new value to the sequence.
     *
     * @param value
     */
    void addItem(T value) {
        TimeStamp newTime = TypeSupport.timestampOf(value);
        TimeDuration timeFromPreviousItem;
        if (lastTime == null) {
            lastTime = newTime;
            timeFromPreviousItem = TimeDuration.ms(0);
        } else {
            timeFromPreviousItem = newTime.durationFrom(lastTime);
        }
        sequence.add(new Item(sequence.size(), timeFromPreviousItem, value));
    }

    List<Item> getItems() {
        return Collections.unmodifiableList(sequence);
    }

    class Item {
        /**
         * Position in the sequence
         */
        final int sequenceNumber;

        /**
         * Time between previous item and this
         */
        final TimeDuration timeFromPreviousItem;

        /**
         * Value
         */
        final T value;

        private Item(int sequenceNumber, TimeDuration timeFromPreviousItem, T value) {
            this.sequenceNumber = sequenceNumber;
            this.timeFromPreviousItem = timeFromPreviousItem;
            this.value = value;
        }
    }
}
