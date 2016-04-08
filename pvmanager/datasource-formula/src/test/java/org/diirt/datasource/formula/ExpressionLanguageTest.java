/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.time.Duration;

import org.antlr.runtime.RecognitionException;
import org.diirt.datasource.DataSource;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.vtype.VDouble;
import org.junit.Test;

import static org.diirt.datasource.formula.ExpressionLanguage.*;

import org.diirt.datasource.test.CountDownPVWriterListener;
import org.diirt.datasource.test.MockDataSource;
import org.diirt.util.array.*;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VNumberArray;
import org.diirt.vtype.VString;
import org.diirt.vtype.ValueFactory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {

    @Test
    public void formula1() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(3+2+'x')*(5-'y')/'z'"));
        assertThat(exp.getExpression().getName(), equalTo("((((3 + 2) + x) * (5 - y)) / z)"));
        exp.writeValue("x", ValueFactory.newVDouble(3.0));
        exp.writeValue("y", ValueFactory.newVDouble(2.0));
        exp.writeValue("z", ValueFactory.newVDouble(4.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(6.0));
    }

    @Test
    public void formula2() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='x' + 2"));
        assertThat(exp.getExpression().getName(), equalTo("(x + 2)"));
        exp.writeValue("x", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(5.0));
    }

    @Test
    public void formula3() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("mypv.FIELD"));
        assertThat(exp.getExpression().getName(), equalTo("mypv.FIELD"));
        exp.writeValue("mypv.FIELD", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula4() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("mypv.FIELD$"));
        assertThat(exp.getExpression().getName(), equalTo("mypv.FIELD$"));
        exp.writeValue("mypv.FIELD$", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula5() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='loc://test(0)'"));
        assertThat(exp.getExpression().getName(), equalTo("loc://test(0)"));
        exp.writeValue("loc://test(0)", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula6() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='loc://test(0)'+3"));
        assertThat(exp.getExpression().getName(), equalTo("(loc://test(0) + 3)"));
        exp.writeValue("loc://test(0)", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(6.0));
    }

    @Test
    public void formula7() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='loc://test(0, 1, 2, 3)'"));
        assertThat(exp.getExpression().getName(), equalTo("loc://test(0, 1, 2, 3)"));
        exp.writeValue("loc://test(0, 1, 2, 3)", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula8() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='loc://test(\"0\", \"1\", \"2\", \"3\")'"));
        assertThat(exp.getExpression().getName(), equalTo("loc://test(\"0\", \"1\", \"2\", \"3\")"));
        exp.writeValue("loc://test(\"0\", \"1\", \"2\", \"3\")", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula9() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3e3"));
        assertThat(exp.getExpression().getName(), equalTo("3000.0"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3000.0));
    }

    @Test
    public void formula10() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='300abc'"));
        assertThat(exp.getExpression().getName(), equalTo("300abc"));
        exp.writeValue("300abc", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula11() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=log(1)"));
        assertThat(exp.getExpression().getName(), equalTo("log(1)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(0.0));
    }

    @Test
    public void formula12() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=sin(3.1415)"));
        assertThat(exp.getExpression().getName(), equalTo("sin(3.1415)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(0.0, 0.0001));
    }

    @Test
    public void formula13() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=log('sim://test()')"));
        assertThat(exp.getExpression().getName(), equalTo("log(sim://test())"));
        exp.writeValue("sim://test()", ValueFactory.newVDouble(1.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(0.0));
    }

    @Test
    public void formula14() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=abs(-3)"));
        assertThat(exp.getExpression().getName(), equalTo("abs(-3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula15() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=-10"));
        assertThat(exp.getExpression().getName(), equalTo("-10"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(-10.0));
    }

    @Test
    public void formula16() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=sqrt(4)"));
        assertThat(exp.getExpression().getName(), equalTo("sqrt(4)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(2.0));
    }

    @Test
    public void formula17() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=2*acos(0)"));
        assertThat(exp.getExpression().getName(), equalTo("(2 * acos(0))"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(3.1415, 0.0001));
    }

    @Test
    public void formula18() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='sim://const(\"Hello!\")'"));
        assertThat(exp.getExpression().getName(), equalTo("sim://const(\"Hello!\")"));
        exp.writeValue("sim://const(\"Hello!\")", ValueFactory.newVDouble(1.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1.0));
    }

    @Test
    public void formula19() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='loc://test'"));
        assertThat(exp.getExpression().getName(), equalTo("loc://test"));
        exp.writeValue("loc://test", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula20() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("='escaped\\\"pv\\\"'"));
        assertThat(exp.getExpression().getName(), equalTo("escaped\"pv\""));
        exp.writeValue("escaped\"pv\"", ValueFactory.newVDouble(3.0));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3.0));
    }

    @Test
    public void formula21() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=\"I said \\\"Hi\\\"\""));
        assertThat(exp.getExpression().getName(), equalTo("I said \"Hi\""));
        VString result = (VString) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo("I said \"Hi\""));
    }

    @Test
    public void formula22() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=asin(0)"));
        assertThat(exp.getExpression().getName(), equalTo("asin(0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(0.0, 0.0001));
    }

    @Test
    public void formula23() {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=1+2-3"));
        assertThat(exp.getExpression().getName(), equalTo("((1 + 2) - 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(0.0));
    }

    @Test
    public void formula24() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=atan(0)"));
        assertThat(exp.getExpression().getName(), equalTo("atan(0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(0.0, 0.0001));
    }

    @Test
    public void formula25() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=cbrt(8.0)"));
        assertThat(exp.getExpression().getName(), equalTo("cbrt(8.0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(2.0, 0.0001));
    }

    @Test
    public void formula26() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=ceil(2.6)"));
        assertThat(exp.getExpression().getName(), equalTo("ceil(2.6)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(3.0, 0.0001));
    }

    @Test
    public void formula27() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=cos(0.0)"));
        assertThat(exp.getExpression().getName(), equalTo("cos(0.0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.0, 0.0001));
    }

    @Test
    public void formula28() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=cosh(0.0)"));
        assertThat(exp.getExpression().getName(), equalTo("cosh(0.0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.0, 0.0001));
    }

    @Test
    public void formula29() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=exp(1.0)"));
        assertThat(exp.getExpression().getName(), equalTo("exp(1.0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(Math.E, 0.0001));
    }

    @Test
    public void formula30() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=floor(1.6)"));
        assertThat(exp.getExpression().getName(), equalTo("floor(1.6)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.0, 0.0001));
    }

    @Test
    public void formula31() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=log10(100)"));
        assertThat(exp.getExpression().getName(), equalTo("log10(100)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(2.0, 0.0001));
    }

    @Test
    public void formula32() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=signum(100)"));
        assertThat(exp.getExpression().getName(), equalTo("signum(100)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.0, 0.0001));
    }

    @Test
    public void formula33() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=sinh(1.0)"));
        assertThat(exp.getExpression().getName(), equalTo("sinh(1.0)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.175201, 0.0001));
    }

    @Test
    public void formula34() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=tan(0.78539)"));
        assertThat(exp.getExpression().getName(), equalTo("tan(0.78539)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.0, 0.0001));
    }

    @Test
    public void formula35() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=tanh(0.5493)"));
        assertThat(exp.getExpression().getName(), equalTo("tanh(0.5493)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(0.5, 0.0001));
    }

    @Test
    public void formula36() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=2^3"));
        assertThat(exp.getExpression().getName(), equalTo("(2 ^ 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(8.0, 0.0001));
    }

    @Test
    public void formula37() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=2**3"));
        assertThat(exp.getExpression().getName(), equalTo("(2 ^ 3)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(8.0, 0.0001));
    }

    @Test
    public void formula38() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=1+2*3+2"));
        assertThat(exp.getExpression().getName(), equalTo("((1 + (2 * 3)) + 2)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(9.0, 0.0001));
    }

    @Test
    public void formula39() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=2*3^3"));
        assertThat(exp.getExpression().getName(), equalTo("(2 * (3 ^ 3))"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(54.0, 0.0001));
    }

    @Test
    public void formula40() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=2^3^4"));
        assertThat(exp.getExpression().getName(), equalTo("((2 ^ 3) ^ 4)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(4096.0, 0.0001));
    }

    @Test
    public void formula41() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=arrayOf(1,2,3)"));
        assertThat(exp.getExpression().getName(), equalTo("arrayOf(1, 2, 3)"));
        VNumberArray result = (VNumberArray) exp.getFunction().readValue();
        assertThat(result.getData(), equalTo((ListNumber) new ArrayDouble(1,2,3)));
    }

    @Test
    public void formula42() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=5%4"));
        assertThat(exp.getExpression().getName(), equalTo("(5 % 4)"));
        VDouble result = (VDouble) exp.getFunction().readValue();
        assertThat(result.getValue(), closeTo(1.0, 0.0001));
    }

    @Test
    public void formula43() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3<4"));
        assertThat(exp.getExpression().getName(), equalTo("(3 < 4)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula44() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3<3"));
        assertThat(exp.getExpression().getName(), equalTo("(3 < 3)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula45() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3>4"));
        assertThat(exp.getExpression().getName(), equalTo("(3 > 4)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula46() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=4>3"));
        assertThat(exp.getExpression().getName(), equalTo("(4 > 3)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula47() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3 >= 4"));
        assertThat(exp.getExpression().getName(), equalTo("(3 >= 4)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula48() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3>=3"));
        assertThat(exp.getExpression().getName(), equalTo("(3 >= 3)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula49() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3 <= 4"));
        assertThat(exp.getExpression().getName(), equalTo("(3 <= 4)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula50() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3<=3"));
        assertThat(exp.getExpression().getName(), equalTo("(3 <= 3)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula51() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(1>0)?3:4"));
        assertThat(exp.getExpression().getName(), equalTo("((1 > 0) ? 3 : 4)"));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3));
    }

    @Test
    public void formula52() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(1<0)?\"a\":4"));
        assertThat(exp.getExpression().getName(), equalTo("((1 < 0) ? a : 4)"));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(4));
    }

    @Test
    public void formula53() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3==4"));
        assertThat(exp.getExpression().getName(), equalTo("(3 == 4)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula54() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3==3"));
        assertThat(exp.getExpression().getName(), equalTo("(3 == 3)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula55() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3!=4"));
        assertThat(exp.getExpression().getName(), equalTo("(3 != 4)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula56() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3!=3"));
        assertThat(exp.getExpression().getName(), equalTo("(3 != 3)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula57() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(1>0)||(0>1)"));
        assertThat(exp.getExpression().getName(), equalTo("((1 > 0) || (0 > 1))"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula58() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(0>1)||(0>1)"));
        assertThat(exp.getExpression().getName(), equalTo("((0 > 1) || (0 > 1))"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula59() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(1>0)&&(0>1)"));
        assertThat(exp.getExpression().getName(), equalTo("((1 > 0) && (0 > 1))"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula60() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=(0<1)&&(0<1)"));
        assertThat(exp.getExpression().getName(), equalTo("((0 < 1) && (0 < 1))"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula61() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=!(0<1)"));
        assertThat(exp.getExpression().getName(), equalTo("!(0 < 1)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(false));
    }

    @Test
    public void formula62() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=!(1<0)"));
        assertThat(exp.getExpression().getName(), equalTo("!(1 < 0)"));
        VBoolean result = (VBoolean) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(true));
    }

    @Test
    public void formula63() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3&1"));
        assertThat(exp.getExpression().getName(), equalTo("(3 & 1)"));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(1));
    }

    @Test
    public void formula64() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=7&12"));
        assertThat(exp.getExpression().getName(), equalTo("(7 & 12)"));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(4));
    }

    @Test
    public void formula65() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=7|12"));
        assertThat(exp.getExpression().getName(), equalTo("(7 | 12)"));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(15));
    }

    @Test
    public void formula66() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=xor(7,4)"));
        assertThat(exp.getExpression().getName(), equalTo("xor(7, 4)"));
        VInt result = (VInt) exp.getFunction().readValue();
        assertThat(result.getValue(), equalTo(3));
    }

    @Test
    public void formula67() throws RecognitionException {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=1+'broken token"));
        assertThat(exp.getExpression().getDesiredRateExpressionImpl(), instanceOf(ErrorDesiredRateExpression.class));
    }

    @Test(expected = RuntimeException.class)
    public void formulaCast1() {
        ReadExpressionTester exp = new ReadExpressionTester(formula("=3.0", VString.class));
        exp.getValue();
    }

    @Test
    public void channelFromFormula1() {
        assertThat(ExpressionLanguage.channelFromFormula("='test'"), equalTo("test"));
    }

    @Test
    public void channelFromFormula2() {
        assertThat(ExpressionLanguage.channelFromFormula("=test()"), nullValue());
    }

    @Test
    public void channelFromFormula3() {
        assertThat(ExpressionLanguage.channelFromFormula("='test'"), equalTo("test"));
    }

    @Test
    public void channelFromFormula4() {
        assertThat(ExpressionLanguage.channelFromFormula("='2+3'"), equalTo("2+3"));
    }

    @Test
    public void channelFromFormula5() {
        assertThat(ExpressionLanguage.channelFromFormula("=2+3"), nullValue());
    }

    @Test
    public void channelFromFormula6() {
        assertThat(ExpressionLanguage.channelFromFormula("2+3"), equalTo("2+3"));
    }

    @Test
    public void readOnlyWriteExpression1() throws InterruptedException {
        DataSource sim = new MockDataSource();
        CountDownPVWriterListener<Object> listener = new CountDownPVWriterListener<Object>(1);
        PVWriter<Object> pvWriter = PVManager.write(ExpressionLanguage.readOnlyWriteExpression("Error message"))
                .from(sim)
                .writeListener(listener)
                .async();
        try {
            listener.await(Duration.ofMillis(200));
            Exception ex = pvWriter.lastWriteException();
            assertThat(ex, instanceOf(RuntimeException.class));
            assertThat(ex.getMessage(), equalTo("Error message"));
            assertThat(pvWriter.isWriteConnected(), equalTo(false));
            Thread.sleep(200);
            assertThat(pvWriter.lastWriteException(), nullValue());
            assertThat(pvWriter.isWriteConnected(), equalTo(false));
        } finally {
            pvWriter.close();
            sim.close();
        }
    }
}
