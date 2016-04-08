/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

import org.diirt.util.time.TimeInterval;

/**
 * Helper for unit tests.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class UnitTestUtils {

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");

    public static File getTestResource(String name)
            throws FileNotFoundException {
        return new File("src/test/resources" + name);
    }

    public static String readFile(File file) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (String line = br.readLine(); line != null; line = br.readLine())
            out.append(line + "\n");
        br.close();
        return out.toString();
    }

    public static void writeFile(String filename, String content)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(content);
        writer.close();
    }

    public static Instant timestampOf(String time) throws ParseException {
        if (time == null)
            return null;
        return dateFormat.parse(time).toInstant();
    }

    public static TimeInterval timeIntervalOf(String start, String end)
            throws ParseException {
        return TimeInterval.between(timestampOf(start), timestampOf(end));
    }

}
