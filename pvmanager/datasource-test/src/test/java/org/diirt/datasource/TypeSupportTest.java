/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.diirt.datasource.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class TypeSupportTest {

    private static class MyTypeSupport<T> extends TypeSupport<T> {
        private final Class<T> type;

        public MyTypeSupport(Class<T> type) {
            super(type, MyTypeSupport.class);
            this.type = type;
        }

    }

    @Test
    public void testInheritance() {
        // Nothing added, so there should be no support for anything
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Object.class), equalTo(false));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Object.class), equalTo(false));
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Double.class), equalTo(false));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Double.class), equalTo(false));
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Integer.class), equalTo(false));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Integer.class), equalTo(false));

        // Add support for Object
        TypeSupport.addTypeSupport(new MyTypeSupport<Object>(Object.class));
        MyTypeSupport support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Number.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        // Object is supported directly, everything supported indirectly
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Object.class), equalTo(true));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Object.class), equalTo(true));
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Double.class), equalTo(true));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Double.class), equalTo(false));
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Integer.class), equalTo(true));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Integer.class), equalTo(false));

        // Add direct support for double
        TypeSupport.addTypeSupport(new MyTypeSupport<Double>(Double.class));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Object.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Integer.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Double.class);
        assertThat(support.type, is(equalTo((Class) Double.class)));
        // Object and Double supported directly, Integer indirectly
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Object.class), equalTo(true));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Object.class), equalTo(true));
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Double.class), equalTo(true));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Double.class), equalTo(true));
        assertThat(TypeSupport.isTypeSupported(MyTypeSupport.class, Integer.class), equalTo(true));
        assertThat(TypeSupport.isTypeDirectlySupported(MyTypeSupport.class, Integer.class), equalTo(false));

        // Add direct support for Integer
        TypeSupport.addTypeSupport(new MyTypeSupport<Integer>(Integer.class));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Object.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Integer.class);
        assertThat(support.type, is(equalTo((Class) Integer.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Double.class);
        assertThat(support.type, is(equalTo((Class) Double.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Float.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));

        // Add direct support for Comparable and Number
        TypeSupport.addTypeSupport(new MyTypeSupport<Comparable>(Comparable.class));
        TypeSupport.addTypeSupport(new MyTypeSupport<Number>(Number.class));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Object.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Integer.class);
        assertThat(support.type, is(equalTo((Class) Integer.class)));
        support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Double.class);
        assertThat(support.type, is(equalTo((Class) Double.class)));
        try {
            support = (MyTypeSupport) TypeSupport.findTypeSupportFor(MyTypeSupport.class, Float.class);
            fail("Found single support for " + support.type);
        } catch(Exception ex) {
            // Fails because two supports are there
        }
    }

}
