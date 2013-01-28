/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.util;

import java.util.Arrays;
import java.util.List;
import org.epics.pvmanager.util.FunctionParser;
import org.epics.util.array.ArrayDouble;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test simulated pv function names parsing
 *
 * @author carcassi
 */
public class StringUtilTest {

    public StringUtilTest() {
    }

    @Test
    public void unescapeString1() {
        assertThat(StringUtil.unescapeString("\\\""), equalTo("\""));
        assertThat(StringUtil.unescapeString("\\\"hello\\\""), equalTo("\"hello\""));
    }

    @Test
    public void unescapeString2() {
        assertThat(StringUtil.unescapeString("\\\\"), equalTo("\\"));
        assertThat(StringUtil.unescapeString("path\\\\to\\\\file"), equalTo("path\\to\\file"));
    }

    @Test
    public void unescapeString3() {
        assertThat(StringUtil.unescapeString("\\\'"), equalTo("\'"));
        assertThat(StringUtil.unescapeString("That\\\'s right!"), equalTo("That\'s right!"));
    }

    @Test
    public void unescapeString4() {
        assertThat(StringUtil.unescapeString("\\r"), equalTo("\r"));
        assertThat(StringUtil.unescapeString("This\\rThat"), equalTo("This\rThat"));
    }

    @Test
    public void unescapeString5() {
        assertThat(StringUtil.unescapeString("\\n"), equalTo("\n"));
        assertThat(StringUtil.unescapeString("This\\nThat"), equalTo("This\nThat"));
    }

}