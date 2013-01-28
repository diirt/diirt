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
        parameters = FunctionParser.parsePvAndArguments("test(\"A\",\"B\")");
        assertThat(parameters, equalTo(Arrays.asList((Object) "test", "A", "B")));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments1() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(1.0)", "error");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", 1.0)));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments2() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(\"test\")", "error");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", "test")));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments3() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(1,2,3)", "error");
        assertThat(parameters.get(0), equalTo((Object) "foo"));
        assertThat(parameters.get(1), equalTo((Object) new ArrayDouble(1.0,2.0,3.0)));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments4() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(\"a\", \"b\", \"c\")", "error");
        assertThat(parameters.get(0), equalTo((Object) "foo"));
        assertThat(parameters.get(1), equalTo((Object) Arrays.asList("a", "b", "c")));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments5() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(\"It\\\'s \\u0061n escaped parameter\")", "error");
        assertThat(parameters.get(0), equalTo((Object) "foo"));
        assertThat(parameters.get(1), equalTo((Object) "It\'s an escaped parameter"));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments6() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(\"\\u0061\", \"b\", \"\\u0063\")", "error");
        assertThat(parameters.get(0), equalTo((Object) "foo"));
        assertThat(parameters.get(1), equalTo((Object) Arrays.asList("a", "b", "c")));
    }

    @Test
    public void parseFunctionWithScalarOrArrayArguments7() {
        List<Object> parameters = FunctionParser.parseFunctionWithScalarOrArrayArguments("foo(\"I said \\\"Right!\\\"\")", "error");
        assertThat(parameters.get(0), equalTo((Object) "foo"));
        assertThat(parameters.get(1), equalTo((Object) "I said \"Right!\""));
    }

}