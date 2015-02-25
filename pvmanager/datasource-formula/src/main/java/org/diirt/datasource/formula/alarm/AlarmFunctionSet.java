/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.alarm;

import org.diirt.datasource.formula.*;

/**
 * A set of functions for alarm manipulation.
 *
 * @author carcassi
 */
public class AlarmFunctionSet extends FormulaFunctionSet {

    /**
     * Creates a new set.
     */
    public AlarmFunctionSet() {
        super(new FormulaFunctionSetDescription("alarm",
                "Functions for alarm manipulation")
                .addFormulaFunction(new HighestSeverityFunction())
                .addFormulaFunction(new AlarmOfFunction())
        );
    }

}
