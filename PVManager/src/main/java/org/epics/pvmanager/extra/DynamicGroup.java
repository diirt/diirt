/*
 * Copyright 2008-2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.extra;

import java.util.ArrayList;
import java.util.List;
import org.epics.pvmanager.DataRecipe;
import org.epics.pvmanager.DataSource;
import org.epics.pvmanager.DesiredRateExpression;
import org.epics.pvmanager.PVManager;

/**
 *
 * @author carcassi
 */
public class DynamicGroup extends DesiredRateExpression<List<Object>> {

    private final DataSource dataSource = PVManager.getDefaultDataSource();
    private final List<DataRecipe> recipes = new ArrayList<DataRecipe>();
    
    public DynamicGroup() {
        super((DesiredRateExpression<?>) null, new DynamicGroupFunction(), "dynamic group");
    }

    DynamicGroupFunction getGroup() {
        return (DynamicGroupFunction) getFunction();
    }
    
    public synchronized DynamicGroup add(DesiredRateExpression<?> expression) {
        DataRecipe recipe = expression.getDataRecipe();
        dataSource.connect(recipe);
        recipes.add(recipe);
        getGroup().getArguments().add(expression.getFunction());
        return this;
    }
    
    public synchronized DynamicGroup remove(int index) {
        DataRecipe recipe = recipes.remove(index);
        getGroup().getArguments().remove(index);
        dataSource.disconnect(recipe);
        return this;
    }
    
    public synchronized DynamicGroup set(int index, DesiredRateExpression<?> expression) {
        DataRecipe recipe = expression.getDataRecipe();
        DataRecipe oldRecipe = recipes.get(index);
        dataSource.disconnect(oldRecipe);
        
        dataSource.connect(recipe);
        recipes.set(index, recipe);
        getGroup().getArguments().set(index, expression.getFunction());
        return this;
    }
}
