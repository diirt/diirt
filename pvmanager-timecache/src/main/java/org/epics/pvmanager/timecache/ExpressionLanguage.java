/**
 * Copyright (C) 2010-14 pvmanager developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.pvmanager.timecache;

import java.util.Arrays;
import org.epics.pvmanager.vtype.DataTypeSupport;
import org.epics.pvmanager.BasicTypeSupport;
import org.epics.pvmanager.expression.DesiredRateExpression;
import org.epics.pvmanager.ReadFunction;
import org.epics.pvmanager.expression.DesiredRateExpressionImpl;
import org.epics.pvmanager.expression.DesiredRateExpressionListImpl;
import org.epics.pvmanager.timecache.query.QueryParameters;
import org.epics.util.array.ArrayDouble;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;

/**
 * PVManager expression language support for additional operations.
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    static {
        // Add support for Epics types.
        DataTypeSupport.install();
        // Add support for Basic types
        BasicTypeSupport.install();
    }

    /**
     * A query expression that returns the result formatted as a table.
     * 
     * @param channelName
     * @param parameters 
     * @return a new expression
     */
    public static DesiredRateExpression<VTable> timeTableOf(final String channelName, QueryParameters parameters) {
        // TODO: Create the cache and query here. This method must return as soon as possible.
        return new DesiredRateExpressionImpl<VTable>(new DesiredRateExpressionListImpl<Object>(), 
                new ReadFunction<VTable>() {
                    
            private VTable previousValue;
            
            {
                previousValue = ValueFactory.newVTable(Arrays.<Class<?>>asList(String.class, double.class), 
                        Arrays.asList("Name", "Value"), 
                        Arrays.asList(Arrays.asList(channelName, "B", "C"), new ArrayDouble(3.4, 1.2, 6.5)));
            }

            @Override
            public VTable readValue() {
                // TODO: hook up the code that returns a table
                // It should create table by merging chuncks as they
                // come
                // If not new data is available, return the previous table
                return previousValue;
            }
        }, channelName);
    }

}
