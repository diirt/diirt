/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.graphene.profile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.diirt.graphene.profile.impl.ProfileAreaGraph2D;
import org.diirt.graphene.profile.impl.ProfileBubbleGraph2D;
import org.diirt.graphene.profile.impl.ProfileHistogram1D;
import org.diirt.graphene.profile.impl.ProfileIntensityGraph2D;
import org.diirt.graphene.profile.impl.ProfileLineGraph2D;
import org.diirt.graphene.profile.impl.ProfileMultiYAxisGraph2D;
import org.diirt.graphene.profile.impl.ProfileMultilineGraph2D;
import org.diirt.graphene.profile.impl.ProfileNLineGraphs2D;
import org.diirt.graphene.profile.impl.ProfileScatterGraph2D;
import org.diirt.graphene.profile.impl.ProfileSparklineGraph2D;
import org.diirt.graphene.profile.io.DateUtils;
import org.diirt.graphene.profile.io.DateUtils.DateFormat;
import org.diirt.graphene.profile.utils.DatasetFactory;
import org.diirt.graphene.profile.utils.ProfileAnalysis;
import org.diirt.graphene.profile.utils.Resolution;
import org.diirt.graphene.profile.utils.Statistics;
import org.diirt.graphene.profile.utils.StopWatch;
import org.diirt.graphene.profile.utils.StopWatch.TimeType;

/**
 * Displays options and actions for profiling of the Graphene project
 * in a graphical-user-interface.
 *
 * @author asbarber
 */
public class VisualProfiler extends JPanel{

    //Data
    //-------------------------------------------------------------------------
    private SwingWorker             thread;
    private List<JButton>           actionButtons;
    private ActionModel             model;
    private UserSettings            userSettings;
    //-------------------------------------------------------------------------


    //Panel Data Members
    //-------------------------------------------------------------------------
    private JTabbedPane     tabs;
    private SettingsPanel   settingsPanel;
    private Profile1DTable  profile1DTable;
    private Profile2DTable  profile2DTable;
    private FileViewer      fileViewer;
    private AnalyzePanel    analyzePanel;
    private Console         console;
    //-------------------------------------------------------------------------


    //Panel Structures
    //-------------------------------------------------------------------------
    private class SettingsPanel extends JPanel{
        public  JComboBox           listRendererTypes;
        private JLabel              lblRendererTypes;

        private JTextField          txtTestTime;
        private JLabel              lblTestTime;

        private JTextField          txtMaxAttempts;
        private JLabel              lblMaxAttempts;

        private JComboBox           listTimeTypes;
        private JLabel              lblTimeTypes;

        private JLabel              lblSaveImage;
        private JCheckBox           chkSaveImage;

        private JList               listUpdateTypes;
        private JLabel              lblUpdateTypes;

        private JLabel              lblSaveMessage;
        private JTextField          txtSaveMessage;

        private JLabel              lblAuthorMessage;
        private JTextField          txtAuthorMessage;


        public SettingsPanel(){
            this.initComponents();
            this.addComponents();
        }

        private void initComponents(){
            listRendererTypes   = new JComboBox(SUPPORTED_PROFILERS);
            lblRendererTypes    = new JLabel("Renderer Type: ");

            txtTestTime         = new JTextField("20");
            lblTestTime         = new JLabel("Test Time: ");

            txtMaxAttempts      = new JTextField("1000000");
            lblMaxAttempts      = new JLabel("Max Attempts: ");

            listTimeTypes       = new JComboBox(StopWatch.TimeType.values());
            lblTimeTypes        = new JLabel("Timing Based Off: ");

            lblSaveImage        = new JLabel("Save Images: ");
            chkSaveImage        = new JCheckBox("Save Image");

            listUpdateTypes     = new JList();
            lblUpdateTypes      = new JLabel("Apply Update: ");

            lblSaveMessage      = new JLabel("Save Message: ");
            txtSaveMessage      = new JTextField("");

            lblAuthorMessage    = new JLabel("Author: ");
            txtAuthorMessage    = new JTextField("");
        }

        private void addComponents(){
            JPanel right = new JPanel();
                right.setLayout(new BorderLayout());
                right.add(this.lblUpdateTypes, BorderLayout.NORTH);
                right.add(new JScrollPane(this.listUpdateTypes), BorderLayout.CENTER);

            JPanel left = new JPanel();
                left.setLayout(new GridLayout(0, 2));

                left.add(this.lblRendererTypes);
                left.add(this.listRendererTypes);

                left.add(this.lblTestTime);
                left.add(this.txtTestTime);

                left.add(this.lblMaxAttempts);
                left.add(this.txtMaxAttempts);

                left.add(this.lblTimeTypes);
                left.add(this.listTimeTypes);

                left.add(this.lblSaveImage);
                left.add(this.chkSaveImage);

                left.add(lblSaveMessage);
                left.add(txtSaveMessage);

                left.add(lblAuthorMessage);
                left.add(txtAuthorMessage);

            this.setLayout(new GridLayout(0,2));
            this.add(left);
            this.add(right);
        }
    }
    private class Profile1DTable extends JPanel{
        private JLabel              lblDatasetSize;
        private JTextField          txtDatasetSize;

        private JLabel              lblImageWidth;
        private JTextField          txtImageWidth;

        private JLabel              lblImageHeight;
        private JTextField          txtImageHeight;

        private JLabel              lblShowGraph;
        private JCheckBox           chkShowGraph;

        public  JButton             btnProfile1D;
        public  JButton             btnProfile1DAll;

        public Profile1DTable(){
            this.initComponents();
            this.initMnemonics();
            this.addComponents();
        }

