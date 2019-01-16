/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import static org.diirt.datasource.vtype.ExpressionLanguage.vDoubleConstants;
import static org.diirt.datasource.vtype.ExpressionLanguage.vIntConstants;
import static org.diirt.datasource.vtype.ExpressionLanguage.vNumberArrayOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.diirt.datasource.ReadExpressionTester;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import org.epics.vtype.VNumberArray;
import org.junit.Test;

/**
 *
 * @author carcassi
 */
public class VNumberToVNumberArrayTest {

    @Test
    public void vNumberArrayOf1() throws Exception {
        ReadExpressionTester exp = new ReadExpressionTester(vNumberArrayOf(vDoubleConstants(Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))));
        VNumberArray value = (VNumberArray) exp.getValue();
        assertThat(value.getData(), equalTo((ListNumber) ArrayDouble.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void vNumberArrayOf2() throws Exception {
        ReadExpressionTester exp = new ReadExpressionTester(vNumberArrayOf(vIntConstants(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))));
        VNumberArray value = (VNumberArray) exp.getValue();
        assertThat(value.getData(), equalTo((ListNumber) ArrayDouble.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }
}
