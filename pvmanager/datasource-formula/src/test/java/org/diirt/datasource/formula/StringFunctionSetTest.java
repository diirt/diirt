/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import org.diirt.vtype.AlarmSeverity;
import org.diirt.vtype.VEnum;

import org.diirt.vtype.VString;
import org.diirt.vtype.VStringArray;
import org.junit.Test;

import static org.diirt.vtype.ValueFactory.*;


/**
 * @author shroffk
 * 
 */
public class StringFunctionSetTest extends BaseTestForFormula {

    private FormulaFunctionSet set = new StringFunctionSet();

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
