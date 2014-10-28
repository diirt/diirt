/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.diirt.util.time.Timestamp;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 *
 * @author carcassi
 */
public class TimeMatchers {
    public static Matcher<Timestamp> within(final org.diirt.util.time.TimeInterval operand) {
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
