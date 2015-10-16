/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.time;

import java.text.ParseException;
import java.util.TimeZone;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class TimestampFormatTest {

    public TimestampFormatTest() {
    }

    @Test
    public void formatTimestamp1() {
        // Test with milliseconds
        TimestampFormat format = new TimestampFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        Timestamp time = Timestamp.of(0, 30000000);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.30"));
    }

    @Test
    public void formatTimestamp2() {
        // Test with nanoseconds
        TimestampFormat format = new TimestampFormat("yyyy-MM-dd'T'HH:mm:ss.N");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        Timestamp time = Timestamp.of(0, 30000000);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.30000000"));
    }

    @Test
    public void spacedFormat() {
        TimestampFormat format = new TimestampFormat("yyyy-MM-dd'T'HH:mm:ss.NNNNNN");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        Timestamp time = Timestamp.of(0, 1);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.000001"));
        time = Timestamp.of(0, 12);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.000012"));
        time = Timestamp.of(0, 123);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.000123"));
        time = Timestamp.of(0, 1234);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.001234"));
        time = Timestamp.of(0, 12345);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.012345"));
        time = Timestamp.of(0, 123456);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.123456"));
        time = Timestamp.of(0, 1234567);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.1234567"));
        time = Timestamp.of(0, 12345678);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.12345678"));
    }

    @Test
    public void specialCases() {
        // Only nanoseconds
        TimestampFormat format = new TimestampFormat("NNN");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        Timestamp time = Timestamp.of(0, 12345);
        assertThat(format.format(time), equalTo("12345"));

        // Multiple nanoseconds with different format
        // and N as part of escaped text
        format = new TimestampFormat("NNNNN-'''N'''-N");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        time = Timestamp.of(0, 1);
        assertThat(format.format(time), equalTo("00001-'N'-1"));
    }

    @Test
    public void parse1() throws Exception {
        TimestampFormat format = new TimestampFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        Timestamp time = format.parse("1976-01-01T00:00:00");
        assertThat(time, equalTo(Timestamp.of(189302400, 0)));
    }

    @Test(expected=ParseException.class)
    public void parse2() throws Exception {
        TimestampFormat format = new TimestampFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        Timestamp time = format.parse("1976-NN-01T00:00:00");
    }

}