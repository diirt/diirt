/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.vtype.io;

import java.io.StringReader;
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
    public void importList1() {
        String inputText = "10\n" +
                "8\n" +
                "6\n" +
                "4\n" +
                "2\n" +
                "0\n";
        
        VType value = TextIO.readList(new StringReader(inputText));
        assertThat(value, instanceOf(VNumberArray.class));
        VNumberArray array = (VNumberArray) value;
        assertThat(array.getData(), equalTo((ListNumber) new ArrayDouble(10,8,6,4,2,0)));
    }
    @Test
    public void importList2() {
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
}