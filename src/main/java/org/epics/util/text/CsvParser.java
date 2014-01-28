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
 * Utility class to parse CSV text. The parser is not thread safe. It can be
 * reused multiple times, but nothing much is gained.
 * <p>
 * Since there is no CSV strict format, this parser honors as best it
 * can the suggestions found in <a href="http://tools.ietf.org/html/rfc4180">RFC4180</a>,
 * in the <a haref="http://en.wikipedia.org/wiki/Comma-separated_values">CSV wikipedia article</a>
 * and other sources.
 * <p>
 * The parser can try multiple separators, so that it can auto-detect the
 * likely correct one. It does so by trying them one by one, checking
 * that it finds more than one column and that all the rows have the same
 * number of columns. If not, proceeds to the next separator.
 * <p>
 * The parsing of each line is based on code and insights found in
 * <a href="http://regex.info/book.html"> Mastering Regular Expressions</a>.
 *
 * @author carcassi
 */
public class CsvParser {
    
    private final CsvParserConfiguration configuration;
    
    // Parser state
    private int nColumns;
    private boolean columnMismatch = false;
    private List<String> columnNames;
    private List<Boolean> columnNumberParsable;
    private List<Boolean> columnTimestampParsable;
    private List<List<String>> columnTokens;
    
    // Regex object used for parsing
    private Matcher mLineTokens;
    private static final Pattern pQuote = Pattern.compile("\"\"");
    private final Matcher mQuote = pQuote.matcher("");
    private static final Pattern pDouble = Pattern.compile(DOUBLE_REGEX);
    private final Matcher mDouble = pDouble.matcher("");

    /**
     * Creates a new parser based on the given configuration.
     * 
     * @param configuration a configuration
     */
    public CsvParser(CsvParserConfiguration configuration) {
        this.configuration = configuration;
    }
    
    /**
     * Removes state left over from the parsing
     */
    private void clear() {
        columnNames = null;
        columnNumberParsable = null;
        columnTimestampParsable = null;
        columnTokens = null;
        mQuote.reset("");
        mDouble.reset("");
    }
    
    public CsvParserResult parse(Reader reader) {
        // Divide into lines.
        // Note that means we are going to keep in memory the whole file.
        // This is not very memory efficient. But since we have to do multiple
        // passes to find the right separator, we don't have much choice.
        // Also: the actual parsed result will need to stay in memory anyway.
        List<String> lines = csvLines(reader);
        
        // Try each seaparater
        separatorLoop:
        for(int nSeparator = 0; nSeparator < configuration.getSeparators().length(); nSeparator++) {
            String currentSeparator = configuration.getSeparators().substring(nSeparator, nSeparator+1);
            
            // Taken from Mastering Regular Exceptions
            // Disabled comments so that space could work as possible separator
            String regex = // puts a doublequoted field in group(1) and an unquoted field into group(2)
                    // Start with beginning of line or separator
                    "\\G(?:^|" + currentSeparator + ")" +
                    // Match a quoted string
                    "(?:" +
                    "\"" +
                    "((?:[^\"]++|\"\")*+)" +
                    "\"" +
                    // Or match a string without the separator
                    "|" +
                    "([^\"" + currentSeparator + "]*)" +
                    ")";
            // Compile the matcher once for all the parsing
            mLineTokens = Pattern.compile(regex).matcher("");
            
            // Try to parse the first line (the titles)
            // If only one columns is found, proceed to next separator
            columnNames = parseTitles(lines.get(0));
            nColumns = columnNames.size();
            if (nColumns == 1) {
                continue;
            }
            
            // Prepare the data structures to hold column data while parsing
            columnMismatch = false;
            columnNumberParsable = new ArrayList<>(nColumns);
            columnTimestampParsable = new ArrayList<>(nColumns);
            columnTokens = new ArrayList<>();
            for (int i = 0; i < nColumns; i++) {
                columnNumberParsable.add(true);
                columnTimestampParsable.add(false);
                columnTokens.add(new ArrayList<String>());
            }
            
            // Parse each line
            // If one line does not match the number of columns found in the first
            // line, pass to the next separator
            for (int i = 1; i < lines.size(); i++) {
                parseLine(lines.get(i));
                if (columnMismatch) {
                    continue separatorLoop;
                }
            }
            
            // The parsing succeeded! No need to try other separator
            break;
            
        }
        
        // We are out of the loop: did we end because we parsed correctly,
        // or because even the last separator was a mismatch?
        if (columnMismatch) {
            clear();
            return new CsvParserResult(null, null, null, 0, false, "Number of columns is not the same for all lines");
        }
        
        // Parsing was successful. Now it's time to convert the tokens
        // to the actual type.
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
        
        // Prepare result, and remember to clear the state, so
        // we don't keep references to junk
        CsvParserResult result = new CsvParserResult(columnNames, columnValues, columnTypes, lines.size() - 1, true, null);
        clear();
        return result;
    }
    
    /**
     * Given a list of tokens, convert them to a list of numbers.
     * 
     * @param tokens the tokens to be converted
     * @return the number list
     */
    private ListDouble convertToListDouble(List<String> tokens) {
        double[] values = new double[tokens.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = Double.parseDouble(tokens.get(i));
        }
        return new ArrayDouble(values);
    }

    /**
     * Divides the whole text into lines.
     * 
     * @param reader the source of text
     * @return the lines
     */
    static List<String> csvLines(Reader reader) {
        // FIXME: this does not handle quoted text that spans multiple lines!!!
        try {
            // Just take each line and put it in the list
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

    /**
     * Parses the first line to get the column names.
     * 
     * @param line the text line
     * @return the column names
     */
    private List<String> parseTitles(String line) {
        // Match using the parser
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

    /**
     * Parses a line, saving the tokens, and determines the type match.
     * 
     * @param line a new line
     */
    private void parseLine(String line) {
        // Match using the parser
        mLineTokens.reset(line);
        int nColumn = 0;
        while (mLineTokens.find()) {
            // Does this line have more columns than expected?
            if (nColumn == nColumns) {
                columnMismatch = true;
                return;
            }
            
            String token;
            if (mLineTokens.start(2) >= 0) {
                // The token was unquoted. Check if it could be a number.
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
        // Does this line have fewer columns than expected?
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
