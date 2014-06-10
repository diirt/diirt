/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.formula;

import java.util.Arrays;
import java.util.List;
import org.epics.vtype.VBoolean;
import org.epics.vtype.VNumber;
import org.epics.vtype.ndarray.DimensionInfo;
import org.epics.vtype.ndarray.VNumberArrayFactory;
import org.epics.vtype.table.ListNumberProvider;
import org.epics.vtype.table.VTableFactory;

/**
 *
 * @author carcassi
 */
class DimInfoFormulaFunction implements FormulaFunction {

    @Override
    public boolean isPure() {
        return true;
    }

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "dimInfo";
    }

    @Override
    public String getDescription() {
        return "Gathers information for one dimension of an nd array";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>>asList(VNumber.class, VBoolean.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("size", "invert");
    }

    @Override
    public Class<?> getReturnType() {
        return DimensionInfo.class;
    }

    @Override
    public Object calculate(final List<Object> args) {
        VNumber size = (VNumber) args.get(0);
        VBoolean invert = (VBoolean) args.get(1);
        
        if (size == null || invert == null) {
            return null;
        }
        
        return VNumberArrayFactory.dimInfo(size.getValue().intValue(), invert.getValue());
    }
    
}
