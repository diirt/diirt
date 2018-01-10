/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.formula;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.diirt.datasource.formula.FormulaAst.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class FormulaAstTest {

    @Test
    public void substitution1() {
        FormulaAst orig = formula("='x'+'y'");
        Map<String, FormulaAst> subs = new HashMap<>();
        subs.put("x", integer(3));
        subs.put("y", integer(4));
        FormulaAst end = orig.substituteChannels(subs);
        assertThat(end, equalTo(formula("=3+4")));
    }

    @Test
    public void equals1() {
        assertThat(formula("=3"), equalTo(integer(3)));
    }

    @Test
    public void toString1() {
        assertThat(formula("=3").toString(), equalTo("3"));
    }

    @Test
    public void toString2() {
        assertThat(formula("=3+4").toString(), equalTo("(3 + 4)"));
    }

    @Test
    public void listChannelNames1() {
        assertThat(formula("=3+4").listChannelNames(), equalTo(new ArrayList<String>()));
        assertThat(formula("='x'+4").listChannelNames(), equalTo(Arrays.asList("x")));
        assertThat(formula("=3+'x'+\"y\"").listChannelNames(), equalTo(Arrays.asList("x")));
        assertThat(formula("='x'+'y'").listChannelNames(), equalTo(Arrays.asList("x", "y")));
    }
}
