/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.enums;

import org.diirt.datasource.formula.*;

/**
 * A set of functions for enum manipulation.
 *
 * @author carcassi
 */
public class EnumFunctionSet extends FormulaFunctionSet {

    /**
     * Creates a new set.
     */
    public EnumFunctionSet() {
        super(new FormulaFunctionSetDescription("enum",
                "Functions for enum manipulation")
                .addFormulaFunction(new EnumIndexOfFunction())
                .addFormulaFunction(new EnumFromVNumberFunction())
        );
    }

}
