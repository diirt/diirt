/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.sample;

import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.PVManager;
import org.epics.pvmanager.file.FileDataSource;
import org.epics.pvmanager.formula.ArrayFunctionSet;
import org.epics.pvmanager.formula.FormulaRegistry;
import org.epics.pvmanager.formula.MathFunctionSet;
import org.epics.pvmanager.formula.NumberOperatorFunctionSet;
import org.epics.pvmanager.formula.StringFunctionSet;
import org.epics.pvmanager.formula.TableFunctionSet;
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
        System.setProperty("com.cosylab.epics.caj.CAJContext.max_array_bytes", "10000000");
        dataSource.putDataSource("ca", new JCADataSourceBuilder().build());
        dataSource.putDataSource("loc", new LocalDataSource());
        dataSource.putDataSource("sys", new SystemDataSource());
        dataSource.putDataSource("file", new FileDataSource());
        dataSource.setDefaultDataSource("ca");
        PVManager.setDefaultDataSource(dataSource);
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new ArrayFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new MathFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new NumberOperatorFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new TableFunctionSet());
        FormulaRegistry.getDefault().registerFormulaFunctionSet(new StringFunctionSet());
    }
    public static void defaultCASetupForSwing() {
        PVManager.setDefaultNotificationExecutor(Executors.swingEDT());
        defaultCASetup();
    }
}
