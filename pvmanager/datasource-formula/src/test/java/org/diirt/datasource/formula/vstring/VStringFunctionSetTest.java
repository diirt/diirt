/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vstring;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FunctionTester;

import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.junit.Test;



/**
 * @author shroffk
 *
 */
public class VStringFunctionSetTest {

    private FormulaFunctionSet set = new VStringFunctionSet();

    @Test
    public void concatStringArray() {
        FunctionTester.findBySignature(set, "concat", VStringArray.class)
                .compareReturnValue("xyz", (Object) new String[] {"x", "y", "z"})
                .compareReturnValue(null, (Object) null)
                .highestAlarmReturned()
                .latestTimeReturned();
    }

    @Test
    public void concatStrings() {
        FunctionTester.findBySignature(set, "concat", VString.class)
                .compareReturnValue("xyz", "x", "y", "z")
                .compareReturnValue(null, "a", null)
                .compareReturnValue(null, null, "b")
                .highestAlarmReturned()
                .latestTimeReturned();
    }
}
