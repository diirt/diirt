/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.support.cf.formula;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.diirt.datasource.formula.StatefulFormulaFunction;
import org.diirt.service.ServiceMethod;
import org.diirt.service.ServiceRegistry;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;

/**
 * Function that connects to the Channel Finder server, runs the query,
 * and returns the result.
 *
 * @author Kunal Shroff
 */
public class CFQueryFunction extends StatefulFormulaFunction {

    // Function state (will be different for each use of the function)
    private VString currentQuery;
    private volatile VTable currentResult;
    private volatile Exception currentException;
    private ServiceMethod serviceMethod;

    @Override
    public boolean isVarArgs() {
        return false;
    }

    @Override
    public String getName() {
        return "cfQuery";
    }

    @Override
    public String getDescription() {
        return "Query ChannelFinder";
    }

    @Override
    public List<Class<?>> getArgumentTypes() {
        return Arrays.<Class<?>>asList(VString.class);
    }

    @Override
    public List<String> getArgumentNames() {
        return Arrays.asList("query");
    }

    @Override
    public Class<?> getReturnType() {
        return VTable.class;
    }

    @Override
    public Object calculate(List<Object> args) {
        if (currentQuery == null || !((VString) args.get(0)).getValue().equals(currentQuery.getValue())) {
            currentQuery = (VString) args.get(0);
            serviceMethod = ServiceRegistry.getDefault().findServiceMethod("cf/find");
            serviceMethod.executeAsync(Collections.<String, Object>singletonMap("query", currentQuery),
                    (Map<String, Object> newValue) -> {
                        currentResult = (VTable) newValue.get("result");
                        currentException = null;
                    }, (Exception newValue) -> {
                        currentException = newValue;
                        currentResult = null;
                    });
        }
        if (currentException != null) {
            if (currentException instanceof RuntimeException) {
                throw (RuntimeException) currentException;
            } else {
                throw new RuntimeException("Call to ChannelFinder service failed", currentException);
            }
        }
        return currentResult;
    }

}
