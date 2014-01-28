/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.util.text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.epics.util.text.StringUtil.DOUBLE_REGEX;

/**
 * Utility class to parse CSV text.
 *
 * @author carcassi
 */
public class CSVParser {
    
    /**
     * Parses a line of text representing comma separated values and returns
     * the values themselves.
     * 
     * @param line the line to parse
     * @param separatorChar the regular expression for the separator
     * @return the list of values
     */
    public static List<Object> parseCSVLine(String line, String separatorChar) {
        String regex = // puts a doublequoted field in group(1) and an unquoted field into group(2)
                "\\G(?:^|" + separatorChar + ")" +
                "(?:" +
                "\"" +
                "((?:[^\"]++|\"\")*+)" +
                "\"" +
                "|" +
                "([^\"" + separatorChar + "]*)" +
                ")";
        Matcher mMain = Pattern.compile(regex).matcher("");
        Matcher mQuote = Pattern.compile("\"\"").matcher("");
        Matcher mDouble = Pattern.compile(DOUBLE_REGEX).matcher("");
        
        List<Object> values = new ArrayList<>();
        mMain.reset(line);
        while (mMain.find()) {
            Object value;
            if (mMain.start(2) >= 0) {
                String field = mMain.group(2);
                if (mDouble.reset(field).matches()) {
                    value = Double.parseDouble(field);
                } else {
                    value = field;
                }
            } else {
                // If quoted, always use string
                value = mQuote.reset(mMain.group(1)).replaceAll("\"");
            }
            values.add(value);
        }
        return values;
    }
    
    /**
     * Parses a line of text representing comma separated values and returns
     * the tokens.
     * 
     * @param line the line to parse
     * @param separatorChar the regular expression for the separator
     * @return the list of values
     */
    static List<String> csvTokens(String line, String separatorChar) {
        String regex = // puts a doublequoted field in group(1) and an unquoted field into group(2)
                "\\G(?:^|" + separatorChar + ")" +
                "(?:" +
                "\"" +
                "((?:[^\"]++|\"\")*+)" +
                "\"" +
                "|" +
                "([^\"" + separatorChar + "]*)" +
                ")";
        Matcher mMain = Pattern.compile(regex).matcher("");
        Matcher mQuote = Pattern.compile("\"\"").matcher("");
        
        List<String> tokens = new ArrayList<>();
        mMain.reset(line);
        while (mMain.find()) {
            String field;
            if (mMain.start(2) >= 0) {
                field = mMain.group(2);
            } else {
                field = mQuote.reset(mMain.group(1)).replaceAll("\"");
            }
            tokens.add(field);
        }
        return tokens;
    }
}
