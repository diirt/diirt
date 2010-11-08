/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 *
 * @author carcassi
 */
public class TimeMatchers {
    public static Matcher<TimeStamp> within(final TimeInterval operand) {
        return new BaseMatcher<TimeStamp>() {

            @Override
            public boolean matches(Object o) {
                if (o instanceof TimeStamp) {
                    return operand.contains((TimeStamp) o);
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
