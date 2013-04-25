/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory All rights reserved. Use
 * is subject to license terms.
 */
package org.epics.vtype.io;

import java.io.StringWriter;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VNumber;
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
    
    public static void exportTest(CSVIO io, Object value, String csv) {
        assertThat(io.canExport(value), equalTo(true));
        StringWriter writer = new StringWriter();
        io.export(value, writer);
        System.out.println(writer.toString());
        assertThat(writer.toString(), equalTo(csv));
    }
}