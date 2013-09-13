/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.exec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import org.epics.pvmanager.WriteFunction;
import org.epics.pvmanager.service.ServiceMethod;
import org.epics.util.array.CircularBufferDouble;
import org.epics.util.time.Timestamp;
import org.epics.vtype.VNumber;
import org.epics.vtype.VString;
import org.epics.vtype.VTable;
import org.epics.vtype.ValueFactory;

/**
 * The implementation of an executor service method.
 *
 * @author carcassi
 */
class ExecServiceMethod extends ServiceMethod {
    
    private final ExecutorService executorService;
    private final String shell;
    private final String shellArg;
    private final String command;

    /**
     * Creates a new service method.
     * 
     * @param serviceMethodDescription a method description
     */
    ExecServiceMethod(ExecServiceMethodDescription serviceMethodDescription) {
        super(serviceMethodDescription.serviceMethodDescription);
        this.executorService = serviceMethodDescription.executorService;
        this.shell = serviceMethodDescription.shell;
        this.shellArg = serviceMethodDescription.shellArg;
        this.command = serviceMethodDescription.command;
    }

    @Override
    public void executeMethod(final Map<String, Object> parameters, final WriteFunction<Map<String, Object>> callback, final WriteFunction<Exception> errorCallback) {
        String expandedCommand = command;
        GenericExecServiceMethod.executeCommand(parameters, callback, errorCallback, executorService, shell, shellArg, expandedCommand);
    }
}
