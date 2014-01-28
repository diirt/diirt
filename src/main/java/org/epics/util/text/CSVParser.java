/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */

package org.epics.util.text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListDouble;
import org.epics.util.array.ListNumber;
import static org.epics.util.text.StringUtil.DOUBLE_REGEX;

/**
 * Utility class to parse CSV text.
 *
 * @author carcassi
 */
public class CSVParser {
    
    private final CsvParserConfiguration configuration;
    private int nColumns;
    private boolean columnMismatch = false;
    private List<String> columnNames;
    private List<Boolean> columnNumberParsable;
    private List<Boolean> columnTimestampParsable;
    private List<List<String>> columnTokens;
    
    private Matcher mLineTokens;
    private final Matcher mQuote = Pattern.compile("\"\"").matcher("");
    private final Matcher mDouble = Pattern.compile(DOUBLE_REGEX).matcher("");
    
    public CsvParserResult parse(Reader reader) {
        // Divide into lines
        List<String> lines = csvLines(reader);
        
        separatorLoop:
        for(int nSeparator = 0; nSeparator < configuration.getSeparators().length(); nSeparator++) {
            String currentSeparator = configuration.getSeparators().substring(nSeparator, nSeparator+1);
            
            String regex = // puts a doublequoted field in group(1) and an unquoted field into group(2)
                    "\\G(?:^|" + currentSeparator + ")" +
                    "(?:" +
                    "\"" +
                    "((?:[^\"]++|\"\")*+)" +
                    "\"" +
                    "|" +
                    "([^\"" + currentSeparator + "]*)" +
                    ")";
            mLineTokens = Pattern.compile(regex).matcher("");
            columnNames = parseTitles(lines.get(0));
            nColumns = columnNames.size();
            if (nColumns == 1) {
                continue;
            }
            columnMismatch = false;
            columnNumberParsable = new ArrayList<>(nColumns);
            columnTimestampParsable = new ArrayList<>(nColumns);
            columnTokens = new ArrayList<>();
            for (int i = 0; i < nColumns; i++) {
                columnNumberParsable.add(true);
                columnTimestampParsable.add(false);
                columnTokens.add(new ArrayList<String>());
            }
            for (int i = 1; i < lines.size(); i++) {
                parseLine(lines.get(i));
                if (columnMismatch) {
                    continue separatorLoop;
                }
            }
            break;
            
        }
        
        if (columnMismatch) {
            throw new RuntimeException("Number of columns is not the same for all lines");
        }
        
        List<Object> columnValues = new ArrayList<>(nColumns);
        List<Class<?>> columnTypes = new ArrayList<>(nColumns);
        for (int i = 0; i < nColumns; i++) {
            if (columnNumberParsable.get(i)) {
                columnValues.add(convertToListDouble(columnTokens.get(i)));
                columnTypes.add(double.class);
            } else {
                columnValues.add(columnTokens.get(i));
                columnTypes.add(String.class);
            }
        }
        
        return new CsvParserResult(columnNames, columnValues, columnTypes, lines.size() - 1);
    }

    public CSVParser(CsvParserConfiguration configuration) {
        this.configuration = configuration;
    }
    
    private ListDouble convertToListDouble(List<String> tokens) {
        double[] values = new double[tokens.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(tokens.get(i));
        }
        return new ArrayDouble(values);
    }
    
    static List<String> csvLines(Reader reader) {
        try {
            BufferedReader br = new BufferedReader(reader);
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch(IOException ex) {
            throw new RuntimeException("Couldn't process data", ex);
        }
    }
    
    private List<String> parseTitles(String line) {
        List<String> titles = new ArrayList<>();
        mLineTokens.reset(line);
        while (mLineTokens.find()) {
            String value;
            if (mLineTokens.start(2) >= 0) {
                value = mLineTokens.group(2);
            } else {
                // If quoted, always use string
                value = mQuote.reset(mLineTokens.group(1)).replaceAll("\"");
            }
            titles.add(value);
        }
        return titles;
    }
    
    private void parseLine(String line) {
        mLineTokens.reset(line);
        int nColumn = 0;
        while (mLineTokens.find()) {
            if (nColumn == nColumns) {
                columnMismatch = true;
                return;
            }
            String token;
            if (mLineTokens.start(2) >= 0) {
                token = mLineTokens.group(2);
                if (!mDouble.reset(token).matches()) {
                    columnNumberParsable.set(nColumn, false);
                }
            } else {
                // If quoted, always use string
                token = mQuote.reset(mLineTokens.group(1)).replaceAll("\"");
                columnNumberParsable.set(nColumn, false);
            }
            columnTokens.get(nColumn).add(token);
            nColumn++;
        }
        if (nColumn != nColumns) {
            columnMismatch = true;
        }
    }
    
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
