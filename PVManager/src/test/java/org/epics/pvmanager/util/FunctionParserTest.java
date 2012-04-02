/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.util;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Test simulated pv function names parsing
 *
 * @author carcassi
 */
public class FunctionParserTest {

    public FunctionParserTest() {
    }

    @Test
    public void testParameterParsing() {
        // A couple of correct combinations
        List<Object> parameters = FunctionParser.parseParameters("1.0,2.0");
        assertThat(parameters, equalTo(Arrays.asList((Object) 1.0, 2.0)));
        parameters = FunctionParser.parseParameters("-1,.5,  23.25");
        assertThat(parameters, equalTo(Arrays.asList((Object) (-1.0), 0.5,  23.25)));
    }

    @Test
    public void testError1() {
        assertThat(FunctionParser.parseParameters("1.0 2.0"), nullValue());
    }

    @Test
    public void testError2() {
        assertThat(FunctionParser.parseParameters("1.O"), nullValue());
    }

    @Test
    public void testError3() {
        assertThat(FunctionParser.parseParameters("1.1.2"), nullValue());
    }

    @Test
    public void testError4() {
        assertThat(FunctionParser.parseFunction("test(1.0 2.0)"), nullValue());
    }

    @Test
    public void testError5() {
        assertThat(FunctionParser.parseFunction("test(1.O)"), nullValue());
    }

    @Test
    public void testError6() {
        assertThat(FunctionParser.parseFunction("test(1.1.2)"), nullValue());
    }

    @Test
    public void testError7() {
       assertThat( FunctionParser.parseFunction("test1.2"), nullValue());
    }

    @Test
    public void testParsing() {
        // Couple of correct functions
        List<Object> parameters = FunctionParser.parseFunction("sine(1.0,2.0)");
        assertThat(parameters, equalTo(Arrays.asList((Object) "sine",  1.0, 2.0)));
        parameters = FunctionParser.parseFunction("ramp(-1,.5,  23.25)");
        assertThat(parameters, equalTo(Arrays.asList((Object) "ramp", -1.0, 0.5,  23.25)));
        parameters = FunctionParser.parseFunction("replay(\"test.xml\")");
        assertThat(parameters, equalTo(Arrays.asList((Object) "replay", "test.xml")));
    }

}