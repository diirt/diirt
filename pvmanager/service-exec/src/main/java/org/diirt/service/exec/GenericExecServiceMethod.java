/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.diirt.service.ServiceMethod;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.io.CSVIO;

/**
 *
 * @author carcassi
 */
class GenericExecServiceMethod extends ServiceMethod {

    public GenericExecServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public void asyncExecImpl(final Map<String, Object> parameters, final Consumer<Map<String, Object>> callback, final Consumer<Exception> errorCallback) {
        
        //TODO: this was replaced with the executor from the parent class
        //ExecutorService executor = Executors.newSingleThreadExecutor();
        
        String shell = defaultShell();
        String shellArg = defaultShellArg();
        String command = ((VString) parameters.get("command")).getValue();
        executeCommand(parameters, callback, errorCallback, super.executor, shell, shellArg, command);
    }

    static String defaultShell() {
        if (isWindows()) {
            return "cmd";
        } else {
            return "/bin/bash";
        }
    }

    static String defaultShellArg() {
        if (isWindows()) {
            return "/c";
        } else {
            return "-c";
        }
    }

    static boolean isWindows() {
        return System.getProperties().get("os.name").toString().toLowerCase().indexOf("win") >= 0;
    }

    // TODO: should expose the synchronous executotion
    static void executeCommand(final Map<String, Object> parameters, final Consumer<Map<String, Object>> callback, final Consumer<Exception> errorCallback,
            final ExecutorService executor, final String shell, final String shellArg, final String command) {
        executor.submit(new Runnable() {

            @Override
            public void run() {
                Process process = null;
                try {
                    process = new ProcessBuilder(shell, shellArg, command).start();

                    // Read output to a text buffer
                    StringBuilder buffer = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                    String output = buffer.toString();

                    // Try parsing output as a table
                    try {
                        CSVIO io = new CSVIO();
                        VTable table = io.importVTable(new StringReader(output));
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("output", table);
                        callback.accept(resultMap);
                        return;
                    } catch (Exception ex) {
                        // Can't parse output to a table
                    }

                    // Return output as a String
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("output", ValueFactory.newVString(output, ValueFactory.alarmNone(), ValueFactory.timeNow()));
                    callback.accept(resultMap);

                } catch (Exception ex) {
                    if (process != null) {
                        // Try to kill the process if it was created
                        try {
                            process.destroy();
                        } catch (Exception ex1) {
                            // Ignore any error
                        }
                    }
                    errorCallback.accept(ex);
                }
            }
        });
    }
}
