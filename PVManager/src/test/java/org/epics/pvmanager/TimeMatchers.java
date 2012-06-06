/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager;

import org.epics.util.time.Timestamp;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 *
 * @author carcassi
 */
public class TimeMatchers {
    public static Matcher<Timestamp> within(final org.epics.util.time.TimeInterval operand) {
        return new BaseMatcher<Timestamp>() {

            @Override
            public boolean matches(Object o) {
                if (o instanceof Timestamp) {
                    return operand.contains((Timestamp) o);
                }
                return false;
            }

            @Override
            public void describeTo(Description d) {
                d.appendText("within ").appendValue(operand);
            }
        };
    }
}
