/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.sample;

import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.formula.ArrayFunctionSet;
import org.epics.pvmanager.formula.FormulaRegistry;
import org.epics.pvmanager.formula.MathFunctionSet;
import org.epics.pvmanager.formula.NumberOperatorFunctionSet;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.jca.JCADataSourceBuilder;
import org.epics.pvmanager.loc.LocalDataSource;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.pvmanager.util.Executors;
import org.epics.pvmanager.sys.SystemDataSource;

/**
 *
 * @author carcassi
 */
public class SetupUtil {
    public static void defaultCASetup() {
        CompositeDataSource dataSource = new CompositeDataSource();
        dataSource.putDataSource("sim", SimulationDataSource.simulatedData());
        dataSource.putDataSource("ca", new JCADataSourceBuilder().build());
        dataSource.putDataSource("loc", new LocalDataSource());
        dataSource.putDataSource("sys", new SystemDataSource());
        dataSource.setDefaultDataSource("ca");
        PVManager.setDefaultDataSource(dataSource);
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new ArrayFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new MathFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new NumberOperatorFunctionSet());
    }
    public static void defaultCASetupForSwing() {
        PVManager.setDefaultNotificationExecutor(Executors.swingEDT());
        defaultCASetup();
    }
}
