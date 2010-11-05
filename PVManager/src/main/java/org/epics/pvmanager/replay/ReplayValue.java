/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.replay;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author carcassi
 */
class ReplayValue {
    private static List<Field> fields = new ArrayList<Field>();
    
    private void updateFields(Class<?> clazz) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        
        if (clazz.getSuperclass() != null) {
            updateFields(clazz.getSuperclass());
        }
    }

    {
        updateFields(getClass());
    }

    void updateValue(Object obj) {
        if (!getClass().isInstance(obj)) {
            throw new RuntimeException("Updating value " + this + " from different class " + obj);
        }
        
        for (Field field : fields) {
            try {
                Object newValue = field.get(obj);
                if (newValue != null) {
                    field.set(this, newValue);
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Field " + field + " is not accessible", ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Field " + field + " had an inconsistent value", ex);
            }
        }
    }

}
