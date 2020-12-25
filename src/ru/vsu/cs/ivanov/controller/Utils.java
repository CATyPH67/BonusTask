package ru.vsu.cs.ivanov.controller;

public class Utils {
    public static String[] changeArrayObjToStr(Object[] arr) {
        String[] textArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null) {
                textArr[i] = arr[i].toString();
            } else {
                textArr[i] = "";
            }
        }
        return textArr;
    }

    public static Object[] changeArrayStrToObj(String[] textArray) {
        Object[] objectArray = new Object[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            if (textArray[i] != null) {
                if (textArray[i].isEmpty()) {
                    textArray[i] = null;
                } else {
                    if (isFloat(textArray[i])) {
                        objectArray[i] = Double.parseDouble(textArray[i]);
                    } else {
                        objectArray[i] = textArray[i];
                    }
                }
            }
        }
        return objectArray;
    }

    public static boolean isArrayOfFloat(String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                if (!isFloat(array[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isFloat(String string) {
        if (string != null) {
            try {
                Double.parseDouble(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return true;
        }
    }

    public static Object[][] sliceTwoDimArray(Object[][] arr, int start, int end) {

        if (arr == null && arr.length == 0) {
            return new String[0][0];
        }
        int arrEnd = arr.length;
        if (end < arrEnd) {
            arrEnd = end;
        }
        Object[][] arrResult = new Object[end - start][arr[0].length];
        for (int i = start, j = 0; i < arrEnd; i++, j++) {
            arrResult[j] = arr[i];
        }
        return arrResult;
    }

    public static String[] sliceStringArray(String[] arr, int start, int end) {
        if (arr == null && arr.length == 0) {
            return new String[0];
        }
        int arrEnd = arr.length;
        if (end < arrEnd) {
            arrEnd = end;
        }
        String[] arrResult = new String[end - start];
        for (int i = start, j = 0; i < arrEnd; i++, j++) {
            arrResult[j] = arr[i];
        }
        return arrResult;
    }
}
