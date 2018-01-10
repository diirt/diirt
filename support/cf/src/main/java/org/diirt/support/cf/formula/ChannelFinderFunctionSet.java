/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.cf.formula;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;

/**
 *
 * @author Kunal Shroff
 *
 */
public class ChannelFinderFunctionSet extends FormulaFunctionSet {

    public ChannelFinderFunctionSet() {
        super(new FormulaFunctionSetDescription("cf", "Functions to query ChannelFinder service")
        .addFormulaFunction(new CFQueryFunction()));
    }

}
