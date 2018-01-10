/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.util;

import java.util.Arrays;
import java.util.List;
import org.diirt.util.array.ArrayDouble;
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

    @Test
    public void parseFunctionAnyParameters1() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo(1.0)");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", 1.0)));
    }

    @Test
    public void parseFunctionAnyParameters2() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo(\"test\")");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", "test")));
    }

    @Test
    public void parseFunctionAnyParameters3() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo(1,2,3)");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", 1.0, 2.0, 3.0)));
    }

    @Test
    public void parseFunctionAnyParameters4() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo(1,\"two\",3)");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", 1.0, "two", 3.0)));
    }

    @Test
    public void parseFunctionAnyParameters5() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo( 1, \"two\" , 3 )");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo", 1.0, "two", 3.0)));
    }

    @Test
    public void parseFunctionAnyParameters6() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo()");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo")));
    }

    @Test
    public void parseFunctionAnyParameters7() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo");
        assertThat(parameters, equalTo(Arrays.asList((Object) "foo")));
    }

    @Test
    public void parseFunctionAnyParameters8() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo[]");
        assertThat(parameters, nullValue());
    }

    @Test
    public void parseFunctionAnyParameters9() {
        List<Object> parameters = FunctionParser.parseFunctionAnyParameter("foo[]()");
        assertThat(parameters, nullValue());
    }

}