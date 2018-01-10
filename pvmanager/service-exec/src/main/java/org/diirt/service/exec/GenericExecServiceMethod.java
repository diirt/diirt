/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.service.exec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import org.diirt.service.ServiceMethod;
import java.util.HashMap;
import java.util.Map;
import org.diirt.service.ServiceDescription;
import org.diirt.service.ServiceMethodDescription;
import org.diirt.vtype.VString;
import org.diirt.vtype.VTable;
import org.diirt.vtype.ValueFactory;
import org.diirt.vtype.io.CSVIO;

/**
 * The implementation of a generic exec service method: for execution of shell
 * commands through command line.
 *
 * @author carcassi
 */
class GenericExecServiceMethod extends ServiceMethod {

    GenericExecServiceMethod(ServiceMethodDescription serviceMethodDescription, ServiceDescription serviceDescription) {
        super(serviceMethodDescription, serviceDescription);
    }

    @Override
    public Map<String, Object> syncExecImpl(final Map<String, Object> parameters) throws Exception{
        String shell = defaultShell();
        String shellArg = defaultShellArg();
        String command = ((VString) parameters.get("command")).getValue();
        return syncExecuteCommand(parameters, shell, shellArg, command);
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
        return System.getProperties().get("os.name").toString().toLowerCase().contains("win");
    }

    static Map<String, Object> syncExecuteCommand(final Map<String, Object> parameters, final String shell, final String shellArg, final String command) throws Exception {
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
                return resultMap;
            } catch (Exception ex) {
                // Can't parse output to a table
            }

            // Return output as a String
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("output", ValueFactory.newVString(output, ValueFactory.alarmNone(), ValueFactory.timeNow()));
            return resultMap;

        } catch (Exception ex) {
            if (process != null) {
                // Try to kill the process if it was created
                try {
                    process.destroy();
                } catch (Exception ex1) {
                    // Ignore any error
                }
            }
            throw new RuntimeException(ex);
        }
    }
}
