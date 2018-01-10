/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.diirt.graphene.profile.ProfileGraph2D;
import org.diirt.graphene.profile.io.CSVFinder;
import org.diirt.graphene.profile.io.CSVReader;
import org.diirt.graphene.profile.io.CSVWriter;
import org.diirt.graphene.profile.io.DateUtils;

/**
 * Provides the tools necessary for comparing the output tables of profiling
 * to determine changes in the results.
 * <p>
 * Provides options to analyze the two tables types:
 * <ul>
 *      <li><code>ProfileGraph2D</code> 1D tables</li>
 *      <li><code>MultiLevelProfiler</code> 2D tables</li>
 * </ul>
 * <p>
 * The <code>ProfileAnalysis</code> class is purely static and unable
 * to be instantiated.
 * <p>
 * <b>WARNING</b>: the analysis operations assume the preconditions
 * are satisfied with only minor checks.  Therefore it essential
 * that the appropriate file types are selected.
 *
 * @author asbarber
 */
public class ProfileAnalysis {

    /**
     * Private constructor; cannot be instantiated.
     */
    private ProfileAnalysis(){};

    /**
     * Percent level in which difference in values are considered statistically
     * significant and thus take appropriate (warning) action.
     */
    public static final double STATISTICALLY_SIGNIFICANT = 0.05;

    /**
     * Brings a dialog box to ask the user to select two table based
     * CSV files and compares the differences of the tables.
     * <p>
     * Computes the difference of each cell between the first file selected
     * and the second file (first - second).
     * <p>
     * Precondition: both files selected are <code>MultiLevelProfiler</code>
     * table files of the same graph type.
     */
    public static void compareTables2D(){
        String message = "Select two Graphene table files (*.csv) to compute"
                       + " the timing differences of.";
        String hint    = "Note that the differences are calculated by:"
                       + " first file cell minus second file cell.";

        JOptionPane.showMessageDialog(null, message);
        JOptionPane.showMessageDialog(null, hint);

        File fileA, fileB;

        fileA = CSVFinder.browseCSV();
        if (fileA == null){ return; }

        fileB = CSVFinder.browseCSV();
        if (fileB == null){ return; }

        compareTables2D(fileA, fileB);
    }

    /**
     * Computes the difference of each cell between the first file selected
     * and the second file (first - second).
     * <p>
     * Precondition: both files selected are <code>MultiLevelProfiler</code>
     * table files of the same graph type.
     *
     * @param fileA .CSV <code>MultiLevelProfiler</code> formatted file,
     *              considered initial file (compares fileA - fileB)
     * @param fileB .CSV <code>MultiLevelProfiler</code> formatted file,
     *              considered initial file (compares fileA - fileB)
     */
    public static void compareTables2D(File fileA, File fileB){
        CSVReader.validate2DTablesNames(fileA, fileB);

        List<List<String>> dataA = CSVReader.parseCSV(fileA);
        List<List<String>> dataB = CSVReader.parseCSV(fileB);

        CSVReader.validate2DTables(dataA, dataB);
        if (dataA.isEmpty() || dataB.isEmpty()){ return; }

        String badCell = "*Error in reading this cell*";
        List<List> output = new ArrayList<>();

        //Header
        List<String> outputHeader = new ArrayList<>();
        for (int c = 0; c < dataA.get(0).size(); ++c){
            //Entry in both headers are the same
            if (dataA.get(0).get(c).equals(dataB.get(0).get(c))){
                outputHeader.add(dataA.get(0).get(c));
            }
            //Different entry in dataA vs dataB
            else{
                outputHeader.add(badCell);
            }
        }
        output.add(outputHeader);

        //Rows
        for (int r = 1; r < dataA.size(); ++r){
            List<String> outputRow = new ArrayList<>();

            for (int c = 0; c < dataA.get(r).size(); ++c){
                try{
                    double numA = Double.parseDouble(dataA.get(r).get(c));
                    double numB = Double.parseDouble(dataB.get(r).get(c));
                    double diff = (numA - numB) / numA * 100;
                    outputRow.add(String.format("%.3f", diff + "%"));
                }
                catch (NumberFormatException ex){
                    outputRow.add(badCell);
                }
            }

            output.add(outputRow);
        }

        //Saving
        String[] compA = fileA.getName().split("-");
        String[] compB = fileB.getName().split("-");

        String date = DateUtils.getDate(DateUtils.DateFormat.NONDELIMITED),
               dateA = compA[0],
               dateB = compB[0],
               graphType = compA[1];


        File outputFile = CSVWriter.createNewFile(  ProfileGraph2D.LOG_FILEPATH +
                                                    date +
                                                    "-" +
                                                    graphType +
                                                    "-Table2D Difference-" +
                                                    dateA +
                                                    "vs" +
                                                    dateB +
                                                    ".csv");

        CSVWriter.writeData(outputFile, output);
    }

    /**
     * Computes the difference between the last two records
     * in each 1D table and analyzes whether the change was significant.
     * <p>
     * Conditions to Analyze:
     * <ul>
     *      <li>Supported files can be found</li>
     *      <li>The 1D tables are in the right .CSV format</li>
     *      <li>The file has two rows of data to compare</li>
     * </ul>
     *
     * @return a list of:
     *         for each 1D table analyzed,
     *         a generated analysis message
     *         including the graph profile and the
     *         the results of the analysis (significant increase/decrease)
     */
    public static List<String> analyzeTables1D(){
        List<File>              files = CSVFinder.findTables1D();
        List<String>            results = new ArrayList<>();

        //All files that are supported and found
        for (File tableFile: files){
            if (tableFile != null){
                List<List<String>> tableRows = CSVReader.parseCSV(tableFile);


                //Includes a header plus two rows of data
                if (tableRows.size() > 2){
                    List<String> previous = tableRows.get(tableRows.size() - 2);
                    List<String> recent = tableRows.get(tableRows.size() - 1);

                    //Graph Type
                    String graphType = previous.get(0);

                    //Average Time
                    double percentChange = percentChange(
                            Double.parseDouble(previous.get(2)),
                            Double.parseDouble(recent.get(2))
                    );

                    //Performance Change
                    String performance = performanceChange(
                            Double.parseDouble(previous.get(2)),
                            Double.parseDouble(recent.get(2))
                    );

                    //Analysis
                    results.add(graphType +
                                ": " +
                                performance +
                                ": " +
                                String.format("%.3f", percentChange) +
                                "% changed"
                   );
                }
            }
        }

        results.add("\n" + results.size() + " files analyzed.");
        return results;
    }

    /**
     * Calculates the percent change from initial to final.
     * @param valInit initial value
     * @param valFinal final value
     * @return percent change (as decimal)
     */
    public static double percentChange(double valInit, double valFinal){
        return (valFinal - valInit) / valInit * 100;
    }

    /**
     * Determines the significance of the change from the initial value to
     * the final value.
     * @param valInit initial value
     * @param valFinal final value
     * @return message about the change in performance
     */
    public static String performanceChange(double valInit, double valFinal){
        double percentChange = percentChange(valInit, valFinal);

        //Signficant performance decrease
        if (percentChange > ProfileAnalysis.STATISTICALLY_SIGNIFICANT*100){
            return "Performance Decrease";
        }
        //Significant performance increase
        else if (percentChange < ProfileAnalysis.STATISTICALLY_SIGNIFICANT*100){
            return "Performance Increase";
        }
        //No Significant performance change
        else{
            return "Performance Stable";
        }
    }

}
