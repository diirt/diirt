/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.diirt.graphene.profile.ProfileGraph2D;

/**
 * Handles finding a .CSV file.
 *
 * @author asbarber
 */
public class CSVFinder {

    /**
     * The file names of the tables supported by 1D table analysis
     * (not <code>MultiLevelProfiler</code> tables).
     *
     * @see #findTables1D()
     */
    public static String[] SUPPORTED_TABLES_1D = new String[]   {"Histogram1D.csv",
                                                                "IntensityGraph2D.csv",
                                                                "LineGraph2D.csv",
                                                                "ScatterGraph2D.csv",
                                                                "SparklineGraph2D.csv"
                                                                };

    /**
     * Prevents instantiation.
     */
    private CSVFinder() {}


    //Find Actions
    //--------------------------------------------------------------------------

    /**
     * Asks the user to use a file-browser to find a file.
     * @return the file selected by the user, null if no file
     */
    public static File browseCSV(){
        final JFileChooser fc = new JFileChooser(ProfileGraph2D.LOG_FILEPATH);
        fc.setFileFilter(new FileNameExtensionFilter("Graphene Tables (*.csv)", "csv"));

        //Opens Dialog
        int returnValue = fc.showOpenDialog(null);

        //Dialog Close
        if (returnValue == JFileChooser.APPROVE_OPTION){
            return fc.getSelectedFile();
        }
        else{
            return null;
        }
    }

    /**
     * Returns any file in the appropriate log file path that is supported
     * by the profile analyzer and thus able to be further analyzed.
     *
     * @return any file in the <code>ProfileGraph2D</code> specified log
     *         file path that is supported by the profile analyzer
     *
     * @see org.diirt.graphene.profile.ProfileGraph2D#LOG_FILEPATH
     * @see #SUPPORTED_TABLES_1D
     */
    public static List<File> findTables1D(){
        List<File> tables1D = new ArrayList<>();

        File root = new File(ProfileGraph2D.LOG_FILEPATH);

        //All files in the directory
        for (File subfile : root.listFiles()){
            for (int i = 0; i < SUPPORTED_TABLES_1D.length; i++){

                //If supported file name
                if (SUPPORTED_TABLES_1D[i].equals(subfile.getName())){
                    tables1D.add(subfile);
                    i = SUPPORTED_TABLES_1D.length;    //break
                }

            }
        }

        return tables1D;
    }

    //--------------------------------------------------------------------------


    //Validating CSV File
    //--------------------------------------------------------------------------

    /**
     * Ensures the csvFile is non-null and that the file exists.
     * @param csvFile file to check .CSV validness of
     */
    public static void validateCSV(File csvFile){
        if (csvFile == null){
            throw new IllegalArgumentException("The CSV file must not be null.");
        }

        if (!csvFile.exists()){
            throw new IllegalArgumentException("The CSV file must exist.");
        }
    }

    //--------------------------------------------------------------------------
}
