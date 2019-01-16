/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import static org.diirt.datasource.formula.FormulaAst.createParser;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.antlr.runtime.RecognitionException;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.epics.vtype.Alarm;
import org.epics.vtype.Display;
import org.epics.vtype.Time;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VDouble;
import org.epics.vtype.VInt;
import org.epics.vtype.VString;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class FormulaParserTest {

    @Test
    public void channel1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("'mychannel'").channel().toExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("mychannel"));
    }

    @Test
    public void channel2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("'mychannel'").channel().toExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("mychannel"));
    }

    @Test
    public void channel3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("'ca://mychannel'").channel().toExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("ca://mychannel"));
    }

    @Test
    public void numericLiteral1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("3").numericLiteral().toExpression();
        assertThat(exp, not(nullValue()));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3));
    }

    @Test
    public void numericLiteral2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("3.14").numericLiteral().toExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.14));
    }

    @Test
    public void stringLiteral1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("\"test\"").formula().toExpression();
        assertThat(exp, not(nullValue()));
        VString result = (VString) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo("test"));
    }

    @Test
    public void multiplicativeExpression1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2*3").multiplicativeExpression().toExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(2 * 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(6.0));
    }

    @Test
    public void multiplicativeExpression2() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("2*'x'").multiplicativeExpression().toExpression());
        assertThat(exp.getExpression().getName(), equalTo("(2 * x)"));
        exp.writeValue("x", createVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(20.0));

        exp.writeValue("x", createVDouble(5.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(10.0));
    }

    @Test
    public void multiplicativeExpression3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("10/2").multiplicativeExpression().toExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(10 / 2)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void multiplicativeExpression4() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("'x'/2").multiplicativeExpression().toExpression());
        assertThat(exp.getExpression().getName(), equalTo("(x / 2)"));
        exp.writeValue("x", createVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));

        exp.writeValue("x", createVDouble(20.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(10.0));
    }

    @Test
    public void multiplicativeExpression5() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("9%2").multiplicativeExpression().toExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void multiplicativeExpression6() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("'x'%4").multiplicativeExpression().toExpression());
        exp.writeValue("x", createVDouble(11.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));

        exp.writeValue("x", createVDouble(21.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void additiveExpression1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2+3").additiveExpression().toExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(2 + 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void additiveExpression2() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("2+'x'").additiveExpression().toExpression());
        exp.writeValue("x", createVDouble(10.0));
        assertThat(exp.getExpression().getName(), equalTo("(2 + x)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(12.0));

        exp.writeValue("x", createVDouble(5.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(7.0));
    }

    @Test
    public void additiveExpression3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2-3").additiveExpression().toExpression();
        assertThat(exp.getName(), equalTo("(2 - 3)"));
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-1.0));
    }

    @Test
    public void additiveExpression4() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("2-'x'").additiveExpression().toExpression());
        assertThat(exp.getExpression().getName(), equalTo("(2 - x)"));
        exp.writeValue("x", createVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-8.0));

        exp.writeValue("x", createVDouble(5.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-3.0));
    }

    @Test
    public void namedConstant1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("PI").formula().toExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(Math.PI));
    }

    @Test
    public void namedConstant2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("TRUE").formula().toExpression();
        assertThat(exp, not(nullValue()));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }
    
    /**
     * Create a VDouble using the value provided with no alarm, no display and with time now
     * @param value
     * @return VDouble
     */
    public VDouble createVDouble(double value) {
        return VDouble.of(value, Alarm.none(), Time.now(), Display.none());
    }
}