        private void initComponents(){
            lblDatasetSize      = new JLabel("Number of Data Points: ");
            lblDatasetSize      .setToolTipText("Format for IntensityGraph2D: 1000x1000");
            txtDatasetSize      = new JTextField("10000");

            lblImageWidth       = new JLabel("Image Width: ");
            txtImageWidth       = new JTextField("640");

            lblImageHeight      = new JLabel("Image Height: ");
            txtImageHeight      = new JTextField("480");

            lblShowGraph        = new JLabel("Graph Results: ");
            chkShowGraph        = new JCheckBox("Show Graph");

            btnProfile1D        = new JButton("Profile");
            actionButtons       .add(btnProfile1D);
            btnProfile1DAll     = new JButton("Profile For All Renderers");
            actionButtons       .add(btnProfile1DAll);
        }

        private void initMnemonics(){
            this.btnProfile1D.setMnemonic('P');
            this.btnProfile1DAll.setMnemonic('A');
        }

        private void addComponents(){
            this.setLayout(new GridLayout(0, 2));

            this.add(lblDatasetSize);
            this.add(txtDatasetSize);

            this.add(lblImageWidth);
            this.add(txtImageWidth);

            this.add(lblImageHeight);
            this.add(txtImageHeight);

            this.add(lblShowGraph);
            this.add(chkShowGraph);

            this.add(blankPanel(btnProfile1D));
            this.add(blankPanel(btnProfile1DAll));
        }

        /**
         * Gets the input from the user interface on whether graphs of statistics
         * are shown after profiling.
         * @return true if graphs of profile statistics are shown after profiling,
         *         false if graphs of profile statistics are not shown
         */
        public boolean getShowGraph(){
            return profile1DTable.chkShowGraph.isSelected();
        }
    }
    private class Profile2DTable extends JSplitPane{
        private JLabel                          lblResolutions,
                                                lblNPoints;

        public  JButton                         btnProfile2D;

        private JList<Resolution>               listResolutions;
        private JList<Integer>                  listNPoints;
        private DefaultListModel<Resolution>    modelResolutions;
        private DefaultListModel<Integer>       modelNPoints;

        public Profile2DTable(){
            this.initComponents();
            this.initMnemonics();
            this.addComponents();
            this.loadLists();
        }

        private void initComponents(){
            lblResolutions      = new JLabel("Resolutions");
            lblNPoints          = new JLabel("N Points");
            btnProfile2D        = new JButton("Start");
            actionButtons       .add(btnProfile2D);

            listResolutions     = new JList<>();
            listNPoints         = new JList<>();
        }

        private void initMnemonics(){
            this.btnProfile2D.setMnemonic('S');
        }

