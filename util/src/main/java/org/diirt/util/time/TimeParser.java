/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.util.time;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A helper class to parse user defined time strings to absolute or relative
 * time durations.
 *
 * The following return a TimeInterval - absolute
 *
 * "last min", "last hour", "last day", "last week"
 *
 * "last 5 mins", "last 5 hours", "last 5 days", "last 5 weeks"
 *
 * "5 mins ago", "5 hours ago", "5 days ago", "5 weeks ago"
 *
 * The following returns a Instant "now"
 *
 * The following returns a Duration - relative
 *
 * "last min", "last hour", "last day", "last week"
 *
 * "last 5 mins", "last 5 hours", "last 5 days", "last 5 weeks"
 *
 * "5 mins ago", "5 hours ago", "5 days ago", "5 weeks ago"
 *
 *
 * @author shroffk
 */
public class TimeParser {

    public static Duration getDuration(String time) {
        // TODO this regular expression needs to be reviewed and improved if
        // possible
        int quantity = 0;
        String unit = "";

        Pattern lastNUnitsPattern = Pattern
                .compile(
                        "last\\s*(\\d*)\\s*(min|mins|hour|hours|day|days|week|weeks).*",
                        Pattern.CASE_INSENSITIVE);
        Matcher lastNUnitsMatcher = lastNUnitsPattern.matcher(time);
        while (lastNUnitsMatcher.find()) {
            quantity = "".equals(lastNUnitsMatcher.group(1)) ? 1 : Integer
                    .valueOf(lastNUnitsMatcher.group(1));
            unit = lastNUnitsMatcher.group(2);
        }

        Pattern nUnitsAgoPattern = Pattern.compile(
                "(\\d*)\\s*(min|mins|hour|hours|day|days|week|weeks)\\s*ago",
                Pattern.CASE_INSENSITIVE);
        Matcher nUnitsAgoMatcher = nUnitsAgoPattern.matcher(time);
        while (nUnitsAgoMatcher.find()) {
            quantity = "".equals(nUnitsAgoMatcher.group(1)) ? 1 : Integer
                    .valueOf(nUnitsAgoMatcher.group(1));
            unit = nUnitsAgoMatcher.group(2);
        }
        unit = unit.toLowerCase();
        switch (unit) {
        case "min":
        case "mins":
            return Duration.ofMinutes(quantity);
        case "hour":
        case "hours":
            return Duration.ofHours(quantity);
        case "day":
        case "days":
            return Duration.ofHours(quantity * 24);
        case "week":
        case "weeks":
            return Duration.ofHours(quantity * 24 * 7);
        default:
            break;
        }
        return null;
    }

    public static TimeInterval getTimeInterval(String time) {
        return getTimeInterval(time, "now");
    }

    public static TimeInterval getTimeInterval(String start, String end) {
        return TimeInterval.between(getInstant(start), getInstant(end));
    }

    /**
     * A Helper function to help you convert various string represented time
     * definition to an absolute Instant.
     *
     * @param time
     *            a string represent the time
     * @return the parsed Instant or null
     */
    public static Instant getInstant(String time) {
        if (time.equalsIgnoreCase("now")) {
            return Instant.now();
        } else {
            int quantity = 0;
            String unit = "";
            Pattern lastNUnitsPattern = Pattern
                    .compile(
                            "last\\s*(\\d*)\\s*(min|mins|hour|hours|day|days|week|weeks).*",
                            Pattern.CASE_INSENSITIVE);
            Matcher lastNUnitsMatcher = lastNUnitsPattern.matcher(time);
            while (lastNUnitsMatcher.find()) {
                quantity = "".equals(lastNUnitsMatcher.group(1)) ? 1 : Integer
                        .valueOf(lastNUnitsMatcher.group(1));
                unit = lastNUnitsMatcher.group(2).toLowerCase();
                switch (unit) {
                case "min":
                case "mins":
                    return Instant.now().minus(Duration.ofMinutes(quantity));
                case "hour":
                case "hours":
                    return Instant.now().minus(Duration.ofHours(quantity));
                case "day":
                case "days":
                    return Instant.now().minus(Duration.ofHours(quantity * 24));
                case "week":
                case "weeks":
                    return Instant.now().minus(
                            Duration.ofHours(quantity * 24 * 7));
                default:
                    break;
                }
            }

            Pattern nUnitsAgoPattern = Pattern
                    .compile(
                            "(\\d*)\\s*(min|mins|hour|hours|day|days|week|weeks)\\s*ago",
                            Pattern.CASE_INSENSITIVE);
            Matcher nUnitsAgoMatcher = nUnitsAgoPattern.matcher(time);
            while (nUnitsAgoMatcher.find()) {
                quantity = "".equals(nUnitsAgoMatcher.group(1)) ? 1 : Integer
                        .valueOf(nUnitsAgoMatcher.group(1));
                unit = nUnitsAgoMatcher.group(2).toLowerCase();
                switch (unit) {
                case "min":
                case "mins":
                    return Instant.now().minus(Duration.ofMinutes(quantity));

                case "hour":
                case "hours":
                    return Instant.now().minus(Duration.ofHours(quantity));
                case "day":
                case "days":
                    return Instant.now().minus(Duration.ofHours(quantity * 24));
                case "week":
                case "weeks":
                    return Instant.now().minus(
                            Duration.ofHours(quantity * 24 * 7));
                default:
                    break;
                }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            return LocalDateTime.parse(time, formatter).atZone(TimeZone.getDefault().toZoneId()).toInstant();
        }
    }
}
