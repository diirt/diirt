/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.data.ExpressionLanguage.*;

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
        TypeSupport.addTypeSupport(new MyTypeSupport<Object>(Object.class));
        MyTypeSupport support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Number.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));

        // Add direct support for double
        TypeSupport.addTypeSupport(new MyTypeSupport<Double>(Double.class));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Object.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Integer.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Double.class);
        assertThat(support.type, is(equalTo((Class) Double.class)));

        // Add direct support for Integer
        TypeSupport.addTypeSupport(new MyTypeSupport<Integer>(Integer.class));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Object.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Integer.class);
        assertThat(support.type, is(equalTo((Class) Integer.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Double.class);
        assertThat(support.type, is(equalTo((Class) Double.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Float.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));

        // Add direct support for Comparable and Number
        TypeSupport.addTypeSupport(new MyTypeSupport<Comparable>(Comparable.class));
        TypeSupport.addTypeSupport(new MyTypeSupport<Number>(Number.class));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Object.class);
        assertThat(support.type, is(equalTo((Class) Object.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Integer.class);
        assertThat(support.type, is(equalTo((Class) Integer.class)));
        support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Double.class);
        assertThat(support.type, is(equalTo((Class) Double.class)));
        try {
            support = (MyTypeSupport) TypeSupport.cachedTypeSupportFor(MyTypeSupport.class, Float.class);
            fail("Found single support for " + support.type);
        } catch(Exception ex) {
            // Fails because two supports are there
        }
    }

}
