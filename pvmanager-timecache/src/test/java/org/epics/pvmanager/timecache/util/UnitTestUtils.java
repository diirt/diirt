/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
/*******************************************************************************
 * Copyright (c) 2010-2014 ITER Organization.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.epics.pvmanager.timecache.util;

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

import org.epics.util.time.TimeInterval;
import org.epics.util.time.Timestamp;

/**
 * Helper for unit tests.
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class UnitTestUtils {

	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm");

	public static File getTestResource(String name)
			throws FileNotFoundException {
		return new File("resources/test/" + name);
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

	public static Timestamp timestampOf(String time) throws ParseException {
		if (time == null)
			return null;
		return Timestamp.of(dateFormat.parse(time));
	}

	public static TimeInterval timeIntervalOf(String start, String end)
			throws ParseException {
		return TimeInterval.between(timestampOf(start), timestampOf(end));
	}

}
