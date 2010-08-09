/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.data;

import org.epics.pvmanager.Expression;

/**
 * PVManager expression language support for EPICS types.
 *
 * @author carcassi
 */
public class ExpressionLanguage {
    private ExpressionLanguage() {}

    static {
        // Add support for Epics types.
        EpicsTypeSupport.install();
    }

    /**
     * A channel with the given name of type VDouble.
     * @param name the channel name; can't be null
     * @return an expression representing the channel
     */
    public static Expression<VDouble> vDouble(String name) {
        return new Expression<VDouble>(name, VDouble.class);
    }

}
