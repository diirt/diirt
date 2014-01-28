/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.util.text;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test simulated pv function names parsing
 *
 * @author carcassi
 */
public class CSVParserTest {

    public CSVParserTest() {
    }

    @Test
    public void parseCSVLine1() {
        String line = "\"a\" 1 2.3 \"b\"";
        List<Object> tokens = CSVParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("a", 1.0, 2.3, "b")));
    }

    @Test
    public void parseCSVLine2() {
        String line = "\"This is a test\" \"Another test\" \"No spaces\" \"Between these two\"";
        List<Object> tokens = CSVParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("This is a test", "Another test", "No spaces", "Between these two")));
    }

    @Test
    public void parseCSVLine3() {
        String line = "\"And he asked:\"\"Does quoting works?\"\"\"";
        List<Object> tokens = CSVParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("And he asked:\"Does quoting works?\"")));
    }

    @Test
    public void parseCSVLine4() {
        String line = "1 2 3 4";
        List<Object> tokens = CSVParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList(1.0, 2.0, 3.0, 4.0)));
    }

    @Test
    public void parseCSVLine5() {
        String line = "\"Name\" \"Value\" \"Index\"\n";
        List<Object> tokens = CSVParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("Name", "Value", "Index")));
    }

    @Test
    public void parseCSVLine6() {
        String line = "\"A\" 0.234 1";
        List<Object> tokens = CSVParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("A", 0.234, 1.0)));
    }
    
    @Test
    public void csvTokens1() {
        String line = "1,3,HELLO,\"How are you?\"";
        List<String> tokens = CSVParser.csvTokens(line, ",");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens2() {
        String line = "1;3;HELLO;\"How are you?\"";
        List<String> tokens = CSVParser.csvTokens(line, ";");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens3() {
        String line = "1 3 HELLO \"How are you?\"";
        List<String> tokens = CSVParser.csvTokens(line, " ");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens4() {
        String line = "1\t3\tHELLO\t\"How are you?\"";
        List<String> tokens = CSVParser.csvTokens(line, "\t");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens5() {
        String line = "1,3,\"This\nis\nmultiline\",\"How are you?\"";
        List<String> tokens = CSVParser.csvTokens(line, ",");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "This\nis\nmultiline", "How are you?")));
    }
}