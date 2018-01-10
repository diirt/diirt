/**
 * Copyright (C) 2010-18 diirt developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.diirt.datasource.sample;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import org.diirt.vtype.ValueUtil;
import org.diirt.vtype.SimpleValueFormat;
import org.diirt.vtype.ValueFormat;
import org.diirt.vtype.VStatistics;
import org.diirt.datasource.PVReader;
import org.diirt.datasource.PVManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.diirt.datasource.PVReaderListener;
import org.diirt.datasource.PVWriter;
import org.diirt.datasource.extra.DynamicGroup;
import static org.diirt.datasource.ExpressionLanguage.*;
import org.diirt.datasource.PVReaderEvent;
import static org.diirt.datasource.extra.ExpressionLanguage.*;
import static org.diirt.util.concurrent.Executors.swingEDT;
import static org.diirt.util.time.TimeDuration.*;

/**
 *
 * @author carcassi
 */
public class MockDynamicTablePVFrame extends javax.swing.JFrame {

    private DynamicGroup group = group();
    private PVReader<List<Object>> pv = PVManager.read(group)
            .notifyOn(swingEDT()).maxRate(ofHertz(2));
    private List<Object> latestValue = null;
    private List<Exception> latestExceptions = null;
    private List<String> pvNames = new ArrayList<String>();
    private ValueFormat format = new SimpleValueFormat(3);

    private AbstractTableModel model = new AbstractTableModel() {

        private List<String> titles = Arrays.asList("PV name", "Value", "Alarm", "Time");

        {
            pv.addPVReaderListener(new PVReaderListener<List<Object>>() {

                @Override
                public void pvChanged(PVReaderEvent<List<Object>> event) {
                    latestValue = pv.getValue();
                    latestExceptions = group.lastExceptions();
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
                    if (latestValue == null || rowIndex >= latestValue.size()) {
                        return "";
                    } else {
                        return format.format(latestValue.get(rowIndex));
                    }
                case 2:
                    if (latestValue == null || rowIndex >= latestValue.size()) {
                        return "";
                    } else {
                        return alarmSeverityOf(latestValue.get(rowIndex));
                    }
                case 3:
                    if (latestValue == null || rowIndex >= latestValue.size()) {
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
            return ValueUtil.alarmOf(value).getAlarmSeverity().toString();
        }

        private String timeStampOf(Object value) {
            if (value == null)
                return "";
            return ValueUtil.timeOf(value).getTimestamp().toString();
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
                latestExceptions = group.lastExceptions();
                fireTableDataChanged();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MockDynamicTablePVFrame.this, ex.getMessage(), "Can't open pv", JOptionPane.ERROR_MESSAGE);
            }
        }



    };

    private AbstractTableModel writeModel = new AbstractTableModel() {

        private List<String> titles = Arrays.asList("PV name", "New Value");
        private List<String> writePvNames = new ArrayList<String>();
        private List<String> writePvValues = new ArrayList<String>();
        private List<PVWriter> pvWriters = new ArrayList<PVWriter>();

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
            return writePvNames.size() + 1;
        }

        @Override
        public int getColumnCount() {
            return titles.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch(columnIndex) {
                case 0:
                    if (rowIndex >= writePvNames.size()) {
                        return "";
                    } else {
                        return writePvNames.get(rowIndex);
                    }
                case 1:
                    if (rowIndex >= writePvValues.size()) {
                        return "";
                    } else {
                        return writePvValues.get(rowIndex);
                    }
            }
            throw new RuntimeException();
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex > 2) {
                throw new RuntimeException();
            }

            if (columnIndex == 1) {
                writePvValues.set(rowIndex, aValue.toString());
                pvWriters.get(rowIndex).write(aValue.toString());
            } else {
                String name = aValue.toString();

                try {
                    if (rowIndex == writePvNames.size()) {
                        pvWriters.add(PVManager.write(channel(name)).async());
                        writePvNames.add(name);
                        writePvValues.add("");
                    } else {
                        pvWriters.set(rowIndex, PVManager.write(channel(name)).async());
                        writePvNames.set(rowIndex, name);
                        writePvValues.set(rowIndex, "");
                    }
                    fireTableDataChanged();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MockDynamicTablePVFrame.this, ex.getMessage(), "Can't open pv", JOptionPane.ERROR_MESSAGE);
                }
            }
        }



    };

    /** Creates new form MockPVFrame */
    public MockDynamicTablePVFrame() {
        initComponents();
        pvTable.setModel(model);
        writePvTable.setModel(writeModel);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component result = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (latestExceptions != null && row < latestExceptions.size() && latestExceptions.get(row) != null) {
                    setToolTipText(latestExceptions.get(row).getMessage());
                    result.setForeground(Color.red);
                } else {
                    setToolTipText(null);
                    result.setForeground(table.getForeground());
                }
                return result;
            }

        };
        pvTable.setDefaultRenderer(String.class, renderer);
        Action deleteAction = new AbstractAction("delete") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int[] viewRows = pvTable.getSelectedRows();
                for (int i = viewRows.length - 1; i >= 0; i--) {
                    int modelRow = pvTable.convertRowIndexToModel(viewRows[i]);
                    group.remove(modelRow);
                    pvNames.remove(modelRow);
                    model.fireTableRowsDeleted(modelRow, modelRow);
                }
            }
        };
        pvTable.getActionMap().put("delete", deleteAction);
        pvTable.getInputMap(pvTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("DELETE"), "delete");
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
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        writePvTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(pvTable);

        jLabel1.setText("Read:");

        jScrollPane2.setViewportView(writePvTable);

        jLabel2.setText("Write:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 739, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    List<PVReader<VStatistics>> pvs = new ArrayList<PVReader<VStatistics>>();

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        PVManager.setDefaultNotificationExecutor(swingEDT());
        SetupUtil.defaultCASetupForSwing();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MockDynamicTablePVFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable pvTable;
    private javax.swing.JTable writePvTable;
    // End of variables declaration//GEN-END:variables

}
