/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Implements the mechanism for registering different types so that the library
 * knows how to handle them.
 * <p>
 * For a type to be usable by the library it needs to be defined:
 * <ul>
 *   <li>How to copy - since values given to the UI should be modified only
 *   within the UI thread, it follows that new values cannot be prepared
 *   "in place", on the same object that was given to the UI. At notification,
 *   there will be then two copies, the old and the new, and in need to be clear
 *   how the new copy should be delivered. (e.g. just pass the new copy, modify
 *   the old object in place, etc...).</li>
 *   <li>When to notify - by comparing elements of the value, it should
 *   decide on what condition the old value need to be modified and the
 *   UI should be notified of the change.</li>
 * </ul>
 *
 * @author carcassi
 */
public abstract class TypeSupport<T> {
    
    /**
     * Internal class to improve readability.
     * @author bknerr
     * @since 20.01.2011
     */
    private static final class TypeSupportMap<T> extends ConcurrentHashMap<Class<T>, TypeSupport<T>> {
        private static final long serialVersionUID = -8726785703555122582L;
        public TypeSupportMap() { /* EMPTY */ }
    }
    
    private static final Map<Class<? extends TypeSupport>, TypeSupportMap> allTypeSupports = 
        new ConcurrentHashMap<Class<? extends TypeSupport>, TypeSupportMap>();
    private static final Map<Class<? extends TypeSupport>, TypeSupportMap> allCalcTypeSupports = 
        new ConcurrentHashMap<Class<? extends TypeSupport>, TypeSupportMap>();
    
    
    private static 
    void addTypeSupportFamilyIfNotExists(final Map<Class<? extends TypeSupport>, TypeSupportMap> map, 
                                         final Class<? extends TypeSupport> typeSupportFamily) {
        TypeSupportMap<?> familyMap = (TypeSupportMap<?>) map.get(typeSupportFamily);
        if (familyMap == null) {
            TypeSupportMap<?> supportMap = new TypeSupportMap();
            map.put(typeSupportFamily, supportMap);
        }
    }
    
    /**
     * Adds type support for the given class. The type support added will apply
     * to the given class and all of its subclasses. Support of the same
     * family cannot be added twice and will cause an exception. Support for
     * the more specific subclass overrides support for the more abstract class.
     * A class cannot have two types support in the same family coming from
     * two different and unrelated interfaces.
     *
     * @param <T> type to add support for
     * @param typeClass type to add support for
     * @param typeSupport the support to add
     */
    public static
    void addTypeSupport(final TypeSupport<?> typeSupport) {
        Class<? extends TypeSupport<?>> typeSupportFamily = typeSupport.getTypeSupportFamily();
        
        addTypeSupportFamilyIfNotExists(allTypeSupports, typeSupportFamily);
        addTypeSupportFamilyIfNotExists(allCalcTypeSupports, typeSupportFamily);

        // Can't install support for the same type twice
        if (allTypeSupports.get(typeSupportFamily).get(typeSupport.getType()) != null) {
            throw new RuntimeException(typeSupportFamily.getSimpleName() + " was already added for type " + typeSupport.getType().getName());
        }
        
        allTypeSupports.get(typeSupportFamily).put(typeSupport.getType(), typeSupport);
        // Need to clear all calculated supports since registering an
        // interface may affect all the calculated supports
        // of all the implementations
        allCalcTypeSupports.get(typeSupportFamily).clear();
    }
    
    
    
    /**
     * Retrieve support for the given type and if not found looks at the
     * implemented interfaces.
     *
     * @param <T> the type to retrieve support for
     * @param typeClass the class of the type
     * @return the support for the type or null
     */
    @SuppressWarnings("unchecked")
    private static <T> TypeSupport<T> recursiveTypeSupportFor(final Class<T> typeClass,
                                                              final TypeSupportMap<?> supportMap) {
        TypeSupport<T> support = (TypeSupport<T>) supportMap.get(typeClass);
        if (support == null) {
            for (@SuppressWarnings("rawtypes") final Class clazz : typeClass.getInterfaces()) {
                support = recursiveTypeSupportFor(clazz, supportMap);
                if (support != null) {
                    return support;
                }
            }
        }
        return support;
    }

    /**
     * Calculates and caches the type support for a particular class, so that
     * introspection does not occur at every call.
     * 
     * First the supertypes are recursively 
     *
     * @param <T> the type to retrieve support for
     * @param typeClass the class of the type
     * @return the support for the type or null
     * @throws RuntimeException when no support could be identified 
     */
    @SuppressWarnings("unchecked")
    protected static <T> TypeSupport<T> cachedTypeSupportFor(@SuppressWarnings("rawtypes") final Class<? extends TypeSupport> supportFamily,
                                                             final Class<T> typeClass) {
        
        TypeSupportMap<T> calcSupportMap = allCalcTypeSupports.get(supportFamily);
        TypeSupportMap<T> supportMap = allTypeSupports.get(supportFamily);
        
        if (supportMap == null || calcSupportMap == null) {
            throw new RuntimeException("No type support found for family " + supportFamily, null);
        }
        
        TypeSupport<T> support = (TypeSupport<T>) calcSupportMap.get(typeClass);
        if (support == null) {
            support = recursiveTypeSupportFor(typeClass, supportMap);
            if (support == null) {
                support = recursiveClassSupportFor(typeClass, supportMap);
            }
            if (support == null) {
                // TODO (carcassi) : unchecked vs checked? a dedicated TypeSupportException? I don't know...
                throw new RuntimeException("No support found for type " + typeClass);
            }
            calcSupportMap.put(typeClass, support);
        }
        return support;
    }

    private static <T> TypeSupport<T> recursiveClassSupportFor(final Class<T> typeClass,
                                                               final TypeSupportMap<T> supportMap) {
        Class<? super T> superClass = typeClass.getSuperclass();
        TypeSupport<T> support = null;
        while (!superClass.equals(Object.class)) {
            support = (TypeSupport<T>) supportMap.get(superClass);
            if (support != null) {
                break;
            }
            superClass = superClass.getSuperclass();
        }
        return support;
    }

    /**
     * Creates a new type support of the given type
     * 
     * @param type the type on which support is defined
     */
    public TypeSupport(Class<T> type, Class<? extends TypeSupport<T>> typeSupportFamily) {
        this.type = type;
        this.typeSupportFamily = typeSupportFamily;
    }

    // Type on which the support is defined
    private final Class<T> type;

    // Which kind of type support is defined
    private final Class<? extends TypeSupport<T>> typeSupportFamily;


    /**
     * Defines which type of support is implementing, notification or time.
     *
     * @return the support family
     */
    private Class<? extends TypeSupport<T>> getTypeSupportFamily() {
        return typeSupportFamily;
    }

    /**
     * Defines on which class the support is defined.
     */
    private Class<T> getType() {
        return type;
    }

}
