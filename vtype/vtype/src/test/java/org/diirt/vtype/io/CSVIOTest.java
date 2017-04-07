/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.io;

import org.diirt.vtype.io.CSVIO;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;

import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ArrayInt;
import org.diirt.util.array.ListNumber;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VEnumArray;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.diirt.vtype.ValueFactory.*;

/**
 *
 * @author carcassi
 */
public class CSVIOTest {

    public CSVIOTest() {
    }

//    @Test
    public void exportVNumber1() {
        VNumber value = ValueFactory.newVDouble(1.0, newTime(Instant.ofEpochSecond(0, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:00:00.0 -0500\" NONE NONE 1.0");
    }

//    @Test
    public void exportVNumber2() {
        VNumber value = ValueFactory.newVInt(10, alarmNone(), newTime(Instant.ofEpochSecond(90, 0)), displayNone());
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:01:30.0 -0500\" NONE NONE 10.0");
    }

//    @Test
    public void exportVString1() {
        VString value = ValueFactory.newVString("Hello world!", alarmNone(), newTime(Instant.ofEpochSecond(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"Hello world!\"");
    }

//    @Test
    public void exportVStringArray1() {
        VStringArray value = ValueFactory.newVStringArray(Arrays.asList("The first", "The second"), alarmNone(), newTime(Instant.ofEpochSecond(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"The first\" \"The second\"");
    }

//    @Test
    public void exportVEnum1() {
        VEnum value = ValueFactory.newVEnum(1, Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Instant.ofEpochSecond(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"Two\"");
    }

//    @Test
    public void exportVEnumArray() {
        VEnumArray value = ValueFactory.newVEnumArray(new ArrayInt(1,0,0,2), Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Instant.ofEpochSecond(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"Two\" \"One\" \"One\" \"Three\"");
    }

    @Test
    public void exportVTable() {
        VTable value = ValueFactory.newVTable(Arrays.<Class<?>>asList(String.class, Double.TYPE, Integer.TYPE),
                                   Arrays.asList("Name", "Value", "Index"),
                                   Arrays.<Object>asList(Arrays.asList("A", "B", "C", "D", "E"),
                                          new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123),
                                          new ArrayInt(1,2,3,4,5)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"Name\" \"Value\" \"Index\"\n" +
                "\"A\" 0.234 1\n" +
                "\"B\" 1.456 2\n" +
                "\"C\" 2.34567891234E11 3\n" +
                "\"D\" 1.23E-7 4\n" +
                "\"E\" 123.0 5\n");
    }

//    @Test
    public void exportVTable2() {
        VTable value = ValueFactory.newVTable(Arrays.<Class<?>>asList(String.class, Double.TYPE, Integer.TYPE, Instant.class),
                                   Arrays.asList("Name", "Value", "Index", "Time"),
                                   Arrays.<Object>asList(Arrays.asList("A", "B", "C", "D", "E"),
                                          new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123),
                                          new ArrayInt(1,2,3,4,5),
                                          Arrays.asList(Instant.ofEpochSecond(133, 0),Instant.ofEpochSecond(134, 0),Instant.ofEpochSecond(135, 0),Instant.ofEpochSecond(136, 0),Instant.ofEpochSecond(137, 0))));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"Name\" \"Value\" \"Index\" \"Time\"\n" +
                "\"A\" 0.234 1 \"1969/12/31 19:02:13.0 -0500\"\n" +
                "\"B\" 1.456 2 \"1969/12/31 19:02:14.0 -0500\"\n" +
                "\"C\" 2.34567891234E11 3 \"1969/12/31 19:02:15.0 -0500\"\n" +
                "\"D\" 1.23E-7 4 \"1969/12/31 19:02:16.0 -0500\"\n" +
                "\"E\" 123.0 5 \"1969/12/31 19:02:17.0 -0500\"\n");
    }

    public static void exportTest(CSVIO io, Object value, String csv) {
        assertThat(io.canExport(value), equalTo(true));
        StringWriter writer = new StringWriter();
        io.export(value, writer);
        assertThat(writer.toString(), equalTo(csv));
    }

    @Test
    public void importVTable1() {
        String inputText = "\"Name\" \"Value\" \"Index\"\n" +
                "\"A\" 0.234 1\n" +
                "\"B\" 1.456 2\n" +
                "\"C\" 2.34567891234E11 3\n" +
                "\"D\" 1.23E-7 4\n" +
                "\"E\" 123.0 5\n";

        CSVIO io = new CSVIO();
        VTable value = io.importVTable(new StringReader(inputText));
        assertThat(value.getColumnCount(), equalTo(3));
        assertThat(value.getColumnName(0), equalTo("Name"));
        assertThat(value.getColumnName(1), equalTo("Value"));
        assertThat(value.getColumnName(2), equalTo("Index"));
        assertThat((Object) value.getColumnType(0), equalTo((Object) String.class));
        assertThat((Object) value.getColumnType(1), equalTo((Object) double.class));
        assertThat((Object) value.getColumnType(2), equalTo((Object) double.class));
        assertThat(value.getColumnData(0), equalTo((Object) Arrays.asList("A", "B", "C", "D", "E")));
        assertThat(value.getColumnData(1), equalTo((Object) new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123)));
        assertThat(value.getColumnData(2), equalTo((Object) new ArrayDouble(1,2,3,4,5)));
    }

    @Test
    public void importFileTable1CSV() throws Exception {
        CSVIO io = new CSVIO();
        VTable value = io.importVTable(new InputStreamReader(getClass().getResource("table1.csv").openStream()));
        assertThat(value.getColumnCount(), equalTo(3));
        assertThat(value.getColumnName(0), equalTo("Name"));
        assertThat(value.getColumnName(1), equalTo("Value"));
        assertThat(value.getColumnName(2), equalTo("Index"));
        assertThat((Object) value.getColumnType(0), equalTo((Object) String.class));
        assertThat((Object) value.getColumnType(1), equalTo((Object) double.class));
        assertThat((Object) value.getColumnType(2), equalTo((Object) double.class));
        assertThat(value.getColumnData(0), equalTo((Object) Arrays.asList("A", "B", "C", "D", "E")));
        assertThat(value.getColumnData(1), equalTo((Object) new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123)));
        assertThat(value.getColumnData(2), equalTo((Object) new ArrayDouble(1,2,3,4,5)));
    }

    @Test
    public void importFileTable2CSV() throws Exception {
        CSVIO io = new CSVIO();
        VTable value = io.importVTable(new InputStreamReader(getClass().getResource("table2.csv").openStream()));
        assertThat(value.getColumnCount(), equalTo(3));
        assertThat(value.getColumnName(0), equalTo("Name"));
        assertThat(value.getColumnName(1), equalTo("Value"));
        assertThat(value.getColumnName(2), equalTo("Index"));
        assertThat((Object) value.getColumnType(0), equalTo((Object) String.class));
        assertThat((Object) value.getColumnType(1), equalTo((Object) double.class));
        assertThat((Object) value.getColumnType(2), equalTo((Object) double.class));
        assertThat(value.getColumnData(0), equalTo((Object) Arrays.asList("A", "B", "C", "D", "E")));
        assertThat(value.getColumnData(1), equalTo((Object) new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123)));
        assertThat(value.getColumnData(2), equalTo((Object) new ArrayDouble(1,2,3,4,5)));
    }

    @Test
    public void importFileTable4CSV() throws Exception {
        CSVIO io = new CSVIO();
        VTable value = io.importVTable(new InputStreamReader(getClass().getResource("table4.csv").openStream()));
        assertThat(value.getColumnCount(), equalTo(13));
        assertThat(value.getColumnName(0), equalTo("timestamp"));
        assertThat(value.getColumnName(1), equalTo("rta_MIN"));
        assertThat(value.getColumnName(2), equalTo("rta_MAX"));
        assertThat((Object) value.getColumnType(0), equalTo((Object) double.class));
        assertThat((Object) value.getColumnType(1), equalTo((Object) double.class));
        assertThat((Object) value.getColumnType(2), equalTo((Object) double.class));
        assertThat(((ListNumber) value.getColumnData(0)).getDouble(0), equalTo(1390913220.0));
        assertThat(((ListNumber) value.getColumnData(1)).getDouble(1), equalTo(0.28083333333));
        assertThat(((ListNumber) value.getColumnData(2)).getDouble(2), equalTo(0.266825));
    }
}