        private void addComponents(){
                JPanel multiLayerLeft = new JPanel();
                multiLayerLeft.setLayout(new BorderLayout());
                multiLayerLeft.add(lblResolutions, BorderLayout.NORTH);
                multiLayerLeft.add(new JScrollPane(listResolutions), BorderLayout.CENTER);

                JPanel multiLayerMiddle = new JPanel();
                multiLayerMiddle.setLayout(new BorderLayout());
                multiLayerMiddle.add(lblNPoints, BorderLayout.NORTH);
                multiLayerMiddle.add(new JScrollPane(listNPoints), BorderLayout.CENTER);

                JPanel multiLayerRight = new JPanel();
                multiLayerRight.setLayout(new BorderLayout());
                multiLayerRight.add(blankPanel(btnProfile2D), BorderLayout.NORTH);

            final JSplitPane multiLayerInner = new JSplitPane();

            multiLayerInner.setLeftComponent(multiLayerLeft);
            multiLayerInner.setRightComponent(multiLayerMiddle);

            this.setLeftComponent(multiLayerInner);
            this.setRightComponent(multiLayerRight);

            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    Profile2DTable.this.setDividerLocation(0.8);
                    multiLayerInner.setDividerLocation(0.8);
                }

            });
        }

        /**
         * Loads the lists for <code>Resolution</code>s and the
         * dataset sizes for the <code>MultiLevelProfiler</code>.
         */
        private void loadLists(){
            modelResolutions = new DefaultListModel<>();
            modelNPoints = new DefaultListModel<>();

            for (Resolution resolution : Resolution.defaultResolutions()){
                modelResolutions.addElement(resolution);
            }

            for (Integer datasetSize : DatasetFactory.defaultDatasetSizes()){
                modelNPoints.addElement(datasetSize);
            }

            listResolutions.setModel(modelResolutions);
            listNPoints.setModel(modelNPoints);
        }
    }
    private class AnalyzePanel extends JPanel{
        public  JButton             btnCompare2DTables;
        public  JButton             btnAnalyze1DTable;

        public AnalyzePanel(){
            this.initComponents();
            this.addComponents();
        }

        private void initComponents(){
            btnCompare2DTables  = new JButton("Compare Profile Tables");
            actionButtons       .add(btnCompare2DTables);
            btnAnalyze1DTable   = new JButton("Analyze Single Profile Tables");
            actionButtons       .add(btnAnalyze1DTable);
        }

        private void addComponents(){
            this.add(this.btnCompare2DTables);
            this.add(this.btnAnalyze1DTable);
        }
    }
    private class FileViewer extends JSplitPane{
        private JTree                   tree;
        private DefaultTreeModel        treeModel;
        private DefaultMutableTreeNode  treeRoot;
        public  JButton                 btnOpenFiles;
        public  JButton                 btnDeleteFiles;
        public  JButton                 btnReloadFiles;

        public FileViewer(){
            this.initComponents();
            this.initMenmonics();
            this.addComponents();
        }

        private void initComponents(){
            treeRoot            = new DefaultMutableTreeNode(new File(ProfileGraph2D.LOG_FILEPATH));
            treeModel           = new DefaultTreeModel(treeRoot);
            tree                = new JTree(treeRoot){
                @Override
                public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus){
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                    try{
                        File f = (File) node.getUserObject();
                        return f.getName();
                    }
                    catch(Exception e){
                        return node.toString();
                    }
                }
            };
            tree                .setModel(treeModel);
            tree                .expandRow(0);
            tree                .setShowsRootHandles(true);

            btnOpenFiles        = new JButton("Open File(s)");
            actionButtons       .add(btnOpenFiles);
            btnDeleteFiles      = new JButton("Delete File(s)");
            actionButtons       .add(btnDeleteFiles);
            btnReloadFiles      = new JButton("Refresh");
            actionButtons       .add(btnReloadFiles);
        }

        private void initMenmonics(){
            this.btnOpenFiles.setMnemonic('O');
            this.btnDeleteFiles.setMnemonic('D');
            this.btnReloadFiles.setMnemonic('R');
        }

        private void addComponents(){
            JPanel fileTabRight = new JPanel();
            fileTabRight.setLayout(new BoxLayout(fileTabRight, BoxLayout.Y_AXIS));
            fileTabRight.add(this.btnOpenFiles);
            fileTabRight.add(this.btnDeleteFiles);
            fileTabRight.add(this.btnReloadFiles);

            this.setLeftComponent(new JScrollPane(tree));
            this.setRightComponent(fileTabRight);
        }

        public void reloadNodes(){
            //Resets
            this.treeRoot.removeAllChildren();

            //Re-adds
            model.addNodes(treeRoot);

            //Updates GUI
            this.treeModel.nodeStructureChanged(this.treeRoot);
            this.repaint();
        }
    }
    private class Console extends JPanel{
        private JTextArea          txtConsole;
        private JLabel             lblConsole;
        public  JButton            btnClearLog;
        public  JButton            btnSaveLog;

        private JLabel             lblTime;
        private JTextField         txtTime;

        public  JButton            btnCancelThread;

        public Console(){
            this.initComponents();
            this.initMnemonics();
            this.addComponents();
        }

        private void initComponents(){
            txtConsole = new JTextArea(20, 50);
            txtConsole.setEditable(false);
            lblConsole = new JLabel("Console");
            btnSaveLog = new JButton("Save Log");
            actionButtons.add(btnSaveLog);
            btnClearLog = new JButton("Clear Log");
            actionButtons.add(btnClearLog);
            lblTime = new JLabel("Timer:");
            txtTime = new JTextField("00:00:00");
            txtTime.setEditable(false);
            btnCancelThread = new JButton("Cancel");
            btnCancelThread.setEnabled(false);
            actionButtons.add(btnCancelThread);
        }

        private void initMnemonics(){
            this.btnSaveLog.setMnemonic('L');
            this.btnClearLog.setMnemonic('C');
            this.btnCancelThread.setMnemonic('T');
        }

        private void addComponents(){
            this.setLayout(new BorderLayout());
            this.setBorder(BorderFactory.createLineBorder(Color.black));

                JPanel consoleBottom = new JPanel();
                consoleBottom.setLayout(new GridLayout(3, 2));
                    consoleBottom.add(blankPanel(this.btnSaveLog));
                    consoleBottom.add(blankPanel(this.btnClearLog));

                    consoleBottom.add(blankPanel(this.lblTime));
                    consoleBottom.add(blankPanel(this.txtTime));

                    consoleBottom.add(blankPanel(this.btnCancelThread));

            this.add(lblConsole, BorderLayout.NORTH);
            this.add(new JScrollPane(txtConsole), BorderLayout.CENTER);
            this.add(consoleBottom, BorderLayout.SOUTH);
        }

        public void print(List<String> chunks){
            for (String chunk: chunks){
                txtConsole.append(chunk);
            }
        }
    }
    //-------------------------------------------------------------------------


    //Values, Constants
    //-------------------------------------------------------------------------
    /**
     * Package location for <code>ProfileGraph2D</code> subclasses.
     */
    public static final String PROFILE_PATH = "org.diirt.graphene.profile.impl";

    /**
     * Java class names of all <code>ProfileGraph2D</code> subclasses.
     */
    public static final String[] SUPPORTED_PROFILERS = {"AreaGraph2D",
                                                        "BubbleGraph2D",
                                                        "Histogram1D",
                                                        "IntensityGraph2D",
                                                        "LineGraph2D",
                                                        "MultiYAxisGraph2D",
                                                        "MultilineGraph2D",
                                                        "NLineGraphs2D",
                                                        "ScatterGraph2D",
                                                        "SparklineGraph2D"
                                                       };

    /**
     * File names of files that cannot be deleted (protected) by the
     * <code>VisualProfiler</code>'s <code>FileViewer</code>.
     */
    public static final String[] PROTECTED_FILES = {"ProfileResults",
                                                    "1D Table Output",
                                                    "2D Table Output",
                                                    "Tests",
                                                    "README.txt"
                                                   };

    /**
     * Title of the auto-generated frame.
     */
    public static final String FRAME_TITLE = "Visual Profiler";

    /**
     * Makes a Profile object based off of the class name.
     * @param strClass name of Profile object to generate
     *
     * @return a <code>ProfileGraph2D</code> object corresponding to the
     * class name, returns null if no matching name
     */
    public static ProfileGraph2D factory(String strClass){
        switch (strClass) {
            case "AreaGraph2D":
                return new ProfileAreaGraph2D();
            case "BubbleGraph2D":
                return new ProfileBubbleGraph2D();
            case "Histogram1D":
                return new ProfileHistogram1D();
            case "IntensityGraph2D":
                return new ProfileIntensityGraph2D();
            case "LineGraph2D":
                return new ProfileLineGraph2D();
            case "MultiYAxisGraph2D":
                return new ProfileMultiYAxisGraph2D();
            case "MultilineGraph2D":
                return new ProfileMultilineGraph2D();
            case "NLineGraphs2D":
                return new ProfileNLineGraphs2D();
            case "ScatterGraph2D":
                return new ProfileScatterGraph2D();
            case "SparklineGraph2D":
                return new ProfileSparklineGraph2D();
        }

        return null;
    }
    //-------------------------------------------------------------------------


    //Action Structure
    //-------------------------------------------------------------------------
    private class UserSettings{

        public Integer getWidth(){
            String strImageWidth = profile1DTable.txtImageWidth.getText();
            int width;

            try{
                width = Integer.parseInt(strImageWidth);

                if (width <= 0){
                    throw new NumberFormatException();
                }

                return width;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the height.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        public Integer getHeight(){
            String strImageHeight = profile1DTable.txtImageHeight.getText();
            int height;

            try{
                height = Integer.parseInt(strImageHeight);

                if (height <= 0){
                    throw new NumberFormatException();
                }

                return height;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the height.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        public boolean getSaveImage(){
            return settingsPanel.chkSaveImage.isSelected();
        }

        public Integer getTestTime(){
            String strTestTime = settingsPanel.txtTestTime.getText();

            Integer testTime;

            try{
                testTime = Integer.parseInt(strTestTime);

                if (testTime <= 0){
                    throw new NumberFormatException();
                }

                return testTime;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for test time.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        public Integer getMaxTries(){
            String strMaxAttempts = settingsPanel.txtMaxAttempts.getText();
            int maxAttempts;

            try{
                maxAttempts = Integer.parseInt(strMaxAttempts);

                if (maxAttempts <= 0){
                    throw new NumberFormatException();
                }

                return maxAttempts;
            }catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for max attempts.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }

        public TimeType getTimeType(){
            return (StopWatch.TimeType) settingsPanel.listTimeTypes.getSelectedItem();
        }

        public List<String> getUpdateDescriptionList(){
            return settingsPanel.listUpdateTypes.getSelectedValuesList();
        }

        public String getAuthor(){
            return settingsPanel.txtAuthorMessage.getText();
        }

        public String getSaveMessage(){
            return settingsPanel.txtSaveMessage.getText();
        }



        public ProfileGraph2D getProfiler(){
            ProfileGraph2D renderer = selectedProfiler();
            if (renderer == null){ return null; }

            return applyDataset(applySettings(renderer));
        }

        public ProfileGraph2D selectedProfiler(){
            String strClass = settingsPanel.listRendererTypes.getSelectedItem().toString();
            return makeProfiler(strClass);
        }

        public ProfileGraph2D makeProfiler(String strClass){
            return factory(strClass);
        }

        public ProfileGraph2D applySettings(ProfileGraph2D renderer){
            renderer.getProfileSettings().setTestTime(getTestTime());
            renderer.getProfileSettings().setMaxTries(getMaxTries());
            renderer.getProfileSettings().setTimeType(getTimeType());
            renderer.getRenderSettings().setUpdate(getUpdateDescriptionList());

            renderer.getResolution().setWidth(getWidth());
            renderer.getResolution().setHeight(getHeight());

            renderer.getSaveSettings().setAuthorMessage(getAuthor());
            renderer.getSaveSettings().setSaveMessage(getSaveMessage());

            return renderer;
        }

        public ProfileGraph2D applyDataset(ProfileGraph2D renderer){
            String strSize = profile1DTable.txtDatasetSize.getText();
            int size;

            try{
                if (renderer instanceof ProfileIntensityGraph2D){
                    ProfileIntensityGraph2D i = (ProfileIntensityGraph2D) renderer;

                    int w, h;
                    if (strSize.contains("x")){
                        w = Integer.parseInt(strSize.substring(0, strSize.indexOf("x")));
                        h = (Integer.parseInt(strSize.substring(strSize.indexOf("x")+1)));

                        i.setNumXDataPoints(w);
                        i.setNumYDataPoints(h);
                        return i;
                    }
                }

                size = Integer.parseInt(strSize);
                renderer.setNumDataPoints(size);

                if (size <= 0){
                    throw new NumberFormatException();
                }
            }catch (Exception e){
                JOptionPane.showMessageDialog(null, "Enter a positive non-zero integer for the dataset size. Use 1000x1000 for 2D cell data.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            return renderer;
        }
    }
    private class ActionModel{
        private void startTimer(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    Timer t = new Timer();
                    t.scheduleAtFixedRate(new TimerTask(){

                            @Override
                            public void run() {
                                publish(getTime());
                            }

                        }

                        , 1000, 1000
                    );
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        VisualProfiler.this.console.txtTime.setText(chunk);
                    }
                }

                /**
                 * Gets the current time (HH:MM:SS) to be displayed in the
                 * graphical user interface.
                 * @return current time (HH:MM:SS)
                 */
                private String getTime(){
                    int hour = Calendar.getInstance().get(Calendar.HOUR);
                    int minute = Calendar.getInstance().get(Calendar.MINUTE);
                    int second = Calendar.getInstance().get(Calendar.SECOND);
                    String format = "%02d";

                    return String.format(format, hour) +
                           ":" +
                           String.format(format, minute) +
                           ":" +
                           String.format(format, second);

                }
            };
            worker.execute();
        }

        /**
         * Simple login feature that ensures all output files generated
         * by profiling is associated with an author.
         * <p>
         * Provides the option to exit the application if the user so chooses.
         */
        private String login(){
            String input = null;
            boolean exit = false;
            final String cancelMessage = "Do you want to exit the application?";

            //Validate
            while (!exit){
                input = JOptionPane.showInputDialog(null, "Username: ", "Login",
                                                    JOptionPane.PLAIN_MESSAGE);

                //Warning
                if (input == null || input.equals("")){
                    int cancel = JOptionPane.showConfirmDialog(null, cancelMessage,
                                                               "Cancel",
                                                               JOptionPane.YES_NO_OPTION);

                    //User wants to close program
                    if (cancel == JOptionPane.YES_OPTION){
                        exit = true;
                        System.exit(0);
                    }
                }
                //Valid, exits looop
                else{
                    exit = true;
                }
            }

            return input;
        }

        /**
         * Thread safe operation to start a <code>ProfileGraph2D</code>
         * for the renderer selected from the graphical user interface.
         * Uses the given settings taken from the graphical user interface
         * and saves to the specified output file.
         */
        private void profile1D(){
            final ProfileGraph2D profiler = userSettings.getProfiler();

            SwingWorker worker = new SwingWorker<Object, String>(){
                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);

                    ///Begin message
                    publish("--------\n");
                    publish(profiler.getGraphTitle() + ": Single Profile\n");

                    //Runs
                    publish(profiler.getResolution() + ": " + profiler.getNumDataPoints() + ":" + "    ");
                    profiler.profile();
                    publish(profiler.getStatistics().getAverageTime() + "ms" + "\n");

                    //Saves Data
                    if (!Thread.currentThread().isInterrupted()){
                        publish("Saving...");
                        profiler.saveStatistics();
                        publish("finished.\n");


                        //Displays results graph if checked
                        if (profile1DTable.getShowGraph()){
                            publish("Graphing Results...");
                            profiler.graphStatistics();
                            publish("finished.\n");
                        }

                        //Saves image if checked
                        if (userSettings.getSaveImage()){
                            publish("Saving Image...");
                            profiler.saveImage();
                            publish("finished.\n");
                        }

                        //Finish message
                        publish("\nProfiling completed.\n");
                        publish("--------\n");
                    }else{
                        //Finish message
                        publish("\nProfiling cancelled.\n");
                        publish("--------\n");
                    }

                    setEnabledActions(true);
                    threadFinish();

                    return null;
                }


                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Thread safe operation to start a <code>ProfileGraph2D</code>
         * for every supported renderer.
         * Uses the given settings taken from the graphical user interface
         * and saves to the specified output file.
         */
        private void profile1DAll(){

            //Profile Creation
            final List<ProfileGraph2D> profilers = new ArrayList<>();


            for (int i = 0; i < SUPPORTED_PROFILERS.length; i++){
                profilers.add(userSettings.applySettings(userSettings.makeProfiler(SUPPORTED_PROFILERS[i])));

            }

            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);

                    for (final ProfileGraph2D profiler: profilers){
                        if (Thread.currentThread().isInterrupted()){
                            break;  //quits loop if interrupted
                        }

                        ///Begin message
                        publish("--------\n");
                        publish(profiler.getGraphTitle() + ": Single Profile\n");

                        //Runs
                        publish(profiler.getResolution() + ": " + profiler.getNumDataPoints() + ":" + "    ");
                        profiler.profile();
                        publish(profiler.getStatistics().getAverageTime() + "ms" + "\n");


                        if (!Thread.currentThread().isInterrupted()){
                            //Saves
                            publish("Saving...");
                            profiler.saveStatistics();
                            publish("finished.\n");

                            //Displays results graph if checked
                            if (profile1DTable.getShowGraph()){
                                publish("Graphing Results...");
                                profiler.graphStatistics();
                                publish("finished.\n");
                            }

                            //Saves image if checked
                            if (userSettings.getSaveImage()){
                                publish("Saving Image...");
                                profiler.saveImage();
                                publish("finished.\n");
                            }

                            //Finish message
                            publish("\nProfiling completed.\n");
                            publish("--------\n");
                        }else{
                            //Finish message
                            publish("\nProfiling cancelled.\n");
                            publish("--------\n");
                        }


                    }

                    setEnabledActions(true);
                    threadFinish();
                    return null;
                }


                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();

        }

        /**
         * Thread safe operation to start a <code>MultiLevelProfiler</code>
         * profile and saves the results to an output file.
         */
        private void profile2D(){
            List<Resolution> resolutions = profile2DTable.listResolutions.getSelectedValuesList();
            List<Integer> datasetSizes = profile2DTable.listNPoints.getSelectedValuesList();
            ProfileGraph2D profiler = userSettings.getProfiler();

            if (!resolutions.isEmpty() && !datasetSizes.isEmpty() && userSettings.getProfiler() != null){
                Profile2DTableThread worker = new Profile2DTableThread(profiler, resolutions, datasetSizes);
                worker.execute();
            }
            else{
                JOptionPane.showMessageDialog(null, "Profiling was cancelled due to invalid settings.", "Run Fail", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Thread safe operation to analyze all 1D tables
         * (<code>MultiLevelProfiler</code> output, not <code>ProfileGraph2D</code> output)
         * and saves the results to an output file.
         */
        private void compare2DTables(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);
                    publish("--------\n");
                    publish("Comparing Tables...");
                    ProfileAnalysis.compareTables2D();
                    publish("finished.\n");
                    publish("--------\n");
                    setEnabledActions(true);
                    threadFinish();
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Thread safe operation to analyze all 1D tables
         * (<code>ProfileGraph2D</code> output, not <code>MultiLevelProfiler</code> output)
         * and print the results (performance increase/decrease) to the
         * graphical user interface console.
         */
        private void analyze1DTable(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);
                    publish("--------\n");
                    publish("Comparing Single Profile Tables\n");

                    List<String> output = ProfileAnalysis.analyzeTables1D();
                    for (String out: output){
                        publish(out + "\n");
                    }

                    publish("Comparison completed.\n");
                    publish("--------\n");
                    setEnabledActions(true);
                    threadFinish();
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        private void openFiles(final List<File> files){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);
                    publish("--------\n");
                    publish("Opening Files\n");

                    Desktop desktop = Desktop.getDesktop();
                    TreePath[] paths = fileViewer.tree.getSelectionPaths();

                    //Passed by parameter
                    if (files != null && desktop != null){
                        for (File file: files){
                            if (Thread.currentThread().isInterrupted()){
                                break;
                            }

                            if (file != null){
                                try{
                                        desktop.open(file);

                                        publish(file.getName() + "...opened successfully!\n");
                                    }
                                    catch(IOException e){
                                        publish("Unable to open: " +
                                                file.getName() +
                                                "\n"
                                               );
                                    }
                                    catch(ClassCastException e){
                                        //unable to open
                                    }
                            }
                        }
                    }

                    if (!Thread.currentThread().isInterrupted()){
                        publish("File operations completed.\n");
                        publish("--------\n");
                    }
                    else{
                        publish("File operations cancelled.\n");
                        publish("--------\n");
                    }

                    setEnabledActions(true);
                    threadFinish();
                    return null;

                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Thread safe operation to open all selected files from the
         * file tree with the default application to open the files.
         */
        private void openFiles(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);
                    publish("--------\n");
                    publish("Opening Files\n");

                    Desktop desktop = Desktop.getDesktop();
                    TreePath[] paths = fileViewer.tree.getSelectionPaths();

                    //Selected from tree
                    if (paths != null && desktop != null){
                        for (TreePath path: paths){
                            if (Thread.currentThread().isInterrupted()){
                                break;
                            }

                            if (path != null){
                                try{
                                    File toOpen = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                                    desktop.open(toOpen);

                                    publish(toOpen.getName() + "...opened successfully!\n");
                                }
                                catch(IOException e){
                                    publish("Unable to open: " +
                                            ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() +
                                            "\n"
                                           );
                                }
                                catch(ClassCastException e){
                                    //unable to open
                                }
                            }
                        }
                    }

                    if (!Thread.currentThread().isInterrupted()){
                        publish("File operations completed.\n");
                        publish("--------\n");
                    }
                    else{
                        publish("File operations cancelled.\n");
                        publish("--------\n");
                    }

                    setEnabledActions(true);
                    threadFinish();
                    return null;

                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Thread safe operation to delete all selected files from the
         * file tree.
         */
        private void deleteFiles(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);

                    publish("--------\n");
                    publish("Deleting Files\n");

                    TreePath[] paths = fileViewer.tree.getSelectionPaths();

                    if (paths != null){
                        for (TreePath path: paths){
                            if (Thread.currentThread().isInterrupted()){
                                break;  //quits loop
                            }

                            if (path != null){
                                try{
                                    File toDelete = (File) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

                                    //Checks if file is protected
                                    boolean protect = false;
                                    for (String name: PROTECTED_FILES){
                                        if (toDelete.getName().equals(name)){
                                            protect = true;
                                            break;
                                        }
                                    }

                                    //Safe to delete
                                    if (!protect){
                                        Files.delete(toDelete.toPath());
                                        publish(toDelete.getName() + "...deleted successfully!\n");
                                    }
                                    else{
                                        publish("Unable to delete: " + toDelete.getName() + " (Protected)\n");
                                    }
                                }
                                catch(IOException e){
                                    publish("Unable to delete: " +
                                            ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() +
                                            "\n"
                                           );
                                }
                                catch(ClassCastException e){
                                    //unable to open
                                }
                            }
                        }
                    }

                    fileViewer.reloadNodes();

                    if (!Thread.currentThread().isInterrupted()){
                        publish("File operations completed.\n");
                        publish("--------\n");
                    }
                    else{
                        publish("File operations cancelled.\n");
                        publish("--------\n");
                    }

                    setEnabledActions(true);
                    threadFinish();
                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Thread safe operation to refresh nodes of the file tree.
         * Prints a 'refresh' message to the console log.
         */
        private void reloadFiles(){
            this.reloadFiles(false);
        }

        /**
         * Thread safe operation to refresh nodes of the file tree.
         * @param silent true to not print a 'refresh' message to the console log,
         *               false to print a 'refresh' message to the console log
         */
        private void reloadFiles(final boolean silent){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    if (!silent){
                        setEnabledActions(false);
                        threadStart(this);
                        publish("--------\n");
                        publish("Refreshing File Browser...");
                    }

                    fileViewer.reloadNodes();

                    if (!silent){
                        publish("finished.\n");
                        publish("--------\n");
                        setEnabledActions(true);
                        threadFinish();
                    }

                    return null;
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Takes a node with a <code>File</code> user object and adds
         * all subfiles as children nodes.
         *
         * @param parentNode must contain a <code>File</code> as the user object;
         *                   adds all subfiles of the node
         */
        private void addNodes(DefaultMutableTreeNode parentNode){
            //Get subfiles of the node
            File[] subfiles = ((File)parentNode.getUserObject()).listFiles();

            //If there are files within (non-directories would not have subfiles)
            if (subfiles != null){

                //All subfiles
                for (File subfile: subfiles){

                    //Is valid subfile
                    if (subfile != null){
                        //Add node
                        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(subfile);
                        parentNode.add(childNode);
                        this.addNodes(childNode);
                    }
                }
            }
        }

        /**
         * Thread safe operation to save the console log of the graphical
         * user interface.
         * The log is saved as a <b>.txt</b> file with the current timestamp.
         */
        private void saveLog(){
            SwingWorker worker = new SwingWorker<Object, String>(){

                @Override
                protected Object doInBackground() throws Exception {
                    setEnabledActions(false);
                    threadStart(this);

                    //Where saving occurs
                    saveFile();

                    publish("--------\n");
                    publish("Saving Log...");

                    //Saves beforehand to prevent this from being in log

                    publish("finished.\n");
                    publish("--------\n");
                    setEnabledActions(true);
                    threadFinish();
                    return null;
                }

                private void saveFile(){
                    //Creates file
                    File outputFile = new File(ProfileGraph2D.LOG_FILEPATH +
                                      DateUtils.getDate(DateFormat.NONDELIMITED) +
                                      "-Log.txt");

                    try {
                        outputFile.createNewFile();

                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));

                        //Prints console
                        out.print(console.txtConsole.getText());

                        out.close();
                    } catch (IOException ex) {
                        System.err.println("Output errors exist.");
                    }
                }

                @Override
                protected void process(List<String> chunks){
                    for (String chunk: chunks){
                        print(chunk);
                    }
                }
            };
            worker.execute();
        }

        /**
         * Empties the console log of the graphical user interface.
         */
        private void clearLog(){
            console.txtConsole.setText("");
        }

        private void threadStart(SwingWorker worker){
            if (worker == null){
                throw new IllegalArgumentException("Must have a non-null thread.");
            }

            thread = worker;
            console.btnCancelThread.setEnabled(true);
        }

        private void threadFinish(){
            thread = null;
            console.btnCancelThread.setEnabled(false);

            setEnabledActions(true);
        }

        private void cancelThread(){
            if (thread != null){
                thread.cancel(true);
                console.btnCancelThread.setEnabled(false);

                SwingWorker worker = new SwingWorker(){

                    @Override
                    protected Object doInBackground() throws Exception {
                        publish("\nAction Cancelled\n");
                        publish("--------\n");
                        return null;
                    }

                };
                worker.execute();
            }
        }

        /**
         * Creates a thread safe <code>SwingWorker</code> that performs
         * a <code>MultiLevelProfiler</code> profile and prints
         * the results to the console log of the graphical user interface
         * as the results are received.
         */
        private class Profile2DTableThread extends SwingWorker<Object, String>{
            private Profile2DTableThread.VisualMultiLevelProfiler multiProfiler;

            public Profile2DTableThread(ProfileGraph2D profiler, List<Resolution> resolutions, List<Integer> datasetSizes){
                setEnabledActions(false);
                model.threadStart(this);
                publish("--------\n");
                publish(profiler.getGraphTitle() + "\n\n");

                String strAuthor = settingsPanel.txtAuthorMessage.getText();
                String saveMessage = settingsPanel.txtSaveMessage.getText();

                this.multiProfiler = new Profile2DTableThread.VisualMultiLevelProfiler(profiler);
                this.multiProfiler.getSaveSettings().setAuthorMessage(strAuthor);
                this.multiProfiler.getSaveSettings().setSaveMessage(saveMessage);
                this.multiProfiler.setImageSizes(resolutions);
                this.multiProfiler.setDatasetSizes(datasetSizes);
            }

            @Override
            protected Object doInBackground() throws Exception {
                this.multiProfiler.profile();

                if (!Thread.currentThread().isInterrupted()){
                    this.multiProfiler.saveStatistics();

                    //Saves image if checked
                    if (userSettings.getSaveImage()){
                        publish("Saving Images...");
                        this.multiProfiler.saveImages();
                        publish("finished.\n");
                    }

                    publish("\nProfiling complete." + "\n");
                    publish("--------\n");
                }
                else{
                    publish("\nProfiling cancelled." + "\n");
                    publish("--------\n");
                }

                setEnabledActions(true);
                model.threadFinish();
                return true;
            }

            @Override
            protected void process(List<String> chunks){
                for (String chunk: chunks){
                    print(chunk);
                }
            }

            private class VisualMultiLevelProfiler extends MultiLevelProfiler{
                public VisualMultiLevelProfiler(ProfileGraph2D profiler){
                    super(profiler);
                }

                @Override
                public void processTimeWarning(int estimatedTime){
                    Profile2DTableThread.this.publish("The estimated run time is " + estimatedTime + " seconds." + "\n\n");
                }

                @Override
                public void processPreResult(Resolution resolution, int datasetSize){
                    //Publishes
                    Profile2DTableThread.this.publish(resolution + ": " + datasetSize + ":" + "    ");
                }

                @Override
                public void processResult(Resolution resolution, int datasetSize, Statistics stats){
                    Profile2DTableThread.this.publish(stats.getAverageTime() + "ms" + "\n");
                }
            };
        };
    }
    //-------------------------------------------------------------------------


    //Constructor
    //-------------------------------------------------------------------------
    /**
     * Constructs a panel to perform profiling operations.
     */
    public VisualProfiler(){
        initComponents();

        addComponents();
        addListeners();

        finalizePanel();
    }
    //-------------------------------------------------------------------------


    //Panel Setup
    //-------------------------------------------------------------------------

    /**
     * Initializes all graphical user interface components.
     */
    private void initComponents(){
        //Data
        tabs = new JTabbedPane();
        actionButtons = new ArrayList<>();
        model = new ActionModel();
        userSettings = new UserSettings();

        //Panels
        settingsPanel = new SettingsPanel();
        profile1DTable = new Profile1DTable();
        profile2DTable = new Profile2DTable();
        analyzePanel = new AnalyzePanel();
        fileViewer = new FileViewer();
        console = new Console();
    }

    /**
     * Adds all graphical user interface components to the <code>JPanel</code>.
     */
    private void addComponents(){
        //Tabs
        tabs.addTab("Profile 1D Table", profile1DTable);
        tabs.addTab("Profile 2D Table", profile2DTable);
        tabs.addTab("Control Panel", analyzePanel);
        tabs.addTab("File Browser", fileViewer);

        JSplitPane top = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        top.setTopComponent(settingsPanel);
        top.setBottomComponent(tabs);

        JSplitPane bottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        bottom.setTopComponent(top);
        bottom.setBottomComponent(console);

        //Add to panel hiearchy
        this.add(bottom);
    }

    /**
     * Adds action listeners to each button: associates each button
     * with a method of the <code>VisualProfiler</code>.
     */
    private void addListeners(){
        ActionListener listener = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Object o = e.getSource();

                if (o == profile1DTable.btnProfile1D){
                    model.profile1D();
                }
                else if (o == profile1DTable.btnProfile1DAll){
                    model.profile1DAll();
                }
                else if (o == profile2DTable.btnProfile2D){
                    model.profile2D();
                }
                else if (o == analyzePanel.btnCompare2DTables){
                    model.compare2DTables();
                }
                else if (o == analyzePanel.btnAnalyze1DTable){
                    model.analyze1DTable();
                }
                else if (o == fileViewer.btnOpenFiles){
                    model.openFiles();
                }
                else if (o == fileViewer.btnDeleteFiles){
                    model.deleteFiles();
                }
                else if (o == fileViewer.btnReloadFiles){
                    model.reloadFiles();
                }
                else if (o == console.btnSaveLog){
                    model.saveLog();
                }
                else if (o == console.btnClearLog){
                    model.clearLog();
                }
                else if (o == console.btnCancelThread){
                    model.cancelThread();
                }
            }

        };

        for (JButton button: this.actionButtons){
            button.addActionListener(listener);
        }

        tabs.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                model.reloadFiles(true);
            }

        });

        settingsPanel.listRendererTypes.addItemListener(new ItemListener(){

           @Override
           public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED){
                   reloadUpdateVariations();
               }
           }

        });

        fileViewer.tree.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent e) {
                int selRow = fileViewer.tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = fileViewer.tree.getPathForLocation(e.getX(), e.getY());

                if(selRow != -1) {
                    if(e.getClickCount() == 1) {
                        //single click does nothing
                    }
                    else if(e.getClickCount() == 2) {
                        File o = (File)((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
                        List<File> files = new ArrayList<>();
                        files.add(o);
                        model.openFiles(files);
                    }
                }
            }
        });
    }

    /**
     * Finalizes panel properties (login, list updates, timer start).
     */
    private void finalizePanel(){
        setAuthor(model.login());
        reloadUpdateVariations();
        model.reloadFiles(true);
        model.startTimer();
    }

    //-------------------------------------------------------------------------


    //Setters
    //-------------------------------------------------------------------------

    /**
     * Sets the current user, or author of the profiling.
     * The author will be saved upon any profiling.
     * @param author user of the profiling
     */
    public void setAuthor(String author){
        this.settingsPanel.txtAuthorMessage.setText(author);
    }

    /**
     * Enables (or disables) all action buttons in the panel.
     * @param enabled true to enable all buttons, otherwise false
     */
    public void setEnabledActions(boolean enabled){
        for (JButton button: this.actionButtons){
            button.setEnabled(enabled);
        }
    }

    //-------------------------------------------------------------------------


    //Actions
    //-------------------------------------------------------------------------

    /**
     * Prints the text to the console of the panel.
     * @param chunk text to print to the console
     */
    public void print(String chunk){
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(chunk);
        console.print(tmp);
    }

    /**
     * Prints all the text to the console of the panel.
     * @param chunks text elements to print to the console
     */
    public void print(List<String> chunks){
        console.print(chunks);
    }

    /**
     * Updates the list box containing the <code>Graph2DRendererUpdate</code>
     * variations associated with the selected <code>ProfileGraph2D</code>.
     */
    public void reloadUpdateVariations(){
        if (userSettings.selectedProfiler() == null){
            return;
        }

        DefaultComboBoxModel tmp = new DefaultComboBoxModel(
            userSettings.selectedProfiler().getVariations().keySet().toArray()
        );
        this.settingsPanel.listUpdateTypes.setModel(tmp);
    }

    //-------------------------------------------------------------------------


    //Helper
    //-------------------------------------------------------------------------

    /**
     * Makes a <code>JFrame</code> containing a <code>VisualProfiler</code>.
     * @return visible frame with a <code>VisualProfiler</code>
     */
    public static JFrame makeFrame(){
            JFrame frame = new JFrame(FRAME_TITLE);
            frame.add(new VisualProfiler());
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);  //Centers

            return frame;
    }

    /**
     * Creates a panel with the default layout manager
     * to add and center the component.
     * @param itemToAdd component to add to the panel
     * @return JPanel with default layout manager with
     *         component added to it
     */
    private JPanel blankPanel(Component itemToAdd){
        JPanel tmp = new JPanel();
        tmp.add(itemToAdd);
        return tmp;
    }

    //-------------------------------------------------------------------------


    //Main
    //-------------------------------------------------------------------------

    /**
     * Constructs a thread safe <code>VisualProfiler</code> JFrame.
     */
    public static void invokeVisualAid(){
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                makeFrame();
            }

        });
    }

    /**
     * Constructs a <code>VisualProfiler</code> to provide
     * graphical user interface options to profile renderers.
     * @param args no effect
     */
    public static void main(String[] args){
        VisualProfiler.invokeVisualAid();
    }

    //-------------------------------------------------------------------------
}
