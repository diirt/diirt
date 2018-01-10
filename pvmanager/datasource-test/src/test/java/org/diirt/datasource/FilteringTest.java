/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.diirt.datasource.expression.SourceRateExpressionImpl;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.diirt.datasource.expression.SourceRateExpression;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.diirt.datasource.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class FilteringTest {

    public FilteringTest() {
    }

    @Test
    public void filter() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        ExpressionLanguage.Filter<Integer> monotonic = new Filter<Integer>(Integer.class) {

            @Override
            public boolean filter(Integer previousValue, Integer currentValue) {
                if (currentValue == null)
                    return true;
                if (previousValue == null)
                    return false;
                if (previousValue >= currentValue)
                    return true;
                return false;
            }
        };
        SourceRateExpression<Integer> vInteger = new SourceRateExpressionImpl<Integer>("test", Integer.class);
        @SuppressWarnings("unchecked")
        DesiredRateExpression<List<Integer>> expression = filterBy(monotonic, newValuesOf(vInteger));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<List<Integer>> function = expression.getFunction();

        // Test values
        tester.writeValue("test", 1);
        List<Integer> values = function.readValue();
        assertThat(values, equalTo(Arrays.asList(1)));

        tester.writeValue("test", 1);
        tester.writeValue("test", 2);
        tester.writeValue("test", 4);
        tester.writeValue("test", 3);
        values = function.readValue();
        assertThat(values, equalTo(Arrays.asList(2, 4)));

        tester.writeValue("test", 5);
        tester.writeValue("test", 7);
        tester.writeValue("test", 3);
        values = function.readValue();
        assertThat(values, equalTo(Arrays.asList(5, 7)));
    }

    @Test
    public void filterAcceptsDifferentTypes() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        ExpressionLanguage.Filter<Number> monotonic = new Filter<Number>(Number.class) {

            @Override
            public boolean filter(Number previousValue, Number currentValue) {
                if (currentValue == null)
                    return true;
                if (previousValue == null)
                    return false;
                if (previousValue.doubleValue() >= currentValue.doubleValue())
                    return true;
                return false;
            }
        };
        SourceRateExpression<Number> vInteger = new SourceRateExpressionImpl<Number>("test", Number.class);
        @SuppressWarnings("unchecked")
        DesiredRateExpression<List<Number>> expression = filterBy(monotonic, newValuesOf(vInteger));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<List<Number>> function = expression.getFunction();

        // Test values
        tester.writeValue("test", 1);
        List<Number> values = function.readValue();
        assertThat(values, equalTo(Arrays.<Number>asList(1)));

        tester.writeValue("test", 1.0);
        tester.writeValue("test", 2);
        tester.writeValue("test", 4.0);
        tester.writeValue("test", 3);
        values = function.readValue();
        assertThat(values, equalTo(Arrays.<Number>asList(2, 4.0)));

        tester.writeValue("test", 5.0);
        tester.writeValue("test", 7);
        tester.writeValue("test", 3.0);
        values = function.readValue();
        assertThat(values, equalTo(Arrays.<Number>asList(5.0, 7)));
    }

    @Test
    public void filterAcceptsDifferentTypesAndIgnoresSome() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        ExpressionLanguage.Filter<Number> monotonic = new Filter<Number>(Number.class) {

            @Override
            public boolean filter(Number previousValue, Number currentValue) {
                if (currentValue == null)
                    return true;
                if (previousValue == null)
                    return false;
                if (previousValue.doubleValue() >= currentValue.doubleValue())
                    return true;
                return false;
            }
        };
        SourceRateExpression<Object> vInteger = new SourceRateExpressionImpl<Object>("test", Object.class);
        @SuppressWarnings("unchecked")
        DesiredRateExpression<List<Object>> expression = filterBy(monotonic, newValuesOf(vInteger));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<List<Object>> function = expression.getFunction();

        // Test values
        tester.writeValue("test", 1);
        List<Object> values = function.readValue();
        assertThat(values, equalTo(Arrays.<Object>asList(1)));

        tester.writeValue("test", "1.0");
        tester.writeValue("test", 2);
        tester.writeValue("test", 4.0);
        tester.writeValue("test", 3);
        values = function.readValue();
        assertThat(values, equalTo(Arrays.<Object>asList("1.0", 2, 4.0)));

        tester.writeValue("test", 5.0);
        tester.writeValue("test", 7);
        tester.writeValue("test", "3.0");
        values = function.readValue();
        assertThat(values, equalTo(Arrays.<Object>asList(5.0, 7, "3.0")));
    }

    @Test
    public void filterAcceptsDifferentTypesAndRejectsUnmatched() {
        // Sets up a pipeline where we put data in the cache, and apply
        // a function to it
        ExpressionLanguage.Filter<Number> monotonic = new Filter<Number>(Number.class, true) {

            @Override
            public boolean filter(Number previousValue, Number currentValue) {
                if (currentValue == null)
                    return true;
                if (previousValue == null)
                    return false;
                if (previousValue.doubleValue() >= currentValue.doubleValue())
                    return true;
                return false;
            }
        };
        SourceRateExpression<Object> vInteger = new SourceRateExpressionImpl<Object>("test", Object.class);
        @SuppressWarnings("unchecked")
        DesiredRateExpression<List<Object>> expression = filterBy(monotonic, newValuesOf(vInteger));
        ReadExpressionTester tester = new ReadExpressionTester(expression);
        ReadFunction<List<Object>> function = expression.getFunction();

        // Test values
        tester.writeValue("test", 1);
        List<Object> values = function.readValue();
        assertThat(values, equalTo(Arrays.<Object>asList(1)));

        tester.writeValue("test", "1.0");
        tester.writeValue("test", 2);
        tester.writeValue("test", 4.0);
        tester.writeValue("test", 3);
        values = function.readValue();
        assertThat(values, equalTo(Arrays.<Object>asList(2, 4.0)));

        tester.writeValue("test", 5.0);
        tester.writeValue("test", 7);
        tester.writeValue("test", "3.0");
        values = function.readValue();
        assertThat(values, equalTo(Arrays.<Object>asList(5.0, 7)));
    }

}