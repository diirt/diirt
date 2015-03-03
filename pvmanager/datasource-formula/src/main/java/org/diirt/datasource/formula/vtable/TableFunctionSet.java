/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.vtable;

import org.diirt.datasource.formula.FormulaFunctionSet;
import org.diirt.datasource.formula.FormulaFunctionSetDescription;

/**
 * A function set for table operations.
 *
 * @author carcassi
 */
public class TableFunctionSet extends FormulaFunctionSet {

    /**
     * Creates a new set.
     */
    public TableFunctionSet() {
        super(new FormulaFunctionSetDescription("vtable", "Function to aggregate and manipulate tables")
                .addFormulaFunction(new ColumnOfVTableFunction())
                .addFormulaFunction(new ColumnFromVNumberArrayFunction())
                .addFormulaFunction(new ColumnFromVStringArrayFunction())
                .addFormulaFunction(new TableOfFormulaFunction())
                .addFormulaFunction(new RangeFormulaFunction())
                .addFormulaFunction(new StepFormulaFunction())
                .addFormulaFunction(new ColumnFromListNumberGeneratorFunction())
                .addFormulaFunction(new NaturalJoinFunction())
                .addFormulaFunction(new TableUnionFunction())
                .addFormulaFunction(new TableRangeFilterFunction())
                .addFormulaFunction(new TableRangeArrayFilterFunction())
                .addFormulaFunction(new TableStringMatchFilterFunction())
                .addFormulaFunction(new TableValueFilterFunction())
                );
    }


}
