/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import java.util.Arrays;
import org.diirt.datasource.expression.SourceRateExpressionImpl;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.SourceRateExpression;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.diirt.datasource.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class CustomFunctionTest {

    public CustomFunctionTest() {
    }

    @Test
    public void singleArgFunction() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        SourceRateExpression<Double> vDouble = new SourceRateExpressionImpl<>("test", Double.class);
        @SuppressWarnings("unchecked")
        DesiredRateExpression<Double> expression = resultOf(new OneArgFunction<Double, Double>() {
            @Override
            public Double calculate(Double arg) {
                return - arg;
            }
        }, latestValueOf(vDouble));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<Double> function = expression.getFunction();

        // Test values
        tester.writeValue("test", 1.0);
        assertEquals((Object) (-1.0), function.readValue());

        tester.writeValue("test", 123.0);
        assertEquals((Object) (-123.0), function.readValue());

        tester.writeValue("test", 1.0);
        tester.writeValue("test", 2.0);
        tester.writeValue("test", 3.0);
        assertEquals((Object) (-3.0), function.readValue());
    }

    @Test
    public void twoArgFunction() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        SourceRateExpression<String> value1 = new SourceRateExpressionImpl<>("test1", String.class);
        SourceRateExpression<Integer> value2 = new SourceRateExpressionImpl<>("test2", Integer.class);
        DesiredRateExpression<String> expression = resultOf(new TwoArgFunction<String, String, Integer>() {
            @Override
            public String calculate(String name, Integer number) {
                return name + "(" + number + ")";
            }
        }, latestValueOf(value1), latestValueOf(value2));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<String> function = expression.getFunction();

        // Test values
        tester.writeValue("test1", "test");
        tester.writeValue("test2", 1);
        assertEquals("test(1)", function.readValue());

        tester.writeValue("test2", 2);
        assertEquals("test(2)", function.readValue());

        tester.writeValue("test1", "A");
        tester.writeValue("test1", "B");
        tester.writeValue("test1", "C");
        assertEquals("C(2)", function.readValue());
    }

    @Test
    public void nArgFunction() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        @SuppressWarnings("unchecked")
        DesiredRateExpression<Double> expression = resultOf(new OneArgFunction<Double, List<Integer>>() {
            @Override
            public Double calculate(List<Integer> numbers) {
                double average = 0;
                for (int value : numbers) {
                    average += value;
                }
                return average / numbers.size();
            }
        }, listOf(latestValueOf(channels(Arrays.asList("test1", "test2", "test3"), Integer.class, Integer.class))));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<Double> function = expression.getFunction();

        // Test values
        tester.writeValue("test1", 0);
        tester.writeValue("test2", 1);
        tester.writeValue("test3", 2);
        assertEquals((Object) (1.0), function.readValue());

        // Test values
        tester.writeValue("test3", 5);
        assertEquals((Object) (2.0), function.readValue());
    }

}