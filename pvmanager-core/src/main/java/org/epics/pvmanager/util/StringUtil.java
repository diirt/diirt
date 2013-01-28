/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.epics.pvmanager.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author carcassi
 */
public class StringUtil {

    private StringUtil() {
    }
    
    static Pattern escapeSequence = Pattern.compile("(\\\\(\"|\\\\|\'|r|n))");
    
    public static String unescapeString(String escapedString) {
        Matcher match = escapeSequence.matcher(escapedString);
        StringBuffer output = new StringBuffer();
        while(match.find()) {
            match.appendReplacement(output, substitution(match.group()));
        }
        match.appendTail(output);
        return output.toString();
    }
    
    private static String substitution(String escapedToken) {
        switch (escapedToken) {
            case "\\\"":
                return "\"";
            case "\\\\":
                return "\\\\";
            case "\\\'":
                return "\'";
            case "\\r":
                return "\r";
            case "\\n":
                return "\n";
        }
        throw new IllegalArgumentException("Unknown escape token " + escapedToken);
    }
    
}
