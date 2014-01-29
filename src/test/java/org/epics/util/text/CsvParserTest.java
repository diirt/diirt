/**
 * Copyright (C) 2012-14 epics-util developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.util.text;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import org.epics.util.array.ArrayDouble;
import org.epics.util.array.ListNumber;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Test simulated pv function names parsing
 *
 * @author carcassi
 */
public class CsvParserTest {

    public CsvParserTest() {
    }

    @Test
    public void parseCSVLine1() {
        String line = "\"a\" 1 2.3 \"b\"";
        List<Object> tokens = CsvParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("a", 1.0, 2.3, "b")));
    }

    @Test
    public void parseCSVLine2() {
        String line = "\"This is a test\" \"Another test\" \"No spaces\" \"Between these two\"";
        List<Object> tokens = CsvParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("This is a test", "Another test", "No spaces", "Between these two")));
    }

    @Test
    public void parseCSVLine3() {
        String line = "\"And he asked:\"\"Does quoting works?\"\"\"";
        List<Object> tokens = CsvParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("And he asked:\"Does quoting works?\"")));
    }

    @Test
    public void parseCSVLine4() {
        String line = "1 2 3 4";
        List<Object> tokens = CsvParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList(1.0, 2.0, 3.0, 4.0)));
    }

    @Test
    public void parseCSVLine5() {
        String line = "\"Name\" \"Value\" \"Index\"\n";
        List<Object> tokens = CsvParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("Name", "Value", "Index")));
    }

    @Test
    public void parseCSVLine6() {
        String line = "\"A\" 0.234 1";
        List<Object> tokens = CsvParser.parseCSVLine(line, " ");
        assertThat(tokens, equalTo(Arrays.<Object>asList("A", 0.234, 1.0)));
    }
    
    @Test
    public void csvTokens1() {
        String line = "1,3,HELLO,\"How are you?\"";
        List<String> tokens = CsvParser.csvTokens(line, ",");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens2() {
        String line = "1;3;HELLO;\"How are you?\"";
        List<String> tokens = CsvParser.csvTokens(line, ";");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens3() {
        String line = "1 3 HELLO \"How are you?\"";
        List<String> tokens = CsvParser.csvTokens(line, " ");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens4() {
        String line = "1\t3\tHELLO\t\"How are you?\"";
        List<String> tokens = CsvParser.csvTokens(line, "\t");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "HELLO", "How are you?")));
    }
    
    @Test
    public void csvTokens5() {
        String line = "1,3,\"This\nis\nmultiline\",\"How are you?\"";
        List<String> tokens = CsvParser.csvTokens(line, ",");
        assertThat(tokens, equalTo(Arrays.asList("1", "3", "This\nis\nmultiline", "How are you?")));
    }
    
    @Test
    public void parseTable1CSV() throws Exception {
        CsvParserResult result = CsvParser.AUTOMATIC.parse(new InputStreamReader(getClass().getResource("table1.csv").openStream()));
        assertThat(result.getColumnNames().size(), equalTo(3));
        assertThat(result.getColumnNames().get(0), equalTo("Name"));
        assertThat(result.getColumnNames().get(1), equalTo("Value"));
        assertThat(result.getColumnNames().get(2), equalTo("Index"));
        assertThat((Object) result.getColumnTypes().get(0), equalTo((Object) String.class));
        assertThat((Object) result.getColumnTypes().get(1), equalTo((Object) double.class));
        assertThat((Object) result.getColumnTypes().get(2), equalTo((Object) double.class));
        assertThat(result.getColumnValues().get(0), equalTo((Object) Arrays.asList("A", "B", "C", "D", "E")));
        assertThat(result.getColumnValues().get(1), equalTo((Object) new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123)));
        assertThat(result.getColumnValues().get(2), equalTo((Object) new ArrayDouble(1,2,3,4,5)));
    }

    @Test
    public void parseFileTable2CSV() throws Exception {
        CsvParserResult result = CsvParser.AUTOMATIC.parse(new InputStreamReader(getClass().getResource("table2.csv").openStream()));
        assertThat(result.getColumnNames().size(), equalTo(3));
        assertThat(result.getColumnNames().get(0), equalTo("Name"));
        assertThat(result.getColumnNames().get(1), equalTo("Value"));
        assertThat(result.getColumnNames().get(2), equalTo("Index"));
        assertThat((Object) result.getColumnTypes().get(0), equalTo((Object) String.class));
        assertThat((Object) result.getColumnTypes().get(1), equalTo((Object) double.class));
        assertThat((Object) result.getColumnTypes().get(2), equalTo((Object) double.class));
        assertThat(result.getColumnValues().get(0), equalTo((Object) Arrays.asList("A", "B", "C", "D", "E")));
        assertThat(result.getColumnValues().get(1), equalTo((Object) new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123)));
        assertThat(result.getColumnValues().get(2), equalTo((Object) new ArrayDouble(1,2,3,4,5)));
    }

    @Test
    public void parseFileTable4CSV() throws Exception {
        CsvParserResult result = CsvParser.AUTOMATIC.parse(new InputStreamReader(getClass().getResource("table4.csv").openStream()));
        assertThat(result.getColumnNames().size(), equalTo(13));
        assertThat(result.getColumnNames().get(0), equalTo("timestamp"));
        assertThat(result.getColumnNames().get(1), equalTo("rta_MIN"));
        assertThat(result.getColumnNames().get(2), equalTo("rta_MAX"));
        assertThat((Object) result.getColumnTypes().get(0), equalTo((Object) double.class));
        assertThat((Object) result.getColumnTypes().get(1), equalTo((Object) double.class));
        assertThat((Object) result.getColumnTypes().get(2), equalTo((Object) double.class));
        assertThat(((ListNumber) result.getColumnValues().get(0)).getDouble(0), equalTo(1390913220.0));
        assertThat(((ListNumber) result.getColumnValues().get(1)).getDouble(1), equalTo(0.28083333333));
        assertThat(((ListNumber) result.getColumnValues().get(2)).getDouble(2), equalTo(0.266825));
    }

    @Test
    public void parseFileTable5CSV() throws Exception {
        CsvParserResult result = CsvParser.AUTOMATIC.parse(new InputStreamReader(getClass().getResource("table5.csv").openStream()));
        assertThat(result.getColumnNames().size(), equalTo(3));
        assertThat(result.getColumnNames().get(0), equalTo("A"));
        assertThat(result.getColumnNames().get(1), equalTo("B"));
        assertThat(result.getColumnNames().get(2), equalTo("C"));
        assertThat((Object) result.getColumnTypes().get(0), equalTo((Object) String.class));
        assertThat((Object) result.getColumnTypes().get(1), equalTo((Object) double.class));
        assertThat((Object) result.getColumnTypes().get(2), equalTo((Object) double.class));
        assertThat(result.getColumnValues().get(0), equalTo((Object) Arrays.asList("A", "B", "C", "D", "E")));
        assertThat(result.getColumnValues().get(1), equalTo((Object) new ArrayDouble(0.234, 1.456, 234567891234.0, 0.000000123, 123)));
        assertThat(result.getColumnValues().get(2), equalTo((Object) new ArrayDouble(1,2,3,4,5)));
    }

    @Test
    public void parseFileTable6CSV() throws Exception {
        CsvParserResult result = CsvParser.AUTOMATIC.parse(new InputStreamReader(getClass().getResource("table6.csv").openStream()));
        assertThat(result.getColumnNames().size(), equalTo(3));
        assertThat(result.getColumnNames().get(0), equalTo("Name"));
        assertThat(result.getColumnNames().get(1), equalTo("Surname"));
        assertThat(result.getColumnNames().get(2), equalTo("Address"));
        assertThat((Object) result.getColumnTypes().get(0), equalTo((Object) String.class));
        assertThat((Object) result.getColumnTypes().get(1), equalTo((Object) String.class));
        assertThat((Object) result.getColumnTypes().get(2), equalTo((Object) String.class));
        assertThat(result.getColumnValues().get(0), equalTo((Object) Arrays.asList("Gabriele", "Kunal")));
        assertThat(result.getColumnValues().get(1), equalTo((Object) Arrays.asList("Carcassi", "Shroff")));
        assertThat(result.getColumnValues().get(2), equalTo((Object) Arrays.asList("Happytown", "Politeville")));
    }
}