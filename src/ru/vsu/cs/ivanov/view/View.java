package ru.vsu.cs.ivanov.view;

import ru.vsu.cs.ivanov.controller.SpringUtilities;
import ru.vsu.cs.ivanov.controller.Utils;
import ru.vsu.cs.ivanov.model.Model;
import ru.vsu.cs.ivanov.model.Storage;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class View {
    final static int GAP = 10;

    public View() {
        JFrame frame = new JFrame("The ranking of processors");

        JTable table = new JTable();

        Model model = new Model();
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if ((row >= 0) && (column >= 0)) {
                    TableModel model = (TableModel) e.getSource();
                    Object data = model.getValueAt(row, column);
                    if (Utils.isFloat(data.toString())) {
                        Storage.getData()[row][column] = Double.parseDouble(data.toString());
                    } else {
                        Storage.getData()[row][column] = data.toString();
                    }
                }
            }
        });

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(1700, 900));

        JMenu fileMenu = new  JMenu("Menu");

        JMenuItem saveChartItem = new JMenuItem("Save");
        fileMenu.add(saveChartItem);
        saveChartItem.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Storage.rewriteFile();
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu fileEdit = new  JMenu("Edit table");

        JMenuItem addCpuItem = new JMenuItem("Add CPU");
        fileEdit.add(addCpuItem);
        addCpuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(frame, "Add CPU", true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setSize(600, 300);
                dialog.setLocationRelativeTo(frame);

                JPanel cont = new JPanel();
                cont.setLayout(new BoxLayout(cont, BoxLayout.PAGE_AXIS));

                JPanel panel = new JPanel(new SpringLayout());

                JLabel[] labels = new JLabel[Storage.getHeader().length];
                ArrayList<JTextField> fieldsRow = new ArrayList<>();
                ArrayList<JTextField> fieldsColumn = new ArrayList<>();

                for (int i = 0; i < Storage.getHeader().length; i++) {
                    labels[i] = new JLabel(Storage.getHeader()[i], JLabel.TRAILING);
                    fieldsRow.add(new JTextField());
                    fieldsRow.get(i).setColumns(20);
                    labels[i].setLabelFor(fieldsRow.get(i));
                    panel.add(labels[i]);
                    panel.add(fieldsRow.get(i));
                }

                SpringUtilities.makeCompactGrid(panel,
                        fieldsRow.size(), 2,
                        GAP, GAP, //init x,y
                        GAP, GAP/2);//xpad, ypad

                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

                JButton addColumnButton = new JButton("Add new column");
                addColumnButton.addActionListener (new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        fieldsColumn.add(new JTextField());
                        fieldsRow.add(new JTextField());
                        panel.add(fieldsColumn.get(fieldsColumn.size() - 1));
                        panel.add(fieldsRow.get(fieldsRow.size() - 1));
                        SpringUtilities.makeCompactGrid(panel,
                                fieldsRow.size(), 2,
                                GAP, GAP, //init x,y
                                GAP, GAP/2);//xpad, ypad
                        panel.revalidate();
                        dialog.pack();
                    }
                });
                buttonPanel.add(addColumnButton);

                JButton removeColumnButton = new JButton("Remove new column");
                removeColumnButton.addActionListener (new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (fieldsColumn.size() > 0) {
                            JTextField fieldColumn = fieldsColumn.remove(fieldsColumn.size() - 1);
                            JTextField fieldRow = fieldsRow.remove(fieldsRow.size() - 1);
                            panel.remove(fieldRow);
                            panel.remove(fieldColumn);
                            SpringUtilities.makeCompactGrid(panel,
                                    fieldsRow.size(), 2,
                                    GAP, GAP, //init x,y
                                    GAP, GAP / 2);//xpad, ypad
                            dialog.pack();
                        }
                    }
                });
                buttonPanel.add(removeColumnButton);

                JButton saveButton = new JButton("Save");
                saveButton.addActionListener (new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String[] newRow = new String[fieldsRow.size()];
                        for (int i = 0; i < fieldsRow.size(); i++) {
                            if (!fieldsRow.get(i).getText().isEmpty()) {
                                newRow[i] = fieldsRow.get(i).getText();
                            } else {
                                newRow[i] = null;
                            }
                        }
                        String[] newColumns = new String[fieldsColumn.size()];
                        for (int i = 0; i < fieldsColumn.size(); i++) {
                            if (!fieldsColumn.get(i).getText().isEmpty()) {
                                newColumns[i] = fieldsColumn.get(i).getText();
                            } else {
                                newColumns[i] = null;
                            }
                        }

                        String[] newRowNum = newRow;
                        newRowNum = Utils.sliceStringArray(newRowNum, 1, newRowNum.length);
                        String newRowName = newRow[0];

                        if ((Utils.isArrayOfFloat(newColumns)) && (newColumns.length > 0) ||
                                (Utils.isFloat(newRowName))) {
                            JOptionPane.showMessageDialog(frame, "Column names are empty or contain the numbers!");
                        } else {
                            if (!Utils.isArrayOfFloat(newRowNum)) {
                                JOptionPane.showMessageDialog(frame,
                                        "The value fields contain the characters or separated by commas!");
                            } else {
                                Storage.set(Utils.changeArrayStrToObj(newRow), newColumns);
                                for (int i = 0; i < fieldsColumn.size(); i++) {
                                    model.addColumn(newColumns[i]);
                                }
                                model.addRow(Utils.changeArrayStrToObj(newRow));
                                dialog.dispose();
                            }
                        }
                    }
                });
                buttonPanel.add(saveButton);

                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener (new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
                buttonPanel.add(cancelButton);

                cont.add(panel);
                cont.add(buttonPanel);

                dialog.add(cont);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        JMenuItem removeRowItem = new JMenuItem("Remove selected row");
        fileEdit.add(removeRowItem);
        removeRowItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.getRowSorter().setSortKeys(null);
                int indexRow = table.getSelectedRow();
                if (indexRow >= 0) {
                    model.removeRow(indexRow);
                    Storage.removeRow(indexRow);
                }
            }
        });

        JMenuItem removeColumnItem = new JMenuItem("Remove selected column");
        fileEdit.add(removeColumnItem);
        removeColumnItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int indexColumn = table.getSelectedColumn();
                if (indexColumn > 0) {
                    TableColumn tcol = table.getColumnModel().getColumn(indexColumn);
                    table.removeColumn(tcol);
                    Storage.removeColumn(indexColumn);
                } else {
                    JOptionPane.showMessageDialog(frame, "You can't remove name CPU column!");
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(fileEdit);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.add(tableScrollPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
