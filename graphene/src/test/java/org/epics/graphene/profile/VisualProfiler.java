/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene.profile;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author asbarber
 */
public class VisualProfiler extends JFrame{
    private JPanel mainPanel;
    
    //Middle
    private JLabel              lblResolutions,
                                lblNPoints;
    
    private JButton             btnStart;
    
    private JList<Resolution>               listResolutions;
    private JList<Integer>                  listNPoints;
    private DefaultListModel<Resolution>    modelResolutions;
    private DefaultListModel<Integer>       modelNPoints;
    
    private JTextArea          console;
    
    
    public VisualProfiler(){
        super("Visual Profiler");
        
        initFrame();
        initComponents();
        loadLists();
        addListeners();
        addComponents();
        
        super.pack();
    }
    
    //Swing Setup
    private void initFrame(){
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setVisible(true);
    }
    private void initComponents(){
        mainPanel = new JPanel();              
        
        //Middle Area
        //------------
        
        lblResolutions = new JLabel("Resolutions");
        lblNPoints = new JLabel("N Points");
        btnStart = new JButton("Start");
        
        listResolutions = new JList<>();
        listNPoints = new JList<>();
        
        console = new JTextArea(20, 50);
        console.setEditable(false);
    }
    private void loadLists(){
        modelResolutions = new DefaultListModel<>();
        modelNPoints = new DefaultListModel<>();
        
        for (Resolution resolution : Resolution.defaultResolutions()){
            modelResolutions.addElement(resolution);
        }
        
        for (Integer datasetSize : MultiLevelProfiler.defaultDatasetSizes()){
            modelNPoints.addElement(datasetSize);
        }
                
        listResolutions.setModel(modelResolutions);
        listNPoints.setModel(modelNPoints);
    }
    private void addListeners(){
        btnStart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                VisualProfiler.this.startAction();
            }
            
        });
    }    
    private void addComponents(){
        
        
        JPanel left = new JPanel();
        left.setLayout(new BorderLayout());
        left.add(lblResolutions, BorderLayout.NORTH);
        left.add(listResolutions, BorderLayout.CENTER);
        
        JPanel middle = new JPanel();
        middle.setLayout(new BorderLayout());
        middle.add(lblNPoints, BorderLayout.NORTH);
        middle.add(listNPoints, BorderLayout.CENTER);
        
        JPanel right = new JPanel();
        right.setLayout(new BorderLayout());        
        right.add(btnStart, BorderLayout.NORTH);
        right.add(console, BorderLayout.CENTER);
        
            JSplitPane inner = new JSplitPane();
            JSplitPane outer = new JSplitPane();

            inner.setLeftComponent(left);
            inner.setRightComponent(middle);

            outer.setLeftComponent(inner);
            outer.setRightComponent(right);
        
        //Add to panel hiearchy
        mainPanel.add(outer);
        super.add(mainPanel);
    }
    
    
    //Actions
    
    private void startAction(){
        List<Resolution> resolutions = listResolutions.getSelectedValuesList();
        List<Integer> datasetSizes = listNPoints.getSelectedValuesList();
        ProfileGraph2D profiler = this.getProfiler();

        if (!resolutions.isEmpty() && !datasetSizes.isEmpty() && getProfiler() != null){
            consoleAppend("\n" + profiler.getGraphTitle());

            VisualMultiLevelProfiler multiprofiler = new VisualMultiLevelProfiler(getProfiler());
            multiprofiler.setImageSizes(resolutions);
            multiprofiler.setDatasetSizes(datasetSizes);
            multiprofiler.run();
            multiprofiler.saveStatistics();
        }    
        else{
            JOptionPane.showMessageDialog(null, "Profiling was cancelled due to invalid settings.", "Run Fail", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    //Helper
    
    public ProfileSparklineGraph2D getProfiler(){
        ProfileSparklineGraph2D graph = new ProfileSparklineGraph2D();
        graph.setTestTime(1);
        return graph;
    }
    private void consoleAppend(final String output){
        synchronized (this) {
            console.append(output);
        }
    }
    private class VisualMultiLevelProfiler extends MultiLevelProfiler{
        public VisualMultiLevelProfiler(ProfileGraph2D profiler){
            super(profiler);
        }

        @Override
        public void processTimeWarning(int estimatedTime){
            consoleAppend("The estimated run time is " + estimatedTime + " seconds.");
        }

        @Override
        public void processPreResult(Resolution resolution, int datasetSize){
            consoleAppend(resolution + ": " + datasetSize + ": ");
        }

        @Override
        public void processResult(Resolution resolution, int datasetSize, Statistics stats){
            consoleAppend(stats.getAverageTime() + "ms" + "\n");
        }        
    };
    private JPanel buildTempPanel(Component component){
        JPanel panel = new JPanel();
        
        panel.add(component);
        
        panel.add(component);
        return panel;
    }
    
    //Static
    
    public static void invokeVisualAid(){
        EventQueue.invokeLater(new Runnable(){

            @Override
            public void run() {
                VisualProfiler frame = new VisualProfiler();
            }
            
        });
    }  
    public static void main(String[] args){
        VisualProfiler.invokeVisualAid();
    }
}
