/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import org.diirt.datasource.CompositeDataSource;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.ca.JCADataSourceBuilder;
import org.diirt.datasource.file.FileDataSource;
import org.diirt.datasource.loc.LocalDataSource;
import org.diirt.datasource.formula.ArrayFunctionSet;
import org.diirt.datasource.formula.FormulaRegistry;
import org.diirt.datasource.formula.MathFunctionSet;
import org.diirt.datasource.formula.NumberOperatorFunctionSet;
import org.diirt.datasource.formula.StringFunctionSet;
import org.diirt.datasource.formula.TableFunctionSet;
import org.diirt.datasource.sim.SimulationDataSource;
import org.diirt.datasource.util.Executors;
import org.diirt.datasource.sys.SystemDataSource;

/**
 *
 * @author carcassi
 */
public class SetupUtil {
    public static void defaultCASetup() {
        System.setProperty("com.cosylab.epics.caj.CAJContext.max_array_bytes", "10000000");
    }
    public static void defaultCASetupForSwing() {
        PVManager.setDefaultNotificationExecutor(Executors.swingEDT());
        defaultCASetup();
    }
}
