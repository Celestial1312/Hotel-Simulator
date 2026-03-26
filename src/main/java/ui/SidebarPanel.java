package ui;

import simulation.Simulation;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SidebarPanel extends JPanel {
    private final Simulation simulation;

    public SidebarPanel(Simulation simulation) {
        this.simulation = simulation;

       setPreferredSize(new Dimension(600, 1080));
       setBackground(Color.GRAY);
       setLayout(new GridBagLayout());

       JButton uploadButton = new JButton("Upload JSON");
       JButton chooseLayoutButton = new JButton("Choose Layout");

        Dimension size = new Dimension(180, 40);

        uploadButton.setPreferredSize(size);
        chooseLayoutButton.setPreferredSize(size);

       add(uploadButton);
       add(chooseLayoutButton);

       uploadButton.addActionListener(e -> uploadJson());
       chooseLayoutButton.addActionListener(e -> chooseLayout());
    }

    private void uploadJson() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            try {
                File folder = new File("layouts");
                if(!folder.exists()){
                    folder.mkdir();
                }

                File destination = new File(folder, selectedFile.getName());

                Files.copy(selectedFile.toPath(),
                        destination.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );

                JOptionPane.showMessageDialog(this, "File uploaded successfully!", "Upload Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "File upload failed!", "Upload Failed", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        }
    }

    private void chooseLayout() {
        File layoutsFolder = new File("layouts");

        if(!layoutsFolder.exists() ||  !layoutsFolder.isDirectory()){
            JOptionPane.showMessageDialog(this, "Layout folder does not exist or is not a directory!", "Layout Failed", JOptionPane.ERROR_MESSAGE);
        }

        File[] jsonFiles = layoutsFolder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".json")
        );

        if(jsonFiles == null || jsonFiles.length == 0){
            JOptionPane.showMessageDialog(this, "Layout folder does not exist or is not a directory!", "Layout Failed", JOptionPane.ERROR_MESSAGE);
        }

        String[] fileNames = new String[jsonFiles.length];
        for(int i = 0; i < jsonFiles.length; i++){
            fileNames[i] = jsonFiles[i].getName();
        }

        String selectedFileName = (String) JOptionPane.showInputDialog(this, "Choose a layout", "Layouts",
                JOptionPane.QUESTION_MESSAGE, null, fileNames, fileNames[0]
        );

        if (selectedFileName != null) {
            File selectedFile = new File(layoutsFolder, selectedFileName);

            try {
                simulation.loadGridFromJsonFile(selectedFile);
                JOptionPane.showMessageDialog(

                        this,
                        "Loaded layout: " + selectedFileName
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
