/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula.venum;

import static org.diirt.vtype.ValueFactory.newVString;

import java.util.Arrays;
import java.util.List;
import org.diirt.datasource.formula.FormulaFunction;

import org.diirt.datasource.util.NullUtils;
import org.diirt.vtype.VEnum;
import org.diirt.vtype.VNumber;
import org.diirt.vtype.ValueUtil;

/**
 * Return the enum label as VString
 *
 */
class EnumLabelOfFunction implements FormulaFunction {

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
                return "labelOf";
        }

        @Override
        public String getDescription() {
                return "Gets the label of a VEnum";
        }

        @Override
        public List<Class<?>> getArgumentTypes() {
                return Arrays.<Class<?>> asList(VEnum.class);
        }

        @Override
        public List<String> getArgumentNames() {
                return Arrays.asList("enum");
        }

        @Override
        public Class<?> getReturnType() {
                return VNumber.class;
        }

        @Override
        public Object calculate(List<Object> args) {
                if (NullUtils.containsNull(args)) {
                        return null;
                }
                // args[0] is a VEnum
                VEnum value = (VEnum) args.get(0);
                return newVString(value.getValue(),
                                ValueUtil.highestSeverityOf(args, false),
                                ValueUtil.latestValidTimeOrNowOf(args));
        }

}
