/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.datasource.test.ExpressionLanguage.*;
import org.diirt.datasource.expression.DesiredRateExpression;

/**
 *
 * @author carcassi
 */
public class ExpressionLanguageTest {

    @Test
    public void vType1() {
        DesiredRateExpression<Integer> counter = counter();
        assertThat(counter.getFunction().readValue(), equalTo(0));
        assertThat(counter.getFunction().readValue(), equalTo(1));
        assertThat(counter.getFunction().readValue(), equalTo(2));
        assertThat(counter.getFunction().readValue(), equalTo(3));
    }
}
