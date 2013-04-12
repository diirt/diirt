/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.formula;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author carcassi
 */
public class FormulaRegistry {
    private final static FormulaRegistry registry = new FormulaRegistry();

    public static FormulaRegistry getDefault() {
        return registry;
    }
    
    public void registerFormulaFunctionSet(FormulaFunctionSet functionSet) {
        functionSets.put(functionSet.getName(), functionSet);
        for (FormulaFunction formulaFunction : functionSet.getFunctions()) {
            registerFormulaFunction(formulaFunction);
        }
    }
    
    private Map<String, FormulaFunctionSet> functionSets = new ConcurrentHashMap<>();
    private Map<String, Map<Integer, Collection<FormulaFunction>>> formulaFunctions = new ConcurrentHashMap<>();
    
    private void registerFormulaFunction(FormulaFunction formulaFunction) {
        // Get the map based by name
        Map<Integer, Collection<FormulaFunction>> functionForName = formulaFunctions.get(formulaFunction.getName());
        if (functionForName == null) {
            functionForName = new ConcurrentHashMap<>();
            formulaFunctions.put(formulaFunction.getName(), functionForName);
        }
        
        // Get the collection based on number of arguments
        Collection<FormulaFunction> functionsForNArguments = functionForName.get(formulaFunction.getArgumentNames().size());
        if (functionsForNArguments == null) {
            functionsForNArguments = Collections.newSetFromMap(new ConcurrentHashMap<FormulaFunction, Boolean>());
            functionForName.put(formulaFunction.getArgumentNames().size(), functionsForNArguments);
        }
        
        // Add formula
        functionsForNArguments.add(formulaFunction);
    }
    
    public Set<String> listFunctionSets() {
        return Collections.unmodifiableSet(new HashSet<>(functionSets.keySet()));
    }
    
    public FormulaFunctionSet findFunctionSet(String name) {
        return functionSets.get(name);
    }
    
    public Collection<FormulaFunction> findFunctions(String functionName, Integer nArguments) {
        FormulaFunctionSet set = functionSets.get(functionName);
        if (set == null) {
            return Collections.emptySet();
        }
        
        Collection<FormulaFunction> functions = set.getFunctions();
        if (functions == null) {
            return Collections.emptySet();
        }
        
        return Collections.unmodifiableCollection(functions);
    }
}
