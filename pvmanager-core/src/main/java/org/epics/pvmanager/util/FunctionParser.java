/**
 * Copyright (C) 2010-12 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.pvmanager.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.epics.util.text.StringUtil;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;

/**
 * Utility class to parse variable names and create simulated signals.
 *
 * @author carcassi
 */
public class FunctionParser {

    public static final String STRING_OR_DOUBLE_REGEX = "(" + StringUtil.DOUBLE_REGEX + "|" + StringUtil.QUOTED_STRING_REGEX + ")";
    static final Pattern doubleParameter = Pattern.compile("\\s*(" + StringUtil.DOUBLE_REGEX + ")\\s*");
    static final Pattern stringParameter = Pattern.compile("\\s*(" + StringUtil.QUOTED_STRING_REGEX + ")\\s*");
    static final Pattern commaSeparatedDoubles = Pattern.compile(doubleParameter + "(," + doubleParameter + ")*");
    static final Pattern commaSeparatedStrings = Pattern.compile(stringParameter + "(," + stringParameter + ")*");
    static final Pattern commaSeparatedStringOrDoubles = Pattern.compile("\\s*" + STRING_OR_DOUBLE_REGEX + "(\\s*,\\s*" + STRING_OR_DOUBLE_REGEX + ")*\\s*");
    static final Pattern functionAndParameter = Pattern.compile("(\\w+)(\\(((" + commaSeparatedDoubles + ")?)\\))?");
    static final Pattern functionAndStringParameter = Pattern.compile("(\\w+)(\\((\".*\")\\))?");
    static final Pattern pvNameAndParameter = Pattern.compile("([^\\(]+)(\\(((" + commaSeparatedDoubles + ")?)\\))?");
    static final Pattern pvNameAndStringParameter = Pattern.compile("([^\\(]+)(\\((" + commaSeparatedStrings + ")\\))?");
    static final Pattern functionAndParameters = Pattern.compile("(\\w+)(\\(((" + commaSeparatedStringOrDoubles + ")?)\\))?");
 
    /**
     * Parses a comma separated list of arguments and returns them as a list.
     *
     * @param string a comma separated list of arguments; if null or empty
     * returns the empty list
     * @return the list of parsed arguments
     */
    static List<Object> parseParameters(String string) {
        // Argument is empty
        if (string == null || "".equals(string)) {
            return Collections.emptyList();
        }

        // Validate input
        if (!commaSeparatedDoubles.matcher(string).matches()) {
            return null;
        }

        // Parse parameters
        Matcher matcher = doubleParameter.matcher(string);
        List<Object> parameters = new ArrayList<Object>();
        while (matcher.find()) {
            String parameter = matcher.group();
            Double value = Double.parseDouble(parameter);
            parameters.add(value);
        }

        return parameters;
    }
    
    private static List<Object> parseStringParameters(String string) {
        // Parse parameters
        Matcher matcher = stringParameter.matcher(string);
        List<Object> parameters = new ArrayList<Object>();
        while (matcher.find()) {
            String parameter = matcher.group(1);
            parameters.add(StringUtil.unquote(parameter));
        }

        return parameters;
    }

    /**
     * Parse a function with parameters and returns a list where the first
     * element is the function name and the others are the parsed arguments.
     *
     * @param string a string representing a function
     * @return the name and the parameters
     */
    public static List<Object> parseFunction(String string) {
        Matcher matcher = functionAndParameter.matcher(string);
        // Match comma separate double list
        if (matcher.matches()) {
            List<Object> parameters = new ArrayList<Object>();
            parameters.add(matcher.group(1));
            parameters.addAll(parseParameters(matcher.group(3)));
            return parameters;
        }

        // Match string parameter
        matcher = functionAndStringParameter.matcher(string);
        if (matcher.matches()) {
            List<Object> parameters = new ArrayList<Object>();
            parameters.add(matcher.group(1));
            String quotedString = matcher.group(3);
            parameters.add(quotedString.substring(1, quotedString.length() - 1));
            return parameters;
        }

        return null;
    }

