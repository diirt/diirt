/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import org.diirt.datasource.ExpressionLanguage;
import org.diirt.vtype.VDouble;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueFactory;


/**
 *
 * @author carcassi
 */
abstract class OneArgNumericFunction implements ExpressionLanguage.OneArgFunction<VDouble, VNumber>  {

    @Override
    public VDouble calculate(VNumber arg) {
        if (arg == null) {
            return null;
        }
        return ValueFactory.newVDouble(calculate(arg.getValue().doubleValue()));
    }
    
    abstract double calculate(double arg);
    
}
