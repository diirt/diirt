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
    
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends TypeSupport>, TypeSupportMap> allTypeSupports = 
        new ConcurrentHashMap<Class<? extends TypeSupport>, TypeSupportMap>();
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends TypeSupport>, TypeSupportMap> allCalcTypeSupports = 
        new ConcurrentHashMap<Class<? extends TypeSupport>, TypeSupportMap>();
    
    
    @SuppressWarnings("rawtypes")
    private static 
    void addTypeSupportFamilyIfNotExists(final Map<Class<? extends TypeSupport>, TypeSupportMap> map, 
                                         final Class<? extends TypeSupport> typeSupportFamily) {
        
        TypeSupportMap<?> familyMap = (TypeSupportMap<?>) map.get(typeSupportFamily);
        if (familyMap == null) {
            TypeSupportMap<?> supportMap = new TypeSupportMap();
            map.put(typeSupportFamily, supportMap);
        }
    }
    
    
    @SuppressWarnings("unchecked")
    public static <T>
    void addTypeSupport(final Class<T> typeClass, 
                        final TypeSupport<T> typeSupport) {
        // typeSupport.getClass() wouldn't work, as anonymous derived classes of XXXTypeSupport 
        // differ by their concrete class 
        Class<? extends TypeSupport<T>> typeSupportFamily = typeSupport.getTypeSupportFamily();
        
        addTypeSupportFamilyIfNotExists(allTypeSupports, typeSupportFamily);
        addTypeSupportFamilyIfNotExists(allCalcTypeSupports, typeSupportFamily);
        
        allTypeSupports.get(typeSupportFamily).put(typeClass, typeSupport);
        allCalcTypeSupports.get(typeSupportFamily).remove(typeClass);
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
        
        TypeSupport<T> support = (TypeSupport<T>) calcSupportMap.get(typeClass);
        if (support == null) {
            support = recursiveTypeSupportFor(typeClass, supportMap);
            if (support == null) {
                Class<? super T> superClass = typeClass.getSuperclass();
                while (!superClass.equals(Object.class)) {
                    support = (TypeSupport<T>) supportMap.get(superClass);
                    if (support != null) {
                        break;
                    }
                    superClass = superClass.getSuperclass();
                }
            }
            if (support == null) {
                // TODO (carcassi) : unchecked vs checked? a dedicated TypeSupportException? I don't know...
                throw new RuntimeException("No support found for type " + typeClass);
            }
            calcSupportMap.put(typeClass, support);
        }
        return support;
    }


    public abstract Class<? extends TypeSupport<T>> getTypeSupportFamily();
}
