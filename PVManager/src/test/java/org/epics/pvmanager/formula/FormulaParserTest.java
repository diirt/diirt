/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.antlr.runtime.*;
import org.epics.pvmanager.ReadExpressionTester;
import org.epics.pvmanager.data.*;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.formula.ExpressionLanguage.*;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.util.array.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class FormulaParserTest {

    @Test
    public void pv1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("mypv").pv();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("mypv"));
    }

    @Test
    public void pv2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("'mypv'").pv();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("mypv"));
    }

    @Test
    public void numericLiteral1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("3").numericLiteral();
        assertThat(exp, not(nullValue()));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3));
    }

    @Test
    public void numericLiteral2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("3.14").numericLiteral();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.14));
    }

    @Test
    public void multiplicativeExpression1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2*3").multiplicativeExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(2 * 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(6.0));
    }

    @Test
    public void multiplicativeExpression2() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("2*x").multiplicativeExpression());
        assertThat(exp.getExpression().getName(), equalTo("(2 * x)"));
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(20.0));

        exp.writeValue("x", ValueFactory.newVDouble(5.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(10.0));
    }

    @Test
    public void multiplicativeExpression3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("10/2").multiplicativeExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(10 / 2)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void multiplicativeExpression4() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("x/2").multiplicativeExpression());
        assertThat(exp.getExpression().getName(), equalTo("(x / 2)"));
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));

        exp.writeValue("x", ValueFactory.newVDouble(20.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(10.0));
    }

    @Test
    public void multiplicativeExpression5() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("9%2").multiplicativeExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void multiplicativeExpression6() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("x%4").multiplicativeExpression());
        exp.writeValue("x", ValueFactory.newVDouble(11.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));

        exp.writeValue("x", ValueFactory.newVDouble(21.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void additiveExpression1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2+3").additiveExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(2 + 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void additiveExpression2() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("2+x").additiveExpression());
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        assertThat(exp.getExpression().getName(), equalTo("(2 + x)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(12.0));

        exp.writeValue("x", ValueFactory.newVDouble(5.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(7.0));
    }

    @Test
    public void additiveExpression3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2-3").additiveExpression();
        assertThat(exp.getName(), equalTo("(2 - 3)"));
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-1.0));
    }

    @Test
    public void additiveExpression4() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("2-x").additiveExpression());
        assertThat(exp.getExpression().getName(), equalTo("(2 - x)"));
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-8.0));

        exp.writeValue("x", ValueFactory.newVDouble(5.0));
        result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-3.0));
    }

    @Test
    public void formula1() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("(3+2+x)*(5-y)/z").additiveExpression());
        assertThat(exp.getExpression().getName(), equalTo("((((3 + 2) + x) * (5 - y)) / z)"));
        exp.writeValue("x", ValueFactory.newVDouble(3.0));
        exp.writeValue("y", ValueFactory.newVDouble(2.0));
        exp.writeValue("z", ValueFactory.newVDouble(4.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(6.0));
    }

    @Test
    public void formula2() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("'x' + 2").additiveExpression());
        assertThat(exp.getExpression().getName(), equalTo("(x + 2)"));
        exp.writeValue("x", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void formula3() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("mypv.FIELD").additiveExpression());
        assertThat(exp.getExpression().getName(), equalTo("mypv.FIELD"));
        exp.writeValue("mypv.FIELD", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula4() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(createParser("mypv.FIELD$").additiveExpression());
        assertThat(exp.getExpression().getName(), equalTo("mypv.FIELD$"));
        exp.writeValue("mypv.FIELD$", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }
}
