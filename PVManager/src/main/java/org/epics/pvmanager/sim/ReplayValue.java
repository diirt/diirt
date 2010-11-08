/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.sim;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.epics.pvmanager.TimeDuration;
import org.epics.pvmanager.TimeStamp;

/**
 *
 * @author carcassi
 */
class ReplayValue {
    private static List<Field> fields = new ArrayList<Field>();
    private static final Logger log = Logger.getLogger(ReplayValue.class.getName());

    @XmlAttribute @XmlJavaTypeAdapter(value=XmlTimeStampAdapter.class)
    TimeStamp timeStamp;

    public TimeStamp getTimeStamp() {
        return timeStamp;
    }

    private void updateFields(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) {
                fields.add(field);
            }
        }
        
        if (clazz.getSuperclass() != null) {
            updateFields(clazz.getSuperclass());
        }
    }

    {
        updateFields(getClass());
    }

    ReplayValue copy() {
        try {
            ReplayValue copy = getClass().newInstance();
            copy.updateValue(this);
            return copy;
        } catch (InstantiationException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Can't copy ReplayValue", ex);
        } catch (IllegalAccessException ex) {
            log.log(Level.SEVERE, null, ex);
            throw new RuntimeException("Can't copy ReplayValue", ex);
        }
    }

    void adjustTime(TimeDuration duration) {
        timeStamp = timeStamp.plus(duration);
    }

    void updateValue(ReplayValue obj) {
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

    void updateNullValues(ReplayValue obj) {
        if (!getClass().isInstance(obj)) {
            throw new RuntimeException("Updating value " + this + " from different class " + obj);
        }

        for (Field field : fields) {
            try {
                Object oldValue = field.get(this);
                if (oldValue == null) {
                    field.set(this, field.get(obj));
                }
            } catch (IllegalAccessException ex) {
                throw new RuntimeException("Field " + field + " is not accessible", ex);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException("Field " + field + " had an inconsistent value", ex);
            }
        }
    }

}
