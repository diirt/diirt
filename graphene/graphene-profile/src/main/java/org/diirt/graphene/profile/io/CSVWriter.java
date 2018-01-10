/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles writing data to a .CSV file.
 *
 * @author asbarber
 */
public final class CSVWriter {

    /**
     * Quote delimiter for a .CSV formatted output file.
     */
    public static final String QUOTE = "\"";

    /**
     * Comma delimiter for a .CSV formatted output file.
     */
    public static final String DELIM = ",";

    /**
     * Prevents instantiation.
     */
    private CSVWriter(){}


    //File Creation
    //--------------------------------------------------------------------------

    /**
     * Creates a CSV file with the specified name.
     * <b>Will overwrite an existing file!</b>
     *
     * @param filename path and name of file
     * @return created CSV file
     */
    public static File createNewFile(String filename){
        try {
            File outputFile = new File(filename + ".csv");

            //Creates File
            outputFile.createNewFile();

            return outputFile;

        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Creates a CSV file with the specified name.
     * <b>Will <i>not</i> overwrite an existing file!</b>
     * Instead a unique name will be found by appending .# to the original name.
     *
     * @param filename path and name of file
     * @return created CSV file
     */
    public static File createFile(String filename){
        try {
            File outputFile = new File(filename + ".csv");

            //Prevent File Overwrite
            int tmp = 1;
            while (outputFile.exists()){
                outputFile = new File(filename + ".csv" + "." + tmp);
                tmp++;
            }

            //Creates File
            outputFile.createNewFile();

            return outputFile;

        } catch (IOException ex) {
            Logger.getLogger(CSVWriter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //--------------------------------------------------------------------------


    //Data Output
    //--------------------------------------------------------------------------

    /**
     * Writes each row as a set of entries in the .CSV file and
     * outputs a new line between each row.
     * Essentially, each row contains items that represent the column
     * entries within that row.
     * @param csvFile file to write to
     * @param rows cells to write
     */
    public static void writeData(File csvFile, List<List> rows){
        //Invalid File
        if (csvFile == null){
            throw new IllegalArgumentException("Cannot write to a null file.");
        }

        //Invalid data
        if (rows == null){
            throw new IllegalArgumentException("Must have non-null data to write.");
        }

        //Writes all rows
        for (List<Object> row: rows){
            writeRow(csvFile, row);
        }
    }

    /**
     * Writes each item in the list as a separate entry in the .CSV file
     * and outputs a new line in the file.
     * @param csvFile file to write to
     * @param row entries to write
     */
    public static void writeRow(File csvFile, List row){
        //Invalid File
        if (csvFile == null){
            throw new IllegalArgumentException("Cannot write to a null file.");
        }

        //Invalid data
        if (row == null){
            throw new IllegalArgumentException("Must have non-null data to write.");
        }

        try {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(csvFile, true)))) {

                //Write Entries
                for (Object entry: row){
                    if (entry != null){
                        out.print(formatEntry(entry));
                    }
                }

                //Clean-up
                out.println();
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Output errors exist.");
        }
    }

    /**
     * Writes the data as a string row in the .CSV file.
     * @param csvFile file to write to
     * @param data data to write
     */
    public static void writeRow(File csvFile, Object data){
        List rows = new ArrayList<>();
        rows.add(data);
        writeRow(csvFile, rows);
    }

    //--------------------------------------------------------------------------


    //Helper
    //--------------------------------------------------------------------------

    /**
     * Formats the object by surrounding it in the delimiting values
     * and converts the object to a string using it's <code>toString</code>
     * property.
     * @param entry data value to format
     * @return formatted entry,
     *         surrounded by quotes and ending with delimiter (ie - comma)
     */
    private static String formatEntry(Object entry){
        return QUOTE + entry.toString() + QUOTE + DELIM;
    }

    /**
     * Combines all items as a list.  The primary use is if an item itself
     * is a list, all the elements of the item is added to the complete list.
     * That is, all sub-items of an item are added to the overall list.
     *
     * @param items object elements to combine
     * @return all items and sub-items combined into one collection
     */
    public static List arrayCombine(Object... items){
        if (items == null){
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        List l = new ArrayList();

        for (Object item: items){
            if (item != null){
                if (item instanceof Object[]){
                    Object[] entries = (Object[]) item;
                    for (int i = 0; i < entries.length; i++){
                        l.add(entries[i]);
                    }
                }
                else{
                    l.add(item);
                }
            }
        }
        return l;
    }

    //--------------------------------------------------------------------------

}