    /**
     * Parse a pv name with parameters and returns a list where the first
     * element is the function name and the others are the parsed arguments.
     *
     * @param string a string representing a function
     * @return the name and the parameters
     */
    public static List<Object> parsePvAndArguments(String string) {
        Matcher matcher = pvNameAndParameter.matcher(string);
        // Match comma separate double list
        if (matcher.matches()) {
            List<Object> parameters = new ArrayList<Object>();
            parameters.add(matcher.group(1));
            parameters.addAll(parseParameters(matcher.group(3)));
            return parameters;
        }

        // Match string parameter
        matcher = pvNameAndStringParameter.matcher(string);
        if (matcher.matches()) {
            List<Object> parameters = new ArrayList<Object>();
            parameters.add(matcher.group(1));
            String quotedString = matcher.group(3);
            
            parameters.addAll(parseStringParameters(quotedString));
            //parameters.add(quotedString.substring(1, quotedString.length() - 1));
            return parameters;
        }

        return null;
    }
    
    /**
     * Parse a function that accepts a scalar value (number or string) or
     * an array value (number or string).
     * 
     * @param string the string to be parsed
     * @param errorMessage the error message
     * @return the name of the function and the argument
     */
    public static List<Object> parseFunctionWithScalarOrArrayArguments(String string, String errorMessage) {
        // Parse the channel name
        List<Object> parsedTokens = FunctionParser.parsePvAndArguments(string);
        
        // Single argument, return right away
        if (parsedTokens != null && parsedTokens.size() <= 2) {
            return parsedTokens;
        }
        
        // Multiple arguments, collect in array if possible
        Object data = asScalarOrList(parsedTokens.subList(1, parsedTokens.size()));
        if (data == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        return Arrays.asList(parsedTokens.get(0), data);
    }
    
    /**
     * Converts the list of arguments into a scalar or
     * an appropriate list. Returns null if it's not possible.
     * 
     * @param objects the argument list
     * @return the value converted or null
     */
    static Object asScalarOrList(List<Object> objects) {
        if (objects.size() <=1) {
            return objects;
        } else if (objects.get(0) instanceof Double) {
            return asListDouble(objects);
        } else if (objects.get(0) instanceof String) {
            return asListString(objects);
        } else {
            return null;
        }
    }
    
    /**
     * Convert the list of arguments to a ListDouble. Returns
     * null if it's not possible.
     * 
     * @param objects a list of arguments
     * @return the converted list or null
     */
    static ListDouble asListDouble(List<Object> objects) {
        double[] data = new double[objects.size()];
        for (int i = 0; i < objects.size(); i++) {
            Object value = objects.get(i);
            if (value instanceof Double) {
                data[i] = (Double) value;
            } else {
                return null;
            }
        }
        return new ArrayDouble(data);
    }

    /**
     * Convert the list of arguments to a List. Returns
     * null if it's not possible.
     * 
     * @param objects a list of arguments
     * @return  the converted list of null
     */
    static List<String> asListString(List<Object> objects) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            Object value = objects.get(i);
            if (value instanceof String) {
                data.add((String) value);
            } else {
                return null;
            }
        }
        return data;
    }
    
    public static List<Object> parseFunctionAnyParameter(String string) {
        if (!functionAndParameters.matcher(string).matches()) {
            return null;
        }
        if (string.indexOf('(') == -1) {
            return Arrays.<Object>asList(string);
        }
        
        String name = string.substring(0, string.indexOf('('));
        String arguments = string.substring(string.indexOf('(') + 1, string.lastIndexOf(')'));
        List<Object> result = new ArrayList<>();
        result.add(name);
        result.addAll(StringUtil.parseCSVLine(arguments.trim(), "\\s*,\\s*"));
        return result;
    }
}
