/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.timecache.integration;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;

/**
 * @author Fred Arnaud (Sopra Group) - ITER
 */
public class TimeCacheFunctionSet extends FormulaFunctionSet {

        public TimeCacheFunctionSet() {
                super(new FormulaFunctionSetDescription("tc",
                                "Functions to query data threw a TimeCache")
                                .addFormulaFunction(new TCQueryFunction()));
        }

}
