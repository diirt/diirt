/*
 * Copyright 2010 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */

package org.epics.pvmanager.test;

import org.epics.pvmanager.CompositeDataSource;
import org.epics.pvmanager.jca.JCADataSource;
import org.epics.pvmanager.data.Util;
import org.epics.pvmanager.data.SimpleValueFormat;
import org.epics.pvmanager.data.ValueFormat;
import org.epics.pvmanager.ThreadSwitch;
import org.epics.pvmanager.data.VStatistics;
import org.epics.pvmanager.sim.SimulationDataSource;
import org.epics.pvmanager.PV;
import org.epics.pvmanager.PVManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import org.epics.pvmanager.PVValueChangeListener;
import org.epics.pvmanager.extra.DynamicGroup;
import static org.epics.pvmanager.ExpressionLanguage.*;
import static org.epics.pvmanager.extra.ExpressionLanguage.*;

/**
 *
 * @author carcassi
 */
public class MockDynamicTablePVFrame extends javax.swing.JFrame {
    
    private DynamicGroup group = group();
    private PV<List<Object>> pv = PVManager.read(group).andNotify(ThreadSwitch.onSwingEDT()).atHz(2);
    private List<Object> latestValue = null;
    private List<String> pvNames = new ArrayList<String>();
    private ValueFormat format = new SimpleValueFormat(3);
    
    private TableModel model = new AbstractTableModel() {
        
        private List<String> titles = Arrays.asList("PV name", "Value", "Alarm", "Time");
        
        {
            pv.addPVValueChangeListener(new PVValueChangeListener() {

                @Override
                public void pvValueChanged() {
                    latestValue = pv.getValue();
                    fireTableRowsUpdated(0, getRowCount());
                }
            });
        }

        @Override
        public String getColumnName(int column) {
            return titles.get(column);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public int getRowCount() {
            return pvNames.size() + 1;
        }

        @Override
        public int getColumnCount() {
            return titles.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    if (rowIndex >= pvNames.size()) {
                        return "";
                    } else {
                        return pvNames.get(rowIndex);
                    }
                case 1:
                    if (rowIndex >= pvNames.size() || latestValue == null) {
                        return "";
                    } else {
                        return format.format(latestValue.get(rowIndex));
                    }
                case 2:
                    if (rowIndex >= pvNames.size() || latestValue == null) {
                        return "";
                    } else {
                        return alarmSeverityOf(latestValue.get(rowIndex));
                    }
                case 3:
                    if (rowIndex >= pvNames.size() || latestValue == null) {
                        return "";
                    } else {
                        return timeStampOf(latestValue.get(rowIndex));
                    }
            }
            throw new RuntimeException();
        }
        
        private String alarmSeverityOf(Object value) {
            if (value == null)
                return "";
            return Util.alarmOf(value).getAlarmSeverity().toString();
        }
        
        private String timeStampOf(Object value) {
            if (value == null)
                return "";
            return Util.timeOf(value).getTimeStamp().toString();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 0;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex != 0) {
                throw new RuntimeException();
            }
            
            String name = aValue.toString();
            
            try {
                if (rowIndex == pvNames.size()) {
                    group.add(latestValueOf(channel(name)));
                    pvNames.add(name);
                } else {
                    group.set(rowIndex, latestValueOf(channel(name)));
                    pvNames.set(rowIndex, name);
                }
                fireTableDataChanged();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MockDynamicTablePVFrame.this, ex.getMessage(), "Can't open pv", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        
        
    };

    /** Creates new form MockPVFrame */
    public MockDynamicTablePVFrame() {
        initComponents();
        pvTable.setModel(model);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        pvTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(pvTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    List<PV<VStatistics>> pvs = new ArrayList<PV<VStatistics>>();

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        PVManager.setDefaultThread(ThreadSwitch.onSwingEDT());
        CompositeDataSource dataSource = new CompositeDataSource();
        dataSource.putDataSource("sim", SimulationDataSource.simulatedData());
        dataSource.putDataSource("epics", new JCADataSource());
        dataSource.setDefaultDataSource("sim");
        PVManager.setDefaultDataSource(dataSource);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MockDynamicTablePVFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable pvTable;
    // End of variables declaration//GEN-END:variables

}
