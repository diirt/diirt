/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.antlr.runtime.*;
import org.epics.pvmanager.ExpressionTester;
import org.epics.pvmanager.data.*;
import org.epics.pvmanager.Function;
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
    
    public static FormulaParser createParser(String text) {
        CharStream stream = new ANTLRStringStream(text);
        FormulaLexer lexer = new FormulaLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        return new FormulaParser(tokenStream);
    }

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
        VInt result = (VInt) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(3));
    }

    @Test
    public void numericLiteral2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("3.14").numericLiteral();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(3.14));
    }

    @Test
    public void multiplicativeExpression1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2*3").multiplicativeExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(6.0));
    }

    @Test
    public void multiplicativeExpression2() throws RecognitionException {
        ExpressionTester exp = new ExpressionTester(createParser("2*x").multiplicativeExpression());
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(20.0));

        exp.writeValue("x", ValueFactory.newVDouble(5.0));
        result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(10.0));
    }

    @Test
    public void multiplicativeExpression3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("10/2").multiplicativeExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void multiplicativeExpression4() throws RecognitionException {
        ExpressionTester exp = new ExpressionTester(createParser("x/2").multiplicativeExpression());
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(5.0));

        exp.writeValue("x", ValueFactory.newVDouble(20.0));
        result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(10.0));
    }

    @Test
    public void multiplicativeExpression5() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("9%2").multiplicativeExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void multiplicativeExpression6() throws RecognitionException {
        ExpressionTester exp = new ExpressionTester(createParser("x%4").multiplicativeExpression());
        exp.writeValue("x", ValueFactory.newVDouble(11.0));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(3.0));

        exp.writeValue("x", ValueFactory.newVDouble(21.0));
        result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void additiveExpression1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2+3").additiveExpression();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("(2 + 3)"));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void additiveExpression2() throws RecognitionException {
        ExpressionTester exp = new ExpressionTester(createParser("2+x").additiveExpression());
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        assertThat(exp.getExpression().getName(), equalTo("(2 + x)"));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(12.0));

        exp.writeValue("x", ValueFactory.newVDouble(5.0));
        result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(7.0));
    }

    @Test
    public void additiveExpression3() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("2-3").additiveExpression();
        assertThat(exp, not(nullValue()));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(-1.0));
    }

    @Test
    public void additiveExpression4() throws RecognitionException {
        ExpressionTester exp = new ExpressionTester(createParser("2-x").additiveExpression());
        exp.writeValue("x", ValueFactory.newVDouble(10.0));
        VDouble result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(-8.0));

        exp.writeValue("x", ValueFactory.newVDouble(5.0));
        result = (VDouble) exp.getFunction().getValue();
        assertThat(result.getValue(), equalTo(-3.0));
    }
//
//    @Test
//    public void formula1() throws RecognitionException {
//        ExpressionTester exp = new ExpressionTester(createParser("(3+x)*(5-y)/z").additiveExpression());
//        exp.writeValue("x", ValueFactory.newVDouble(5.0));
//        exp.writeValue("y", ValueFactory.newVDouble(2.0));
//        exp.writeValue("z", ValueFactory.newVDouble(4.0));
//        VDouble result = (VDouble) exp.getFunction().getValue();
//        assertThat(result.getValue(), equalTo(6.0));
//    }
}
