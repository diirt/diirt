/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.jca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.epics.pvmanager.ValueCache;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 *
 * @author carcassi
 */
public class PayloadToTypeMatcherTest {

    public PayloadToTypeMatcherTest() {
    }

    public static PayloadToTypeConverter<Class<?>, Object> createMockConverter(final Class<?> clazz) {
        return new PayloadToTypeConverter<Class<?>, Object>() {

            @Override
            public int match(ValueCache<?> cache, Class<?> connection) {
                if (cache.getType().isAssignableFrom((Class<?>) connection)) {
                    return 1;
                } else {
                    return 0;
                }
            }

            @Override
            public Object getSubscriptionParameter(ValueCache<?> cache, Class<?> connection) {
                return null;
            }

            @Override
            public boolean updateCache(ValueCache cache, Class<?> connection, Object message) {
                return false;
            }
        };
    }

    @Test
    public void find1() {
        PayloadToTypeMatcher matcher = new PayloadToTypeMatcher();
        Collection<PayloadToTypeConverter<Class<?>, Object>> converters = new ArrayList<PayloadToTypeConverter<Class<?>, Object>>();
        PayloadToTypeConverter<Class<?>, Object> converter = createMockConverter(Double.class);
        converters.add(converter);
        PayloadToTypeConverter<Class<?>, Object> matched = matcher.find(converters, new ValueCache<Number>(Number.class), Double.class);
        assertThat(matched, sameInstance(converter));
    }

    @Test(expected=IllegalStateException.class)
    public void find2() {
        PayloadToTypeMatcher matcher = new PayloadToTypeMatcher();
        Collection<PayloadToTypeConverter<Class<?>, Object>> converters = new ArrayList<PayloadToTypeConverter<Class<?>, Object>>();
        PayloadToTypeConverter<Class<?>, Object> converter = createMockConverter(Double.class);
        converters.add(converter);
        PayloadToTypeConverter<Class<?>, Object> matched = matcher.find(converters, new ValueCache<Number>(Number.class), String.class);
    }
}
