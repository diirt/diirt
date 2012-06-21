/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import org.antlr.runtime.*;
import org.epics.pvmanager.data.*;
import org.epics.pvmanager.Function;
import org.epics.pvmanager.ValueCache;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.expression.SourceRateExpression;
import org.junit.Test;
import static org.epics.pvmanager.data.ExpressionLanguage.*;
import static org.epics.pvmanager.formula.ExpressionLanguage.*;
import org.epics.pvmanager.expression.ChannelExpression;
import org.epics.util.array.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author carcassi
 */
public class FormulaParserTest {
    
    public static FormulaParser createParser(String text) {
        CharStream stream = new ANTLRStringStream(text);
        FormulaLexer lexer = new FormulaLexer(stream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        return new FormulaParser(tokenStream);
    }

    @Test
    public void pv1() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("mypv").pv();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("mypv"));
    }

    @Test
    public void pv2() throws RecognitionException {
        DesiredRateExpression<?> exp = createParser("'mypv'").pv();
        assertThat(exp, not(nullValue()));
        assertThat(exp.getName(), equalTo("mypv"));
    }
}
