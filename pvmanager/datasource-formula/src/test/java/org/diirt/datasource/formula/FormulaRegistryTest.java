/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.Collections;
import java.util.List;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.VInt;
import org.diirt.vtype.VDouble;
import org.antlr.runtime.*;
import org.diirt.datasource.ReadExpressionTester;
import org.diirt.datasource.expression.DesiredRateExpression;
import org.junit.Test;
import static org.diirt.datasource.formula.FormulaAst.*;
import org.diirt.vtype.VBoolean;
import org.diirt.vtype.VString;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class FormulaRegistryTest {

    public static class Function1 extends StatefulFormulaFunction {

        @Override
        public boolean isVarArgs() {
            return true;
        }

        @Override
        public String getName() {
            return "function1";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public List<Class<?>> getArgumentTypes() {
            return Collections.emptyList();
        }

        @Override
        public List<String> getArgumentNames() {
            return Collections.emptyList();
        }

        @Override
        public Class<?> getReturnType() {
            return String.class;
        }

        @Override
        public Object calculate(List<Object> args) {
            return "";
        }

    }

    @Test
    public void validateFunction1() {
        FormulaRegistry.validateFormulaFunction(new Function1());
    }

    public static class Function2 extends StatefulFormulaFunction {

        private Function2() {
        }

        @Override
        public boolean isVarArgs() {
            return true;
        }

        @Override
        public String getName() {
            return "function2";
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public List<Class<?>> getArgumentTypes() {
            return Collections.emptyList();
        }

        @Override
        public List<String> getArgumentNames() {
            return Collections.emptyList();
        }

        @Override
        public Class<?> getReturnType() {
            return String.class;
        }

        @Override
        public Object calculate(List<Object> args) {
            return "";
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void validateFunction2() {
        FormulaRegistry.validateFormulaFunction(new Function2());
    }
}
