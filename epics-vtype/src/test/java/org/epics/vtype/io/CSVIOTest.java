/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.vtype.io;

import java.io.StringWriter;
import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ArrayInt;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VEnum;
import org.epics.vtype.VEnumArray;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.vtype.ValueFactory.*;
import static org.epics.vtype.ValueUtil.*;

/**
 *
 * @author carcassi
 */
public class CSVIOTest {
    
    public CSVIOTest() {
    }

    @Test
    public void exportVNumber1() {
        VNumber value = ValueFactory.newVDouble(1.0, newTime(Timestamp.of(0, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:00:00.0 -0500\" NONE NONE 1.0");
    }

    @Test
    public void exportVNumber2() {
        VNumber value = ValueFactory.newVInt(10, alarmNone(), newTime(Timestamp.of(90, 0)), displayNone());
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:01:30.0 -0500\" NONE NONE 10.0");
    }

    @Test
    public void exportVString1() {
        VString value = ValueFactory.newVString("Hello world!", alarmNone(), newTime(Timestamp.of(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"Hello world!\"");
    }

    @Test
    public void exportVStringArray1() {
        VStringArray value = ValueFactory.newVStringArray(Arrays.asList("The first", "The second"), alarmNone(), newTime(Timestamp.of(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"The first\" \"The second\"");
    }

    @Test
    public void exportVEnum1() {
        VEnum value = ValueFactory.newVEnum(1, Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Timestamp.of(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"Two\"");
    }

    @Test
    public void exportVEnumArray() {
        VEnumArray value = ValueFactory.newVEnumArray(new ArrayInt(1,0,0,2), Arrays.asList("One", "Two", "Three"), alarmNone(), newTime(Timestamp.of(133, 0)));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"1969/12/31 19:02:13.0 -0500\" NONE NONE \"Two\" \"One\" \"One\" \"Three\"");
    }

    @Test
    public void exportVTable() {
        VTable value = ValueFactory.newVTable(Arrays.<Class<?>>asList(String.class, Double.TYPE, Integer.TYPE),
                                   Arrays.asList("Name", "Value", "Index"), 
                                   Arrays.<Object>asList(new String[] {"A", "B", "C", "D", "E"},
                                          new double[] {0.234, 1.456, 234567891234.0, 0.000000123, 123},
                                          new int[] {1,2,3,4,5}));
        CSVIO io = new CSVIO();
        exportTest(io, value, "\"Name\" \"Value\" \"Index\"\n" +
                "\"A\" 0.234 1\n" +
                "\"B\" 1.456 2\n" +
                "\"C\" 2.34567891234E11 3\n" +
                "\"D\" 1.23E-7 4\n" +
                "\"E\" 123.0 5\n");
    }
    
    public static void exportTest(CSVIO io, Object value, String csv) {
        assertThat(io.canExport(value), equalTo(true));
        StringWriter writer = new StringWriter();
        io.export(value, writer);
        System.out.println(writer.toString());
        assertThat(writer.toString(), equalTo(csv));
    }
}