/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import java.time.Instant;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 *
 * @author carcassi
 */
public class TimeMatchers {
    public static Matcher<Instant> within(final org.diirt.util.time.TimeInterval operand) {
        return new BaseMatcher<Instant>() {

            @Override
            public boolean matches(Object o) {
                if (o instanceof Instant) {
                    return operand.contains((Instant) o);
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
