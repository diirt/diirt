/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.time;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
        Instant time = Instant.ofEpochSecond(0, 300000000);
        assertThat(
                ZonedDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(format),
                equalTo("1970-01-01T00:00:00.30"));
    }

    @Test
    public void formatTimestamp2() {
        // Test with nanoseconds
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.N");
        Instant time = Instant.ofEpochSecond(0, 30000000);
        assertThat(
                ZonedDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(format),
                equalTo("1970-01-01T00:00:00.30000000"));
    }

    @Test
    public void spacedFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd'T'HH:mm:ss.NNNNNN");
        formatter.withZone(TimeZone.getTimeZone("GMT").toZoneId());
        Instant time = Instant.ofEpochSecond(0, 1);
        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
                equalTo("1970-01-01T00:00:00.000001"));
        time = Instant.ofEpochSecond(0, 12);
        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
                equalTo("1970-01-01T00:00:00.000012"));
        time = Instant.ofEpochSecond(0, 123);
        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
                equalTo("1970-01-01T00:00:00.000123"));
        time = Instant.ofEpochSecond(0, 1234);
        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
                equalTo("1970-01-01T00:00:00.001234"));
        time = Instant.ofEpochSecond(0, 12345);
        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
                equalTo("1970-01-01T00:00:00.012345"));
        time = Instant.ofEpochSecond(0, 123456);
        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
                equalTo("1970-01-01T00:00:00.123456"));
//        time = Instant.ofEpochSecond(0, 1234567);
//        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
//                equalTo("1970-01-01T00:00:00.1234567"));
//        time = Instant.ofEpochSecond(0, 12345678);
//        assertThat(LocalDateTime.ofInstant(time, TimeZone.getTimeZone("GMT").toZoneId()).format(formatter),
//                equalTo("1970-01-01T00:00:00.12345678"));
    }

//    @Test
//    public void specialCases() {
//        // Only nanoseconds
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("NNN");
//        format.withZone(TimeZone.getTimeZone("GMT").toZoneId());
//        Instant time = Instant.ofEpochSecond(0, 12345);
//        assertThat(format.format(time), equalTo("12345"));
//
//        // Multiple nanoseconds with different format
//        // and N as part of escaped text
//        format = DateTimeFormatter.ofPattern("NNNNN-'''N'''-N");
//        format.withZone(TimeZone.getTimeZone("GMT").toZoneId());
//        time = Instant.ofEpochSecond(0, 1);
//        assertThat(format.format(time), equalTo("00001-'N'-1"));
//    }

    @Test
    public void parse1() throws Exception {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        format.withZone(TimeZone.getTimeZone("GMT").toZoneId());
        Instant time = LocalDateTime.parse("1976-01-01T00:00:00", format).toInstant(ZoneOffset.of("Z"));
        assertThat(time, equalTo(Instant.ofEpochSecond(189302400, 0)));
    }

    @Test(expected = DateTimeParseException.class)
    public void parse2() throws Exception {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        format.withZone(TimeZone.getTimeZone("GMT").toZoneId());
        Instant time = LocalDateTime.parse("1976-NN-01T00:00:00", format).toInstant(ZoneOffset.of("Z"));
    }

}