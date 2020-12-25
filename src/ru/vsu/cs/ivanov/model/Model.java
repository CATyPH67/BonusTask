package ru.vsu.cs.ivanov.model;

import javax.swing.table.DefaultTableModel;

public class Model extends DefaultTableModel {

    public Model() {
        super(Storage.getData(), Storage.getHeader());
    }

    @Override
    public Class getColumnClass(int column) {
        Class returnValue;
        if ((column >= 0) && (column < getColumnCount()) && (getValueAt(0, column) != null)) {
            returnValue = getValueAt(0, column).getClass();
        } else {
            returnValue = Double.class;
        }
        return returnValue;
    }
}
