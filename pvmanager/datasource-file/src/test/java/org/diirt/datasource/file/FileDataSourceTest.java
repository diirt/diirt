/**
 * Copyright (C) 2010-14 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Duration;

import org.diirt.datasource.test.CountDownPVReaderListener;

import java.util.Arrays;

import org.diirt.datasource.DataSource;
import org.diirt.datasource.PV;
import org.diirt.datasource.PVManager;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVReaderEvent;

import static org.junit.Assert.*;

import org.junit.*;

import static org.diirt.datasource.vtype.ExpressionLanguage.*;

import org.diirt.util.array.ArrayDouble;

import org.diirt.vtype.VStringArray;
import org.diirt.vtype.VTable;
import org.diirt.vtype.VType;
import org.diirt.vtype.ValueFactory;

import static org.hamcrest.Matchers.*;
import static java.time.Duration.*;
/**
 *
 * @author carcassi
 */
public class FileDataSourceTest {

    public FileDataSourceTest() {
    }

    private static DataSource file;

    @BeforeClass
    public static void createDataSource() {
        file = new FileDataSourceConfiguration().pollEnabled(true).pollInterval(Duration.ofMillis(250)).create();
    }

    @AfterClass
    public static void destroyDataSource() {
        file.close();
        file = null;
    }

    @Before
    public void setUp() {
        pv = null;
    }

    @After
    public void tearDown() {
        if (pv != null) {
            pv.close();
            pv = null;
        }
    }

    private volatile PVReader<?> pv;

    // TODO: Add a test for the image

    @Test
    public void readFile() throws Exception {
        CountDownPVReaderListener listener = new CountDownPVReaderListener(1, PVReaderEvent.VALUE_MASK);
        pv = PVManager.read(vType(getClass().getResource("data1.csv").getPath())).from(file)
                .readListener(listener)
                .maxRate(ofMillis(10));

        // Wait for value
        listener.await(ofMillis(700));
        assertThat(listener.getCount(), equalTo(0));

        assertThat(pv.getValue(), instanceOf(VTable.class));
        VTable vTable = (VTable) pv.getValue();
        assertThat(vTable.getRowCount(), equalTo(3));
        assertThat(vTable.getColumnCount(), equalTo(2));
        assertThat(vTable.getColumnName(0), equalTo("Name"));
        assertThat(vTable.getColumnName(1), equalTo("Value"));
        assertThat(vTable.getColumnData(0), equalTo((Object) Arrays.asList("A", "B", "C")));
//        listener.resetCount(1);
//
//        // No new values, should get no new notification
//        listener.await(ofMillis(500));
//        assertThat(listener.getCount(), equalTo(1));
//
//        // Add one value, notification should not come right away
//        queue.add(1);
//        listener.await(ofMillis(100));
//        assertThat(listener.getCount(), equalTo(1));
//
//        // Add a few other values, still no new notification
//        queue.add(2);
//        queue.add(3);
//        listener.await(ofMillis(100));
//        assertThat(listener.getCount(), equalTo(1));
//        queue.add(4);
//
//        // Wait longer for first notification
//        listener.await(ofMillis(500));
//        assertThat(listener.getCount(), equalTo(0));
//        assertThat(pv.getValue(), equalTo((Object) Arrays.asList(1)));
//        listener.resetCount(1);
//
//        // Wait for second notification
//        listener.await(ofMillis(700));
//        assertThat(listener.getCount(), equalTo(0));
//        assertThat(pv.getValue(), equalTo((Object) Arrays.asList(2,3,4)));
    }

    @Test
    public void udpateFile() throws Exception {
        CountDownPVReaderListener listener = new CountDownPVReaderListener(1, PVReaderEvent.VALUE_MASK);
        File filename = File.createTempFile("file.", ".csv");
        PrintWriter writer = new PrintWriter(filename);
        writer.println("Name,Value");
        writer.println("Andrew,34");
        writer.println("Bob,12");
        writer.close();

        pv = PVManager.read(vType(filename.toURI().getPath())).from(file)
                .readListener(listener)
                .maxRate(ofMillis(10));

        // Wait for value
        listener.await(ofMillis(1500));
        assertThat(listener.getCount(), equalTo(0));

        assertThat(pv.getValue(), instanceOf(VTable.class));
        VTable vTable = (VTable) pv.getValue();
        assertThat(vTable.getRowCount(), equalTo(2));
        assertThat(vTable.getColumnCount(), equalTo(2));
        assertThat(vTable.getColumnName(0), equalTo("Name"));
        assertThat(vTable.getColumnName(1), equalTo("Value"));
        assertThat(vTable.getColumnData(0), equalTo((Object) Arrays.asList("Andrew", "Bob")));
        assertThat(vTable.getColumnData(1), equalTo((Object) new ArrayDouble(34,12)));

        listener.resetCount(1);

        writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        writer.println("Charlie,71");
        writer.close();

        // Wait for value
        listener.await(ofMillis(3000));
        assertThat(listener.getCount(), equalTo(0));

        assertThat(pv.getValue(), instanceOf(VTable.class));
        vTable = (VTable) pv.getValue();
        assertThat(vTable.getRowCount(), equalTo(3));
        assertThat(vTable.getColumnCount(), equalTo(2));
        assertThat(vTable.getColumnName(0), equalTo("Name"));
        assertThat(vTable.getColumnName(1), equalTo("Value"));
        assertThat(vTable.getColumnData(0), equalTo((Object) Arrays.asList("Andrew", "Bob", "Charlie")));
        assertThat(vTable.getColumnData(1), equalTo((Object) new ArrayDouble(34,12,71)));
    }

    @Test
    public void writeToList1() throws Exception {
        CountDownPVReaderListener listener = new CountDownPVReaderListener(1, PVReaderEvent.VALUE_MASK);
        File filename = File.createTempFile("file.", ".list");
        PrintWriter writer = new PrintWriter(filename);
        writer.println("A");
        writer.println("B");
        writer.println("C");
        writer.close();

        PV<VType, Object> fullPv = PVManager.readAndWrite(vType(filename.toURI().getPath())).from(file)
                .readListener(listener)
                .synchWriteAndMaxReadRate(ofMillis(10));
        pv = fullPv;

        // Wait for value
        listener.await(ofMillis(700));
        assertThat(listener.getCount(), equalTo(0));

        assertThat(pv.getValue(), instanceOf(VStringArray.class));
        VStringArray array = (VStringArray) pv.getValue();
        assertThat(array.getData(), equalTo(Arrays.asList("A", "B", "C")));

        listener.resetCount(1);
        fullPv.write(ValueFactory.toVType(Arrays.asList("A", "B", "C", "D")));

        listener.await(ofMillis(2000));
        assertThat(listener.getCount(), equalTo(0));

        assertThat(pv.getValue(), instanceOf(VStringArray.class));
        array = (VStringArray) pv.getValue();
        assertThat(array.getData(), equalTo(Arrays.asList("A", "B", "C", "D")));

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            assertThat(line, equalTo("A"));
            line = reader.readLine();
            assertThat(line, equalTo("B"));
            line = reader.readLine();
            assertThat(line, equalTo("C"));
            line = reader.readLine();
            assertThat(line, equalTo("D"));
            line = reader.readLine();
            assertThat(line, nullValue());
        }
    }

}