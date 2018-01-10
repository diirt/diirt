/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.vtype;

import org.diirt.vtype.VNumberArray;
import java.util.Arrays;
import org.diirt.datasource.ReadExpressionTester;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static org.diirt.datasource.vtype.ExpressionLanguage.*;
import org.diirt.util.array.ArrayDouble;
import org.diirt.util.array.ListNumber;

/**
 *
 * @author carcassi
 */
public class VNumberToVNumberArrayTest {

    @Test
    public void vNumberArrayOf1() throws Exception {
        ReadExpressionTester exp = new ReadExpressionTester(vNumberArrayOf(vDoubleConstants(Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0))));
        VNumberArray value = (VNumberArray) exp.getValue();
        assertThat(value.getData(), equalTo((ListNumber) new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }

    @Test
    public void vNumberArrayOf2() throws Exception {
        ReadExpressionTester exp = new ReadExpressionTester(vNumberArrayOf(vIntConstants(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))));
        VNumberArray value = (VNumberArray) exp.getValue();
        assertThat(value.getData(), equalTo((ListNumber) new ArrayDouble(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)));
    }
}
