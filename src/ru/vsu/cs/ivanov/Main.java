package ru.vsu.cs.ivanov;

import ru.vsu.cs.ivanov.model.Storage;
import ru.vsu.cs.ivanov.view.View;

import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Storage storage = new Storage(".\\CPUsData.csv");
        storage.load();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new View();
            }
        });
    }
}
