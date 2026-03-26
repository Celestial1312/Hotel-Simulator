package Knop;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser {

    public void openFile() {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter JFiles = new FileNameExtensionFilter(
                "JSON Files (*.json)", "json");
        chooser.setFileFilter(JFiles);

        int result = chooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            System.out.println("Selected file: " + chooser.getSelectedFile());
        }
    }
}
/* package Knop;

import Hotelmap.Guis;

import javax.swing.*;

public class FileChooser extends StartGui {

    public void FileChoose() {
        setTitle("Choose a File");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFileChooser chooser = new JFileChooser();
        add(chooser);

        setVisible(true);
    }
}*/
