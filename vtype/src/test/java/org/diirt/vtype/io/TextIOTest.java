/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.vtype.io;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import org.epics.vtype.VNumberArray;
import org.epics.vtype.VStringArray;
import org.epics.vtype.VType;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class TextIOTest {

    @Test
    public void readList1() throws Exception {
        String inputText = "10\n" +
                "8\n" +
                "6\n" +
                "4\n" +
                "2\n" +
                "0\n";

        VType value = TextIO.readList(new StringReader(inputText));
        assertThat(value, instanceOf(VNumberArray.class));
        VNumberArray array = (VNumberArray) value;
        assertThat(array.getData(), equalTo((ListNumber) ArrayDouble.of(10,8,6,4,2,0)));
    }

    @Test
    public void readList2() throws Exception {
        String inputText = "A\n" +
                "B\n" +
                "C\n" +
                "D\n" +
                "E\n" +
                "F\n";

        VType value = TextIO.readList(new StringReader(inputText));
        assertThat(value, instanceOf(VStringArray.class));
        VStringArray array = (VStringArray) value;
        assertThat(array.getData(), equalTo(Arrays.asList("A", "B", "C", "D", "E", "F")));
    }

    @Test
    public void writeList1() throws Exception{
        VType vType = VType.toVType(ArrayDouble.of(10, 8, 6, 4, 2, 0));
        StringWriter writer = new StringWriter();
        TextIO.writeList(vType, writer);
        writer.flush();
        assertThat(writer.toString(), equalTo("10.0" + System.lineSeparator() +
                "8.0" + System.lineSeparator() +
                "6.0" + System.lineSeparator() +
                "4.0" + System.lineSeparator() +
                "2.0" + System.lineSeparator() +
                "0.0" + System.lineSeparator()));
    }

    @Test
    public void writeList2() throws Exception{
        VType vType = VType.toVType(Arrays.asList("A", "B", "C", "D", "E"));
        StringWriter writer = new StringWriter();
        TextIO.writeList(vType, writer);
        writer.flush();
        assertThat(writer.toString(), equalTo("A" + System.lineSeparator() +
                "B" + System.lineSeparator() +
                "C" + System.lineSeparator() +
                "D" + System.lineSeparator() +
                "E" + System.lineSeparator()));
    }
}