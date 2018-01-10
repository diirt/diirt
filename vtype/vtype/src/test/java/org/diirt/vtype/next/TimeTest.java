/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.next;

import java.time.Duration;
import java.time.Instant;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class TimeTest {

    public TimeTest() {
    }

    @Test
    public void create1() {
        Instant timestamp = Instant.ofEpochSecond(123, 123);
        Time time = Time.create(timestamp, 4, false);
        assertThat(time.getTimestamp(), equalTo(timestamp));
        assertThat(time.getUserTag(), equalTo(4));
        assertThat(time.isValid(), equalTo(false));
        assertThat(time.toString(), equalTo("1970-01-01T00:02:03.000000123Z(4)"));
    }

    @Test
    public void now1() {
        Instant timestamp = Instant.now();
        Time time = Time.now();
        assertThat(time.getTimestamp(), lessThan(timestamp.plus(Duration.ofSeconds(1))));
        assertThat(time.getTimestamp(), greaterThan(timestamp.minus(Duration.ofSeconds(1))));
        assertThat(time.getUserTag(), nullValue());
        assertThat(time.isValid(), equalTo(true));
    }
    @Test
    public void equals1() {
        assertThat(Time.create(Instant.ofEpochSecond(123, 123), 4, false), equalTo(Time.create(Instant.ofEpochSecond(123, 123), 4, false)));
        Time time = Time.now();
        assertThat(time, equalTo(Time.create(time.getTimestamp(), null, true)));
        assertThat(time, not(equalTo(null)));
        assertThat(Time.create(Instant.ofEpochSecond(123, 123), 4, false), not(equalTo(Time.create(Instant.ofEpochSecond(123, 124), 4, false))));
        assertThat(Time.create(Instant.ofEpochSecond(123, 123), 4, false), not(equalTo(Time.create(Instant.ofEpochSecond(123, 123), 3, false))));
        assertThat(Time.create(Instant.ofEpochSecond(123, 123), 4, false), not(equalTo(Time.create(Instant.ofEpochSecond(123, 123), 4, true))));
    }

}
