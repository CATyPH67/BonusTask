package ru.vsu.cs.ivanov.model;

import ru.vsu.cs.ivanov.controller.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Storage {
    public static String pathName;
    public static Object[][] tableData;
    public static String[] tableHeader;

    public Storage(String pathName) throws FileNotFoundException {
        this.pathName = pathName;
        load();
    }

    public void load() throws FileNotFoundException {
        File file = new File(pathName);
        Scanner scan = new Scanner(file);
        String[] lineTextArray = scan.useDelimiter("\\Z").next().split("\r\n");
        String[][] textArray = new String[lineTextArray.length][lineTextArray[0].split(",").length];
        for (int i = 0; i < lineTextArray.length; i++) {
            textArray[i] = lineTextArray[i].split(",");
            String[] rightArray = new String[textArray[0].length];
            for (int j = 0; j < textArray[i].length; j++) {
                if (textArray[i][j].isEmpty()) {
                    rightArray[j] = null;
                } else {
                    rightArray[j] = textArray[i][j];
                }
            }
            for (int j = textArray[i].length; j < textArray[0].length; j++) {
                rightArray[j] = null;
            }
            textArray[i] = rightArray;
        }

        Object[][] infArray = new Object[textArray.length][textArray[0].length];

        for (int i = 0; i < textArray.length; i++) {
            infArray[i] = Utils.changeArrayStrToObj(textArray[i]);
        }

        tableHeader = textArray[0];
        tableData = Utils.sliceTwoDimArray(infArray, 1, infArray.length);


    }

    public static Object[][] getData() {
        return tableData;
    }

    public static String[] getHeader() {
        return tableHeader;
    }

    public static void set(Object[] newRow, String[] newColumns) {
        Object[][] newTableData = new Object[tableData.length + 1][newRow.length];
        Object[] newLineData = newRow;
        for (int i = 0; i < tableData.length; i++) {
            for (int j = 0; j < tableData[i].length; j++) {
                newTableData[i][j] = tableData[i][j];
            }
            for (int j = tableData[i].length; j < newRow.length; j++) {
                newTableData[i][j] = null;
            }
        }
        newTableData[tableData.length] = newLineData;
        tableData = newTableData;

        String[] newTableHeader = new String[tableHeader.length + newColumns.length];
        for (int i = 0; i < tableHeader.length; i++) {
            newTableHeader[i] = tableHeader[i];
        }
        for (int i = tableHeader.length, j = 0; i < (tableHeader.length + newColumns.length); i++, j++) {
            newTableHeader[i] = newColumns[j];
        }
        tableHeader = newTableHeader;
    }

    public static void rewriteFile() throws IOException {
        String[][] stringTableData = new String[tableData.length][tableData[0].length];
        for (int i = 0; i < tableData.length; i++) {
            stringTableData[i] = Utils.changeArrayObjToStr(tableData[i]);
        }

        FileWriter fileWriter = new FileWriter(new File(pathName));
        String joinedString = String.join(",", tableHeader) + '\r' + '\n';
        fileWriter.write(joinedString);
        for (int i = 0; i < stringTableData.length; i++) {
            joinedString = String.join(",", stringTableData[i]) + '\r' + '\n';
            fileWriter.write(joinedString);
        }
        fileWriter.flush();
    }

    public static void removeRow(int indexRow) {
        Object[][] newTableData = new Object[tableData.length - 1][tableData[0].length];
        int count = 0;
        for (int j = 0; j < indexRow; count++, j++) {
            newTableData[count] = tableData[j];
        }
        for (int j = (indexRow + 1); j < tableData.length; count++, j++) {
            newTableData[count] = tableData[j];
        }
        tableData = newTableData;
    }

    public static void removeColumn(int indexColumn) {
        Object[][] newTableData = new Object[tableData.length][tableData[0].length - 1];
        for (int i = 0; i < tableData.length; i++) {
            int count = 0;
            for (int j = 0; j < indexColumn; count++, j++) {
                newTableData[i][count] = tableData[i][j];
            }
            for (int j = indexColumn + 1; j < tableData[0].length - 1; count++, j++) {
                newTableData[i][count] = tableData[i][j];
            }
        }
        tableData = newTableData;

        String[] newTableHeader = new String[tableHeader.length - 1];
        int count = 0;
        for (int i = 0; i < indexColumn; count++, i++) {
            newTableHeader[count] = tableHeader[i];
        }
        for (int i = indexColumn + 1; i < tableHeader.length - 1; count++, i++) {
            newTableHeader[count] = tableHeader[i];
        }
        tableHeader = newTableHeader;
    }
}
