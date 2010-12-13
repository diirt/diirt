/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.util;

import java.util.TimeZone;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class TimeStampFormatTest {

    public TimeStampFormatTest() {
    }

    @Test
    public void formatTimeStamp() {
        // Test with milliseconds
        TimeStampFormat format = new TimeStampFormat("yyyy-MM-dd'T'HH:mm:ss.S");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        TimeStamp time = TimeStamp.time(0, 30000000);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.30"));

        // Test with nanoseconds
        format = new TimeStampFormat("yyyy-MM-dd'T'HH:mm:ss.N");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        time = TimeStamp.time(0, 30000000);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.30000000"));
    }

    @Test
    public void spacedFormat() {
        TimeStampFormat format = new TimeStampFormat("yyyy-MM-dd'T'HH:mm:ss.NNNNNN");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        TimeStamp time = TimeStamp.time(0, 1);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.000001"));
        time = TimeStamp.time(0, 12);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.000012"));
        time = TimeStamp.time(0, 123);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.000123"));
        time = TimeStamp.time(0, 1234);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.001234"));
        time = TimeStamp.time(0, 12345);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.012345"));
        time = TimeStamp.time(0, 123456);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.123456"));
        time = TimeStamp.time(0, 1234567);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.1234567"));
        time = TimeStamp.time(0, 12345678);
        assertThat(format.format(time), equalTo("1970-01-01T00:00:00.12345678"));
    }

    @Test
    public void specialCases() {
        // Only nanoseconds
        TimeStampFormat format = new TimeStampFormat("NNN");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        TimeStamp time = TimeStamp.time(0, 12345);
        assertThat(format.format(time), equalTo("12345"));

        // Multiple nanoseconds with different format
        // and N as part of escaped text
        format = new TimeStampFormat("NNNNN-'''N'''-N");
        format.setTimeZome(TimeZone.getTimeZone("GMT"));
        time = TimeStamp.time(0, 1);
        assertThat(format.format(time), equalTo("00001-'N'-1"));
    }

}