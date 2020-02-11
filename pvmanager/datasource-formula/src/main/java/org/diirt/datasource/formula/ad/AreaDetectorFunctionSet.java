/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.ad;

import org.diirt.datasource.formula.*;

/**
 * Formula functions that operate on area detector
 *
 * @author kunal
 */
public class AreaDetectorFunctionSet extends FormulaFunctionSet {

    /**
     * Creates a new set.
     */
    public AreaDetectorFunctionSet() {
        super(new FormulaFunctionSetDescription("ad", "Functions for area detector")
                .addFormulaFunction(new ADDataTypeMappingFunction())
                .addFormulaFunction(new ADDataTypeMappingFunction2())
        );
    }

}
